package com.unimib.oases.domain.usecase

import com.unimib.oases.data.local.model.User
import com.unimib.oases.ui.screen.login.AuthManager
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authManager: AuthManager
) {
    operator fun invoke(): User? = authManager.getCurrentUser()
}