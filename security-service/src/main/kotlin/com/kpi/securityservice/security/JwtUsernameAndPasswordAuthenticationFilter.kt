package com.kpi.securityservice.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtUsernameAndPasswordAuthenticationFilter(private var authManager: AuthenticationManager, private var jwtConfig: JwtConfig): UsernamePasswordAuthenticationFilter() {

    init {
        this.setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher(jwtConfig.getUri(), "POST"))
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        return try {

            // 1. Get credentials from request
            val creds = ObjectMapper().readValue(request.inputStream, UserCredentials::class.java)

            // 2. Create auth object (contains credentials) which will be used by auth manager
            val authToken = UsernamePasswordAuthenticationToken(
                    creds.username, creds.password, Collections.emptyList())

            // 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
            this.authManager.authenticate(authToken)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private class UserCredentials {
        lateinit var username: String
        lateinit var password: String

    }
}
