package com.kpi.userservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class UserServiceApplication(private val userRepository: UserRepository)

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}
private fun createUsers(userRepository: UserRepository) {
	userRepository.save(User(null, "Peter"))
	userRepository.save(User(null, "John"))
	userRepository.save(User(null, "Sofia"))
	userRepository.save(User(null, "George"))
}