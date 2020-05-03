package com.kpi.dataservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("dataService")
class DataServiceImpl : DataService {
    @Autowired
    lateinit var userClient: UserClient
    override fun getAllUsers(): String {
        return userClient.getAllUsers()
    }
}