package com.unimib.oases.data.repository

import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.PasswordUtils
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): UserRepository {

    override suspend fun createUser(username: String, password: String, role: Role): Outcome<Unit> {
        return try {
            // Generate a unique salt for each password
            val salt = PasswordUtils.generateSalt()
            val hash = PasswordUtils.hashPassword(password, salt)
            val user = User(username, hash, role, salt)
            if (insert(user))
                Outcome.Success(Unit)
            else
                Outcome.Error("Error creating user")
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun insert(user: User): Boolean {
        return try {
            roomDataSource.insertUser(user)
            true
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun deleteUser(user: User): Outcome<Unit> {
        return try {
            roomDataSource.deleteUser(user)
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "Unknown error")
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

    override fun getUser(username: String): Flow<Resource<User>> = flow {
        roomDataSource.getUser(username)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect { entity ->
                val resource = when (entity) {
                    null -> Resource.NotFound()
                    else -> Resource.Success(entity)
                }
                emit(resource)
            }
    }
}