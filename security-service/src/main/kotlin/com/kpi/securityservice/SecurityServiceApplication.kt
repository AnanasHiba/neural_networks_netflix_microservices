package com.kpi.securityservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class SecurityServiceApplication

fun main(args: Array<String>) {
	runApplication<SecurityServiceApplication>(*args)
}
