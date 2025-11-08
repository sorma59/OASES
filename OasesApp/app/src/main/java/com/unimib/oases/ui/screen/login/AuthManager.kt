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
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val userPreferences: UserPreferences
) {

    // SSOT: current user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Derived state: user role
    val userRole: StateFlow<Role?> = currentUser
        .map { it?.role }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        // Load persisted user into flow
        _currentUser.update {
            userPreferences.getUser()
        }
    }

    fun login(user: User) {
        _currentUser.update{ user }
        userPreferences.saveUser(user)
    }

    fun logout() {
        _currentUser.update {
            null
        }
        userPreferences.clear()
    }

    // For synchronous access in non-composables
    fun getCurrentUser(): User? = currentUser.value
    fun getCurrentRole(): Role? = currentUser.value?.role
}
