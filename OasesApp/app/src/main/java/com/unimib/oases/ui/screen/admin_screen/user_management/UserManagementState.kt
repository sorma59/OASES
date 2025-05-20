package com.unimib.oases.ui.screen.admin_screen.user_management

import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User


data class UserManagementState(
    val user: User = User(
        username = "",
        pwHash = "",
        salt = "",
        role = Role.Doctor,
    ),
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    var error: String? = null,
    val message: String? = null
)

