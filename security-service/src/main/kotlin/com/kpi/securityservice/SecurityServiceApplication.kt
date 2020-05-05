package com.kpi.securityservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecurityServiceApplication

fun main(args: Array<String>) {
	runApplication<SecurityServiceApplication>(*args)
}