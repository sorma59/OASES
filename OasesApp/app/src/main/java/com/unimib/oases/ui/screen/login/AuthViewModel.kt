package com.unimib.oases.ui.screen.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.local.model.User
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: AuthManager
): ViewModel() {


    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState


    init {
        checkAuthStatus()
    }

    fun currentUser(): User? {
        return if (authState.value is AuthState.Authenticated) {
           (authState.value as AuthState.Authenticated).user
        } else
            null
    }


    private fun checkAuthStatus() {

        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated(User(auth.currentUser!!.username, auth.currentUser!!.pwHash, auth.currentUser!!.role, auth.currentUser!!.salt))
        }
    }



    fun signOut() {
        _authState.value = AuthState.Unauthenticated
        auth.logout()
    }

    fun authenticate(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(2000)
            val result = userRepository.authenticate(username, password)
            if (result is Resource.Success) {
                val user = result.data
                // save the user in the auth object into cache here
                if (user != null) {
                    auth.login(user)
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

//    fun isNurse():Boolean {
//        return authState.value is AuthState.Authenticated && (authState.value as AuthState.Authenticated).user.role == Role.Nurse
//    }
//
//    fun isDoctor(): Boolean {
//        return authState.value is AuthState.Authenticated && (authState.value as AuthState.Authenticated).user.role == Role.Doctor
//    }
//
//    fun isAdmin(): Boolean {
//        return authState.value is AuthState.Authenticated && (authState.value as AuthState.Authenticated).user.role == Role.Admin
//    }
}