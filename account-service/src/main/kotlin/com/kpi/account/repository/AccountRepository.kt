package com.kpi.account.repository

import com.kpi.account.domain.Account
import org.springframework.data.repository.CrudRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: CrudRepository<Account, String> {
    @Nullable
    fun findByName(name: String): Account
}