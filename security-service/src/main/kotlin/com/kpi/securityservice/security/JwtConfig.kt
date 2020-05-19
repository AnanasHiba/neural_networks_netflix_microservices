package com.kpi.securityservice.security

import org.springframework.beans.factory.annotation.Value

class JwtConfig {

    @Value("\${security.jwt.uri:/auth/**}")
    private lateinit var uri: String

    @Value("\${security.jwt.header:Authorization}")
    private lateinit var header: String

    @Value("\${security.jwt.prefix:Bearer }")
    private lateinit var prefix: String

    @Value("\${security.jwt.expiration:#{24*60*60}}")
    private var expiration = 0

    @Value("\${security.jwt.secret:JwtSecretKey}")
    private lateinit var secret: String

    fun getUri(): String {
        return uri
    }

    fun getHeader(): String {
        return header
    }

    fun getPrefix(): String {
        return prefix
    }

    fun getExpiration(): Int {
        return expiration
    }

    fun getSecret(): String {
        return secret
    }
}