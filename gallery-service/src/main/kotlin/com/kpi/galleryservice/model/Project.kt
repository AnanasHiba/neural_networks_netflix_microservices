package com.kpi.galleryservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Document(collection = "gallery")
class Project(@NotBlank @Size(max = 30) private var title: String, var description: String) {
    var id: String = UUID.randomUUID().toString()

}