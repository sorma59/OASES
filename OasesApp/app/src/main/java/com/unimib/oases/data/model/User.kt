package com.unimib.oases.data.model

class User (
    val username: String,
    val role: Role,
    val pwHash: String,
    val salt: String
)

enum class Role {
    Doctor(),
    Nurse()
}
