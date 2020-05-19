package com.kpi.galleryservice.repository

import com.kpi.galleryservice.model.Project
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository: ReactiveMongoRepository<Project,String> {
}