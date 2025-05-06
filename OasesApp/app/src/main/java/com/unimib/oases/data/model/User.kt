package com.unimib.oases.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "pw_hash") val pwHash: String,
    @ColumnInfo(name = "role") val role: Role = Role.Nurse,
    @ColumnInfo(name = "salt") val salt: String,
)

enum class Role(val displayName: String) {
    Doctor("Doctor"),
    Nurse("Nurse")
}
