package com.unimib.oases.domain.usecase

import com.unimib.oases.data.local.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.PasswordUtils
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<User> {
        return try {
            val user = userRepository
                .getUser(username)
                .firstSuccess()

            if (PasswordUtils.verifyPassword(password, user.pwHash, user.salt))
                Resource.Success(user)
            else
                Resource.Error("Wrong password")
        } catch (_: NoSuchElementException){
            Resource.Error("User not found")
        }
        catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}
