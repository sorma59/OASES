package com.unimib.oases.ui.screen.login

sealed class AuthState {
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    object Idle : AuthState() // no login attempt ongoing
}