package com.kpi.securityservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityServiceApplication

fun main(args: Array<String>) {
	runApplication<SecurityServiceApplication>(*args)
}
