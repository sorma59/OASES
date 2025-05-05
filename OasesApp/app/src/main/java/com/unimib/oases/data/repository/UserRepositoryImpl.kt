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
            "account_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val gson = Gson()

    companion object {
        const val ACCOUNT_KEY = "account_key"
    }

    override fun createAccount(username: String, password: String, role: Role) {
        // Generate a unique salt for each PIN
        val salt = BCrypt.gensalt()
        // Hash the PIN with the salt
        val pinHash = BCrypt.hashpw(password, salt)

        val account = User(username, role, pinHash, salt)
        val accountJson = gson.toJson(account)

        sharedPreferences.edit() { putString(ACCOUNT_KEY, accountJson) }
    }

    override fun authenticate(username: String, password: String): Boolean {
        val accountJson = sharedPreferences.getString(ACCOUNT_KEY, null)
            ?: return false

        val account = gson.fromJson(accountJson, User::class.java)
        // Check if the provided PIN matches the stored hash
        return BCrypt.checkpw(password, account.pwHash)
    }

    override fun getUser(): User? {
        val accountJson = sharedPreferences.getString(ACCOUNT_KEY, null) ?: return null
        return gson.fromJson(accountJson, User::class.java)
    }

    override fun deleteAccount(){
        sharedPreferences.edit() { remove(ACCOUNT_KEY) }
    }
}