package com.unimib.oases.ui.screen.login

import com.unimib.oases.data.model.User

sealed class AuthState {
    data object Uninitialized: AuthState()
    data object Unauthenticated: AuthState()
    data class Authenticated(val user: User): AuthState()
}