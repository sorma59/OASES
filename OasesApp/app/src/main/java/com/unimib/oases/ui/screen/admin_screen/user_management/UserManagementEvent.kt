package com.unimib.oases.ui.screen.admin_screen.user_management

import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User

sealed class UserManagementEvent {
    data class EnteredUsername(val value: String) : UserManagementEvent()
    data class EnteredPassword(val value: String) : UserManagementEvent()
    data class SelectedRole(val value: Role) : UserManagementEvent()
    data class Delete(val value: User) : UserManagementEvent()
    data class UserClicked(val value: User) : UserManagementEvent()
    data object SaveUser : UserManagementEvent()
    data object UndoDelete: UserManagementEvent()

    data object OnToastShown : UserManagementEvent()
}