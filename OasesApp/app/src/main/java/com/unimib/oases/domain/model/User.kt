package com.unimib.oases.domain.model

data class User (
    val username: String,
    val password: String,
    val role: Role,
)

enum class Role(val displayName: String) {
    Doctor("Doctor"),
    Nurse("Nurse"),
    Admin("Admin")
}
