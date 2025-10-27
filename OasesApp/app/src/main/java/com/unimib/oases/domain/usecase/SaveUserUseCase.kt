package com.unimib.oases.domain.usecase

import com.unimib.oases.data.local.model.Role
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repo: UserRepository
) {

    suspend operator fun invoke(username: String, pwHash: String, role: Role): SaveUserUseCaseResult {

        val usernameError = validateUsername(username)
        val pwHashError = validatePassword(pwHash)

        if (usernameError != null || pwHashError != null) {
            return SaveUserUseCaseResult.ValidationFailure(
                usernameError = usernameError,
                passwordError = pwHashError
            )
        }

        return when (repo.createUser(username, pwHash, role)){
            is Outcome.Error -> {
                SaveUserUseCaseResult.RepositoryFailure
            }
            is Outcome.Success -> {
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
        data object RepositoryFailure : SaveUserUseCaseResult()
        data object UnknownError : SaveUserUseCaseResult()
    }
}