package com.kpi.galleryservice

import com.kpi.galleryservice.model.Project
import com.kpi.galleryservice.repository.ProjectRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import reactor.core.publisher.Flux


@SpringBootApplication
@EnableEurekaClient
@EnableReactiveMongoRepositories
class GalleryServiceApplication{
	@Bean
	fun run(projectRepository: ProjectRepository): CommandLineRunner? {
		return CommandLineRunner { args: Array<String?>? ->
			projectRepository.deleteAll()
					.thenMany(Flux.just(
							Project("1", "Pets"),
							Project("2", "Cats"),
							Project("3", "Dogs"),
							Project("4", "Secure"),
							Project("5", "Guessing"),
							Project("6", "Shoes")
					)
							.flatMap(projectRepository::save))
					.thenMany(projectRepository.findAll())
					.subscribe(System.out::println)
		}
	}
}

fun main(args: Array<String>) {
	runApplication<GalleryServiceApplication>(*args)
}


