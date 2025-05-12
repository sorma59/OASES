package com.unimib.oases.ui.screen.login

import android.content.Context
import com.unimib.oases.data.model.User
import com.unimib.oases.util.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences
) {
    var currentUser: User? = null
        private set

    init {
        currentUser = userPreferences.getUser()
    }

    fun login(user: User) {
        currentUser = user
        userPreferences.saveUser(user)
    }

    fun logout() {
        currentUser = null
        userPreferences.clear()
    }
}
