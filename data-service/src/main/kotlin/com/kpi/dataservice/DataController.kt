package com.kpi.dataservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/data")
class DataController {
    @Autowired
    lateinit var dataService: DataService
    @GetMapping("/users")
    fun getAllUsers(): String {
        return dataService.getAllUsers()
    }
}