package com.unimib.oases.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.ui.screen.login.AuthState.Authenticated
import com.unimib.oases.ui.screen.login.AuthState.Error
import com.unimib.oases.ui.screen.login.AuthState.Loading
import com.unimib.oases.ui.screen.login.AuthState.Unauthenticated
import com.unimib.oases.ui.screen.login.AuthState.Uninitialized
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: AuthManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(Uninitialized)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val isAuthenticated: Boolean
        get() = authState.value is Authenticated

    val currentUser: User?
        get() = (authState.value as? Authenticated)?.user

    val userRole: Role?
        get () = currentUser?.role

    init {
        checkAuthStatus()
    }

    private fun setAuthState(state: AuthState) {
        _authState.value = state
    }

    private fun checkAuthStatus() {
        val user = auth.currentUser.value
        if (user != null) {
            setAuthState(Authenticated(user))
        } else {
            setAuthState(Unauthenticated)
        }
    }

    fun signOut() {
        auth.logout()
        setAuthState(Unauthenticated)
    }

    fun authenticate(username: String, password: String) {
        viewModelScope.launch {
            setAuthState(Loading)

//            delay(2000)

            when (val result = userRepository.authenticate(username, password)) {
                is Resource.Success -> {
                    val user = result.data
                    if (user != null) {
                        auth.login(user)
                        setAuthState(Authenticated(user))
                    } else {
                        setAuthState(Error("User not found"))
                    }
                }
                is Resource.Error -> {
                    val message = result.message ?: "Unknown error"
                    setAuthState(Error(message))
                }

                is Resource.Loading<*> -> Unit
                is Resource.None<*> -> Unit
            }
        }
    }
}
