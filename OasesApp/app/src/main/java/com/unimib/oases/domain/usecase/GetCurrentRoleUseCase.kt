package com.unimib.oases.domain.usecase

import com.unimib.oases.data.local.model.Role
import com.unimib.oases.ui.screen.login.AuthManager
import javax.inject.Inject

class GetCurrentRoleUseCase @Inject constructor(
    private val authManager: AuthManager
) {
    operator fun invoke(): Role? = authManager.getCurrentRole()
}