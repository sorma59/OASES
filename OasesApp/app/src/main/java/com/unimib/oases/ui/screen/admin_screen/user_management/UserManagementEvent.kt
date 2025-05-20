package com.unimib.oases.ui.screen.admin_screen.user_management

import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User

sealed class UserManagementEvent {
    data class EnteredUsername(val value: String) : UserManagementEvent()
    data class EnteredPassword(val value: String) : UserManagementEvent()
    data class SelectedRole(val value: Role) : UserManagementEvent()
    data class Delete(val value: User) : UserManagementEvent()
    data class Click(val value: User) : UserManagementEvent()
    data object SaveUser : UserManagementEvent()
    data object UndoDelete: UserManagementEvent()
}