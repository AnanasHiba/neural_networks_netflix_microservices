package com.kpi.zuulservice.security

import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtTokenAuthenticationFilter(private val jwtConfig: JwtConfig): OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        val header = request.getHeader(jwtConfig.getHeader())

        // 2. validate the header and check the prefix

        // 2. validate the header and check the prefix
        if (header == null || !header.startsWith(jwtConfig.getPrefix()!!)) {
            chain.doFilter(request, response) // If not valid, go to the next filter.
            return
        }

        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.

        // All secured paths that needs a token are already defined and secured in config class.
        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.

        // 3. Get the token

        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.

        // All secured paths that needs a token are already defined and secured in config class.
        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.

        // 3. Get the token
        val token = header.replace(jwtConfig.getPrefix()!!, "")

        // exceptions might be thrown in creating the claims if for example the token is expired

        // exceptions might be thrown in creating the claims if for example the token is expired
        try {
            // 4. Validate the token
            val claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret()!!.toByteArray())
                    .parseClaimsJws(token)
                    .body
            val username = claims.subject
            if (username != null) {
                val authorities = claims["authorities"] as List<String>?

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                val auth = UsernamePasswordAuthenticationToken(
                        username, null, authorities!!.stream().map { role: String? -> SimpleGrantedAuthority(role) }.collect(Collectors.toList()))

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().authentication = auth
            }
        } catch (e: Exception) {
            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext()
        }

        // go to the next filter in the filter chain

        // go to the next filter in the filter chain
        chain.doFilter(request, response)
    }
}