package com.kpi.galleryservice.service

import com.kpi.galleryservice.domain.Project
import com.kpi.galleryservice.repository.ProjectRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class ProjectServiceImpl: ProjectService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var repository: ProjectRepository

    override fun findAll(accountName: String): MutableList<Project> {
        return repository.findAllByAccountName(accountName)
    }

    override fun get(id: String, accountName: String): Project {
        val project: Project = repository.findByIdAndAccountName(id, accountName)
        Assert.notNull(project, "can't find project $id")
        return project
    }

    override fun delete(id: String, accountName: String) {
        val project: Project = repository.findByIdAndAccountName(id, accountName)
        Assert.notNull(project, "can't find project $id")
        repository.delete(project)
    }

    override fun create(project: Project, accountName: String): Project {
        val existing: Project = repository.findByTitleAndAccountName(project.title, accountName)
        Assert.isNull(existing, "project already exists: " + project.title)
        project.accountName = accountName
        repository.save(project)
        log.info("new project has been created: " + project.title)
        return project
    }

    override fun saveChanges(update: Project, accountName: String) {
        val projectOpt: Optional<Project> = repository.findById(update.id)
        Assert.notNull(projectOpt.get(), "can't find project with name ${update.title}")
        var project = projectOpt.get()
        project.title = update.title
        project.description = update.description
        repository.save(project)
        log.debug("account {} changes has been saved", project.title)
    }
}