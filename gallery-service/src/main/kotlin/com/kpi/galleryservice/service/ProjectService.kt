package com.kpi.galleryservice.service

import com.kpi.galleryservice.domain.Project

interface ProjectService {

    fun findAll(accountName: String): MutableList<Project>

    fun create(project: Project, accountName: String): Project

    fun saveChanges(project: Project, accountName: String)

    fun get(id: String, accountName: String): Project

    fun delete(id: String, accountName: String)
}