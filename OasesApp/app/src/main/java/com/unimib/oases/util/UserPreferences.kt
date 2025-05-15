package com.unimib.oases.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.unimib.oases.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUser(user: User) {
        prefs.edit() { putString("user", gson.toJson(user)) }
    }

    fun getUser(): User? {
        val json = prefs.getString("user", null)
        return json?.let { gson.fromJson(it, User::class.java) }
    }

    fun clear() {
        prefs.edit() { clear() }
    }
}