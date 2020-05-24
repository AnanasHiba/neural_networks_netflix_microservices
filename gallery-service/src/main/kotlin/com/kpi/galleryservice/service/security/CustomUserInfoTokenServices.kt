package com.kpi.galleryservice.service.security

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.OAuth2RestOperations
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
import java.util.*
import java.util.Collections.singletonMap
import kotlin.collections.LinkedHashSet


class CustomUserInfoTokenServices(private val userInfoEndpointUrl: String, private val clientId: String) : ResourceServerTokenServices {
    protected val logger: Log = LogFactory.getLog(javaClass)
    @Autowired
    private lateinit var restTemplate: OAuth2RestOperations
    private var tokenType: String = DefaultOAuth2AccessToken.BEARER_TYPE
    private var authoritiesExtractor: AuthoritiesExtractor = FixedAuthoritiesExtractor()

    fun setTokenType(tokenType: String) {
        this.tokenType = tokenType
    }

    fun setRestTemplate(restTemplate: OAuth2RestOperations) {
        this.restTemplate = restTemplate
    }

    fun setAuthoritiesExtractor(authoritiesExtractor: AuthoritiesExtractor) {
        this.authoritiesExtractor = authoritiesExtractor
    }

    @Throws(AuthenticationException::class, InvalidTokenException::class)
    override fun loadAuthentication(accessToken: String): OAuth2Authentication {
        val map = getMap(userInfoEndpointUrl, accessToken)
        if (map != null) {
            if (map.containsKey("error")) {
                logger.debug("userinfo returned error: " + map["error"])
                throw InvalidTokenException(accessToken)
            }
        }
        return extractAuthentication(map)
    }

    private fun extractAuthentication(map: MutableMap<*, *>?): OAuth2Authentication {
        val principal = getPrincipal(map)
        val request: OAuth2Request = getRequest(map)
        val authorities: List<GrantedAuthority> = authoritiesExtractor
                .extractAuthorities(map as MutableMap<String, Any>)
        val token = UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities)
        token.details = map
        return OAuth2Authentication(request, token)
    }


    private fun getPrincipal(map: MutableMap<*, *>?): Any? {
        for (key in PRINCIPAL_KEYS) {
            if (map != null) {
                if (map.containsKey(key)) {
                    return map[key]
                }
            }
        }
        return "unknown"
    }

    @Suppress("UNCHECKED_CAST")
    private fun getRequest(map: MutableMap<*, *>?): OAuth2Request {
        val request = map?.get("oauth2Request") as Map<String, Any>
        val clientId = request["clientId"] as String
        val scope: Set<String> =
                if (request.containsKey("scope")) {
                    LinkedHashSet(request["scope"] as Collection<String>)
                }
                else {
                    LinkedHashSet(emptySet<String>())
                }


        return OAuth2Request(null, clientId, null, true, HashSet(scope),
                null, null, null, null)
    }

    override fun readAccessToken(accessToken: String): OAuth2AccessToken {
        throw UnsupportedOperationException("Not supported: read access token")
    }

    @Suppress("UNCHECKED_CAST")
    private fun getMap(path: String, accessToken: String): MutableMap<*, *>? {
        logger.debug("Getting user info from: $path")
        try {
            var restTemplate: OAuth2RestOperations? = this.restTemplate
            if (restTemplate == null) {
                val resource = BaseOAuth2ProtectedResourceDetails()
                resource.clientId = this.clientId
                restTemplate = OAuth2RestTemplate(resource)
            }
            val existingToken: OAuth2AccessToken? = restTemplate.oAuth2ClientContext
                    .accessToken
            if (existingToken == null || accessToken != existingToken.value) {
                val token = DefaultOAuth2AccessToken(
                        accessToken)
                token.tokenType = this.tokenType
                restTemplate.oAuth2ClientContext.accessToken = token
            }
            return restTemplate.getForEntity(path, MutableMap::class.java).body
        } catch (ex: Exception) {
            logger.info("Could not fetch user details: " + ex.javaClass + ", "
                    + ex.message)
            return singletonMap<String, Any>("error",
                    "Could not fetch user details")
        }
    }

    companion object {
        private val PRINCIPAL_KEYS = arrayOf("user", "username",
                "userid", "user_id", "login", "id", "name")
    }

}