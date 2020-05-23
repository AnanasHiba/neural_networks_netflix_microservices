package com.kpi.galleryservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Document(collection = "projects")
class Project (@NotBlank @Size(max = 30) var title: String) {
    @Id
    var id: String = UUID.randomUUID().toString()
    lateinit var accountName: String
    var description = ""
}