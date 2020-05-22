package com.kpi.securityservice.controller

import com.kpi.securityservice.model.User
import com.kpi.securityservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid


@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @RequestMapping(value = ["/current"], method = [RequestMethod.GET])
    fun getUser(principal: Principal): Principal {
        return principal
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = [RequestMethod.POST])
    fun createUser(@RequestBody user: @Valid User) {
        userService.create(user)
    }
}