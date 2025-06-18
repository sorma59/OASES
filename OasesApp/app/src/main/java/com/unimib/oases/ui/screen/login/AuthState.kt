package com.unimib.oases.ui.screen.login

import com.unimib.oases.data.local.model.User

sealed class AuthState {
    data object Uninitialized: AuthState()
    data object Loading: AuthState()
    data class Error(val message: String): AuthState()
    data object Unauthenticated: AuthState()
    data class Authenticated(val user: User): AuthState()
}