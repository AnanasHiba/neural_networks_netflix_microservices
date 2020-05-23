package com.kpi.galleryservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@SpringBootApplication
@EnableEurekaClient
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled = true)
class GalleryServiceApplication

fun main(args: Array<String>) {
	runApplication<GalleryServiceApplication>(*args)
}


