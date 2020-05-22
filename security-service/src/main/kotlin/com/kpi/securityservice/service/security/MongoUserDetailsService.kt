package com.kpi.securityservice.service.security

import com.kpi.securityservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class MongoUserDetailsService: UserDetailsService {

    @Autowired
    private lateinit var repository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findById(username).orElseThrow { UsernameNotFoundException(username) }
    }
}