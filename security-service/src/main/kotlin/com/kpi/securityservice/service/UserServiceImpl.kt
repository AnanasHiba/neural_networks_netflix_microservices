package com.kpi.securityservice.service

import com.kpi.securityservice.model.User
import com.kpi.securityservice.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl: UserService {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private val encoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Autowired
    private lateinit var repository: UserRepository

    override fun create(user: User) {
        val existing: Optional<User> = repository.findById(user.username)
        existing.ifPresent { throw IllegalArgumentException("user already exists: " + it.username) }

        val hash = encoder.encode(user.password)
        user.password = hash

        repository.save(user)

        log.info("new user has been created: {}", user.username)
    }
}