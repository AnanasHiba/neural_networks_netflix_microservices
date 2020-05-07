package com.kpi.zuulservice.security

import org.springframework.beans.factory.annotation.Value

class JwtConfig {

    @Value("\${security.jwt.uri:/auth/**}")
    private var Uri: String? = null

    @Value("\${security.jwt.header:Authorization}")
    private var header: String? = null

    @Value("\${security.jwt.prefix:Bearer }")
    private var prefix: String? = null

    @Value("\${security.jwt.expiration:#{24*60*60}}")
    private var expiration = 0

    @Value("\${security.jwt.secret:JwtSecretKey}")
    private var secret: String? = null

    fun getUri(): String? {
        return Uri
    }

    fun getHeader(): String? {
        return header
    }

    fun getPrefix(): String? {
        return prefix
    }

    fun getExpiration(): Int {
        return expiration
    }

    fun getSecret(): String? {
        return secret
    }
}