package com.unimib.oases.domain.usecase

import com.unimib.oases.data.local.model.Role
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.Resource
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repo: UserRepository
) {

    operator fun invoke(username: String, pwHash: String, role: Role): SaveUserUseCaseResult {

        val usernameError = validateUsername(username)
        val pwHashError = validatePassword(pwHash)

        if (usernameError != null || pwHashError != null) {
            return SaveUserUseCaseResult.ValidationFailure(
                usernameError = usernameError,
                passwordError = pwHashError
            )
        }

        return when (val result = repo.createUser(username, pwHash, role)){
            is Resource.Error -> {
                SaveUserUseCaseResult.RepositoryFailure(result.message ?: "Unknown error")
            }
            is Resource.Success -> {
                SaveUserUseCaseResult.Success
            }
            else -> {
                SaveUserUseCaseResult.UnknownError
            }
        }
    }

    private fun validateUsername(username: String): String? {
        if (username.isBlank()) {
            return "The username can't be blank"
        }
        return null
    }

    private fun validatePassword(pwHash: String): String? {
        if (pwHash.isBlank()) {
            return "The password can't be blank"
        }
        return null
    }

    sealed class SaveUserUseCaseResult {
        data object Success : SaveUserUseCaseResult()
        data class ValidationFailure(val usernameError: String?, val passwordError: String?) : SaveUserUseCaseResult()
        data class RepositoryFailure(val errorMessage: String) : SaveUserUseCaseResult()
        data object UnknownError : SaveUserUseCaseResult()
    }
}