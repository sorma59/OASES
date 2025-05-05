package com.unimib.oases.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import com.unimib.oases.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): UserRepository {

    private val sharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "user_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val gson = Gson()

    companion object {
        fun getUserKey(username: String) = "user_$username"
    }

    override fun createUser(username: String, password: String, role: Role) {
        // Generate a unique salt for each password
        val salt = BCrypt.gensalt()
        // Hash the password with the salt
        val pwHash = BCrypt.hashpw(password, salt)

        val user = User(username, role, pwHash)
        val userJson = gson.toJson(user)
        val userKey = getUserKey(username)
        sharedPreferences.edit() { putString(userKey, userJson) }
    }

    override fun authenticate(username: String, password: String): Boolean {
        val userKey = getUserKey(username)
        val userJson = sharedPreferences.getString(userKey, null)
            ?: return false

        val user = gson.fromJson(userJson, User::class.java)
        // Check if the provided PIN matches the stored hash
        return BCrypt.checkpw(password, user.pwHash)
    }

    override fun getUser(username: String): User? {
        val accountKey = getUserKey(username)
        val accountJson = sharedPreferences.getString(accountKey, null) ?: return null
        return gson.fromJson(accountJson, User::class.java)
    }

    override fun deleteUser(username: String) {
        val accountKey = getUserKey(username)
        sharedPreferences.edit() { remove(accountKey) }
    }
}