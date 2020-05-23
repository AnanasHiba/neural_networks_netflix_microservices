package com.kpi.galleryservice.controller

import com.kpi.galleryservice.domain.Project
import com.kpi.galleryservice.service.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid


@RestController
@RequestMapping("/")
class ProjectController {

    @Autowired
    private lateinit var environment: Environment

    @Autowired
    private lateinit var projectService: ProjectService


    @RequestMapping("/")
    fun home(): String {
        return "Gallery-Service running at port: " + environment.getProperty("local.server.port")
    }

    @RequestMapping(path = ["/get_all"], method = [RequestMethod.GET])
    fun getAllProjects(principal: Principal): MutableList<Project> {
        return projectService.findAll(principal.name)
    }

    @PostMapping("/create")
    fun createProject(principal: Principal, @RequestBody project: @Valid Project): Project {
        return projectService.create(project,principal.name)
    }

    @GetMapping("/get/{id}")
    fun getProjectById(principal: Principal, @PathVariable(value = "id") projectId: String): Project {
        return projectService.get(projectId, principal.name)
    }

    @PutMapping("/update")
    fun updateProject(principal: Principal,
                     @RequestBody project: @Valid Project) {
        projectService.saveChanges(project, principal.name)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteProject(principal: Principal, @PathVariable(value = "id") projectId: String) {
        projectService.delete(projectId, principal.name)
    }

}