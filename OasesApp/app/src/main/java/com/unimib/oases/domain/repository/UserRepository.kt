package com.unimib.oases.domain.repository

import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User

interface UserRepository {
    fun createUser(username: String, password: String, role: Role)
    fun authenticate(username: String, password: String): Boolean
    fun getUser(username: String): User?
    fun deleteUser(username: String)
}