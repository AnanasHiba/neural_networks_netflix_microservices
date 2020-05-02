package com.kpi.neural_network
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class NeuralNetworkApplication

fun main(args: Array<String>) {
	runApplication<NeuralNetworkApplication>(*args)
}
