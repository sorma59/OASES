package com.unimib.oases.data.repository

import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.PasswordUtils
import com.unimib.oases.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): UserRepository {

    override fun createUser(username: String, password: String, role: Role):Resource<Unit> {
        return try {
            // Generate a unique salt for each password
            val salt = PasswordUtils.generateSalt()
            val hash = PasswordUtils.hashPassword(password, salt)
            val user = User(username, hash, role, salt)
            if (insert(user))
                Resource.Success(Unit)
            else
                Resource.Error("Error creating user")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    private fun insert(user: User): Boolean {
        return try {
            //launch a coroutine to run the suspend function
            CoroutineScope(Dispatchers.IO).launch {
                roomDataSource.insertUser(user)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun authenticate(username: String, password: String): Resource<User> {
        return try {
            //launch a coroutine to run the suspend function
            val user = roomDataSource.getUser(username).firstOrNull()
                ?: return Resource.Error("User not found")
            if (PasswordUtils.verifyPassword(password, user.pwHash, user.salt))
                Resource.Success(user)
            else
                Resource.Error("Wrong password")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }

    }

    override fun deleteUser(username: String): Resource<Unit> {
        return try {
            //launch a coroutine to run the suspend function
            CoroutineScope(Dispatchers.IO).launch {
                roomDataSource.getUser(username).firstOrNull()?.let {
                    roomDataSource.deleteUser(it)
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getAllUsers(): Flow<Resource<List<User>>> = flow {

        emit(Resource.Loading())
        try {
            roomDataSource.getAllUsers().collect {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getAllNurses(): Flow<Resource<List<User>>> = flow {

        emit(Resource.Loading())
        try {
            roomDataSource.getAllUsersByRole(Role.Nurse).collect {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getAllDoctors(): Flow<Resource<List<User>>> = flow {

        emit(Resource.Loading())
        try {
            roomDataSource.getAllUsersByRole(Role.Doctor).collect {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getUser(username: String): Flow<Resource<User?>> = flow {

        emit(Resource.Loading())
        try {
            roomDataSource.getUser(username).collect {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}