package com.unimib.oases.domain.usecase


import com.unimib.oases.data.local.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repo: UserRepository
) {

    fun createUser(user: User) {
        repo.createUser(user.username, user.pwHash, user.role)
    }

    fun getUsers(): Flow<Resource<List<User>>> {
        val result = repo.getAllUsers()
        return result
    }

    suspend fun deleteUser(user: User){
        repo.deleteUser(user)
    }

}