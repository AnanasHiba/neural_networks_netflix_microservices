package com.kpi.userservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service("userService")
class UserServiceImpl : UserService {
    @Autowired
    lateinit var userRepository: UserRepository
    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}