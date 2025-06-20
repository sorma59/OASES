package com.unimib.oases.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtils {

    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun hashPassword(password: String, salt: String): String {
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), 65536, 128)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = factory.generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(hash)
    }

    fun verifyPassword(inputPassword: String, storedHash: String, storedSalt: String): Boolean {
        val inputHash = hashPassword(inputPassword, storedSalt)
        return inputHash == storedHash
    }

    fun generateShortId(): String {
        val input = "${System.currentTimeMillis()}-${(1000..9999).random()}"
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }.take(7)
    }
}