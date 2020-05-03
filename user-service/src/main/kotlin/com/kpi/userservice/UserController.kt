package com.kpi.userservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class UserController {
    @Autowired
    lateinit var userService: UserService
    @GetMapping("/users")
    fun getUsers(): String {
        val users: List<User> = userService.getAllUsers()
        val names: MutableList<String> = users.map { it.name  }.toMutableList()
        return names.joinToString()
    }
}