package com.unimib.oases.ui.screen.dashboard.admin.user

import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User


data class UserManagementState(
    val user: User = User(
        username = "",
        pwHash = "",
        salt = "",
        role = Role.DOCTOR,
    ),
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null
)

