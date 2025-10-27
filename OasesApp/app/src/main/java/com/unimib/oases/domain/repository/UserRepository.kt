package com.unimib.oases.domain.repository

import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(username: String, password: String, role: Role): Outcome
    fun getUser(username: String): Flow<Resource<User>>
    suspend fun deleteUser(user: User): Outcome
    fun getAllUsers(): Flow<Resource<List<User>>>
    fun getAllNurses(): Flow<Resource<List<User>>>
    fun getAllDoctors(): Flow<Resource<List<User>>>
}