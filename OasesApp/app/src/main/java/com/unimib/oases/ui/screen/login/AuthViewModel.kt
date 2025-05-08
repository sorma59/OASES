package com.unimib.oases.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private var _authState = MutableStateFlow<AuthState>(AuthState.Uninitialized)
    val authState = _authState.asStateFlow()

    private var _creationResult = MutableStateFlow<Resource<Unit>>(Resource.None())
    val creationResult = _creationResult.asStateFlow()

    fun createUser(
        username: String,
        password: String,
        role: Role,
    ) {
        _creationResult.value = Resource.Loading()
        val result = userRepository.createUser(username, password, role)
        _creationResult.value = result
    }

    fun currentUser(): User? {
        return if (authState.value is AuthState.Authenticated) {
            (authState.value as AuthState.Authenticated).user
        } else
            null
    }

    fun authenticate(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = userRepository.authenticate(username, password)
            if (result is Resource.Success) {
                val user = result.data
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Error("User not found")
                }
            } else {
                Log.e("AuthViewModel", "Authentication failed: ${result.message}")
                _authState.value = AuthState.Error((result as Resource.Error).message ?: "Unknown error")
            }
        }
    }

    fun isNurse():Boolean {
        return authState.value is AuthState.Authenticated && (authState.value as AuthState.Authenticated).user.role == Role.Nurse
    }

    fun isDoctor(): Boolean {
        return authState.value is AuthState.Authenticated && (authState.value as AuthState.Authenticated).user.role == Role.Doctor
    }

    fun isAdmin(): Boolean {
        return authState.value is AuthState.Authenticated && (authState.value as AuthState.Authenticated).user.role == Role.Admin
    }
}