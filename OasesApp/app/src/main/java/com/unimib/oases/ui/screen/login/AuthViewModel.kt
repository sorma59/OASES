package com.unimib.oases.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: AuthManager,
    private val userRepository: UserRepository
) : ViewModel() {

    // Expose SSOT flows from AuthManager
    val currentUser: StateFlow<User?> = auth.currentUser
    val userRole: StateFlow<Role?> = auth.userRole

    val isAuthenticated: StateFlow<Boolean> = currentUser
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // UI-level login state (loading/error)
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    fun signOut() {
        auth.logout()
    }

    fun authenticate(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading

            when (val result = userRepository.authenticate(username, password)) {
                is Resource.Success -> {
                    val user = result.data
                    if (user != null) {
                        auth.login(user) // updates SSOT
                        _loginState.value = AuthState.Idle
                    } else {
                        _loginState.value = AuthState.Error("User not found")
                    }
                }
                is Resource.Error -> {
                    _loginState.value = AuthState.Error(result.message ?: "Unknown error")
                }
                else -> Unit
            }
        }
    }
}
