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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
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

    override suspend fun deleteUser(user: User): Resource<Unit> {
        return try {
            roomDataSource.deleteUser(user)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getAllUsers(): Flow<Resource<List<User>>> = flow {
        roomDataSource.getAllUsers()
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect {
                emit(Resource.Success(it))
            }
    }

    override fun getAllNurses(): Flow<Resource<List<User>>> = flow {
        roomDataSource.getAllUsersByRole(Role.NURSE)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect {
                emit(Resource.Success(it))
            }
    }

    override fun getAllDoctors(): Flow<Resource<List<User>>> = flow {
        roomDataSource.getAllUsersByRole(Role.DOCTOR)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect {
                emit(Resource.Success(it))
            }
    }

    override fun getUser(username: String): Flow<Resource<User?>> = flow {
        roomDataSource.getUser(username)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect {
                emit(Resource.Success(it))
            }
    }
}