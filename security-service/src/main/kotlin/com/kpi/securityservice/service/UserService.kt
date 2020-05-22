package com.kpi.securityservice.service

import com.kpi.securityservice.model.User

interface UserService {
    fun create(user: User)
}