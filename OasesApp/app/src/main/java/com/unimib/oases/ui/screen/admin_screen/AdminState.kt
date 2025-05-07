package com.unimib.oases.ui.screen.admin_screen

import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User


data class AdminState(
    val user: User = User(
        username = "",
        pwHash = "",
        salt = "",
        role = Role.Nurse ,
    ),
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    var error: String? = null,
    val message: String? = null
)

