package com.unimib.oases.domain.repository

import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun createUser(username: String, password: String, role: Role): Resource<Unit>
    suspend fun authenticate(username: String, password: String): Resource<User>
    fun getUser(username: String): Flow<Resource<User?>>
    fun deleteUser(username: String): Resource<Unit>
    fun getAllUsers(): Flow<Resource<List<User>>>
    fun getAllNurses(): Flow<Resource<List<User>>>
    fun getAllDoctors(): Flow<Resource<List<User>>>
}