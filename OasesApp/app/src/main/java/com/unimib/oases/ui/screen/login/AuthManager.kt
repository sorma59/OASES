package com.unimib.oases.ui.screen.login

import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import com.unimib.oases.util.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val userPreferences: UserPreferences
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    val userRole: StateFlow<Role?> = _currentUser
        .map { it?.role }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        // Load persisted user into flow
        _currentUser.value = userPreferences.getUser()
    }

    fun login(user: User) {
        _currentUser.value = user
        userPreferences.saveUser(user)
    }

    fun logout() {
        _currentUser.value = null
        userPreferences.clear()
    }
}

