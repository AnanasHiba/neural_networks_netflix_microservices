package com.kpi.securityservice.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.sql.Date
import java.util.*
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtUsernameAndPasswordAuthenticationFilter(private var authManager: AuthenticationManager, private var jwtConfig: JwtConfig): UsernamePasswordAuthenticationFilter() {

    init {
        this.setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher(jwtConfig.getUri(), "POST"))
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        try {

            // 1. Get credentials from request
            val creds = ObjectMapper().readValue(request.inputStream, UserCredentials::class.java)

            // 2. Create auth object (contains credentials) which will be used by auth manager
            val authToken = UsernamePasswordAuthenticationToken(
                    creds.username, creds.password, Collections.emptyList())

            // 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
            return this.authManager.authenticate(authToken)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain,
                                          auth: Authentication) {
        val now = System.currentTimeMillis()
        val token: String = Jwts.builder()
                .setSubject(auth.name) // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", auth.authorities.stream()
                        .map { obj: GrantedAuthority -> obj.authority }.collect(Collectors.toList()))
                .setIssuedAt(Date(now))
                .setExpiration(Date(now + jwtConfig.getExpiration() * 1000)) // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().toByteArray())
                .compact()

        // Add token to header
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token)
    }

    private class UserCredentials {
        lateinit var username: String
        lateinit var password: String

    }
}
