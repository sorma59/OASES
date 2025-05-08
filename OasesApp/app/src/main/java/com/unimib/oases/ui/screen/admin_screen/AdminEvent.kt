package com.unimib.oases.ui.screen.admin_screen

import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User

sealed class AdminEvent {
    data class EnteredUsername(val value: String) : AdminEvent()
    data class EnteredPassword(val value: String) : AdminEvent()
    data class SelectedRole(val value: Role) : AdminEvent()
    data class Delete(val value: User) : AdminEvent()
    data class Click(val value: User) : AdminEvent()
    data object SaveUser : AdminEvent()
    data object UndoDelete: AdminEvent()
}