package com.kpi.account.service

import com.kpi.account.domain.Account
import com.kpi.account.domain.User

interface AccountService {

    fun findByName(accountName: String): Account

    fun create(user: User): Account

    fun saveChanges(name: String, update: Account)
}