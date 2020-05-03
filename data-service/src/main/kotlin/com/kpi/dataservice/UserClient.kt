package com.kpi.dataservice

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient("users")
interface UserClient {
    @RequestMapping(method = arrayOf(RequestMethod.GET), value = ["api/users"])
    fun getAllUsers(): String
}