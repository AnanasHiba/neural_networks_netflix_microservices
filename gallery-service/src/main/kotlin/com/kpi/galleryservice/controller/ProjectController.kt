package com.kpi.galleryservice.controller

import com.kpi.galleryservice.model.Project
import com.kpi.galleryservice.repository.ProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.logging.Logger
import javax.validation.Valid


@RestController
@RequestMapping("/")
class ProjectController {
    var logger = Logger.getLogger(ProjectController::class.java.getName())

    @Autowired
    private lateinit var env: Environment

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @RequestMapping("/")
    fun home(): String? {
        val home = "Gallery-Service running at port: " + env.getProperty("local.server.port")
        logger.info(home)
        return home
    }

    @GetMapping("/getAll")
    fun getAllProjects(): Flux<Project> {
        return projectRepository.findAll()
    }

    @PostMapping("/create")
    fun createProject(@RequestBody project: @Valid Project): Mono<Project> {
        return projectRepository.save(project)
    }

    @GetMapping("/get/{id}")
    fun getProjectById(@PathVariable(value = "id") projectId: String): Mono<ResponseEntity<Project>> {
        return projectRepository.findById(projectId)
                .map { saveProject -> ResponseEntity.ok(saveProject) } // the map operator is called on this Project to wrap it in a ResponseEntity object with status code 200 OK
                .defaultIfEmpty(ResponseEntity.notFound().build()) // finally there is a call to defaultIfEmpty to build an empty ResponseEntity with status 404 NOT FOUND if the Project was not found.
    }

    @PutMapping("/update/{id}")
    fun updateProject(@PathVariable(value = "id") projectId: String,
                     @RequestBody project: @Valid Project): Mono<ResponseEntity<Project>> {
        return projectRepository.findById(projectId)
                .flatMap{ existingProject ->
                    existingProject.description = project.description
                    projectRepository.save(existingProject)
                }
                .map{ updateProject -> ResponseEntity(updateProject, HttpStatus.OK) }
                // it saves them to the database and wraps this updated Project in a ResponseEntity with status code 200 OK in case of success or 404 NOT FOUND in case of failure.
                .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping("/delete/{id}")
    fun deleteProject(@PathVariable(value = "id") projectId: String): Mono<ResponseEntity<Void>> {
        return projectRepository.findById(projectId) // First, you search the Project you want to delete.
                .flatMap{ existingProject ->
                    projectRepository.delete(existingProject) // Next, you delete and return 200 OK to show your delete was successful
                            .then(Mono.just(ResponseEntity<Void>(HttpStatus.OK)))
                }
                .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND)) // or you return 404 NOT FOUND to say the Project was not found
    }

    @DeleteMapping("/deleteAllProjects")
    fun deleteAllProjects(): Mono<Void?>? {
        return projectRepository.deleteAll()
    }

}