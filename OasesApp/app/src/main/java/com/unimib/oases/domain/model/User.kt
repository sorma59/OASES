package com.unimib.oases.domain.model

import com.unimib.oases.data.local.model.Role

data class User (
    val username: String,
    val password: String,
    val role: Role,
)
