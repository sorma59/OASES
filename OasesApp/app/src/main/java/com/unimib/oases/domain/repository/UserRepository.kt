package com.unimib.oases.domain.repository

import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User

interface UserRepository {
    fun createAccount(username: String, password: String, role: Role)
    fun authenticate(username: String, password: String): Boolean
    fun getUser(): User?
    fun deleteAccount()
}