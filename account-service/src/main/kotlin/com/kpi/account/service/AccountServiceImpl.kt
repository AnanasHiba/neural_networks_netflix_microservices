package com.kpi.account.service

import com.kpi.account.client.AuthServiceClient
import com.kpi.account.domain.Account
import com.kpi.account.domain.User
import com.kpi.account.repository.AccountRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.math.BigDecimal
import java.util.*


@Service
class AccountServiceImpl : AccountService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var authClient: AuthServiceClient

    @Autowired
    private lateinit var repository: AccountRepository


    override fun findByName(accountName: String): Account {
        Assert.hasLength(accountName)
        return repository.findByName(accountName)
    }

    override fun create(user: User): Account {
        val existing: Account = repository.findByName(user.username)
        Assert.isNull(existing, "account already exists: " + user.username)
        authClient.createUser(user)
        val account = Account(user.username, Date())
        repository.save(account)
        log.info("new account has been created: " + account.name)
        return account
    }

    override fun saveChanges(name: String, update: Account) {
        val account: Account = repository.findByName(name)
        Assert.notNull(account, "can't find account with name $name")
        account.note = update.note
        account.lastSeen = Date()
        repository.save(account)
        log.debug("account {} changes has been saved", name)
    }
}