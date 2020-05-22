package com.kpi.account.client

import com.kpi.account.domain.User
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


@FeignClient(name = "security-service")
interface AuthServiceClient {
    @RequestMapping(method = [RequestMethod.POST], value = ["/uaa/user"], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun createUser(user: User)
}