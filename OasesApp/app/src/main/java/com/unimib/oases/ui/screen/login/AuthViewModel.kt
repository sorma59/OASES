package com.unimib.oases.ui.screen.login

import androidx.lifecycle.ViewModel
import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import com.unimib.oases.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private var _authState = MutableStateFlow<AuthState>(AuthState.Uninitialized)
    val authState = _authState.asStateFlow()

    fun createUser(
        username: String,
        password: String,
        role: Role,
    ) {
        userRepository.createAccount(username, password, role)
    }

    fun authenticate(
        username: String,
        password: String,
    ): Boolean {
        if (userRepository.authenticate(username, password)){
            val user = userRepository.getUser()
            _authState.value = user?.let { AuthState.Authenticated(it) }!!
            return true
        }
        else{
            _authState.value = AuthState.Unauthenticated
            return false
        }
    }

    fun getUser(): User? {
        return userRepository.getUser()
    }

    fun deleteAccount(){
        userRepository.deleteAccount()
    }
}