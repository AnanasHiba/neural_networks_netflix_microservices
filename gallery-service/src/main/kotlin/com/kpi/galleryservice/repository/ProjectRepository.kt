package com.kpi.galleryservice.repository

import com.kpi.galleryservice.domain.Project
import org.springframework.data.repository.CrudRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository: CrudRepository<Project,String> {
    @Nullable
    fun findAllByAccountName(userId: String): MutableList<Project>

    @Nullable
    fun findByTitleAndAccountName(title: String, accountName: String): Project

    @Nullable
    fun findByIdAndAccountName(id: String, accountName: String): Project

}