package com.kpi.account.controller

import com.kpi.account.domain.Account
import com.kpi.account.domain.User
import com.kpi.account.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.trace.http.HttpTrace
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid


@RestController
class AccountController {

    @Autowired
    private lateinit var accountService: AccountService

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(path = ["/{name}"], method = [RequestMethod.GET])
    fun getAccountByName(@PathVariable name: String): Account {
        return accountService.findByName(name)
    }

    @RequestMapping(path = ["/current"], method = [RequestMethod.GET])
    fun getCurrentAccount(principal: Principal): Account {
        return accountService.findByName(principal.name)
    }

    @RequestMapping(path = ["/current"], method = [RequestMethod.PUT])
    fun saveCurrentAccount(principal: HttpTrace.Principal, @RequestBody account: @Valid Account) {
        accountService.saveChanges(principal.name, account)
    }

    @RequestMapping(path = ["/"], method = [RequestMethod.POST])
    fun createNewAccount(@RequestBody user: @Valid User): Account {
        return accountService.create(user)
    }
}