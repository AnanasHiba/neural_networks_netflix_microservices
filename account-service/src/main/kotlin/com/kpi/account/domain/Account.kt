package com.kpi.account.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.math.min

@Document(collection = "accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
class Account(
        @Id
        var name: String,
        var lastSeen: Date) {

    @Length(min = 0, max = 20_000)
    var note: String = ""
}
