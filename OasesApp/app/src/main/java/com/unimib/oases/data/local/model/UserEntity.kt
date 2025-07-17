package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(tableName = TableNames.USER)
data class User(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "pw_hash") val pwHash: String,
    @ColumnInfo(name = "role") val role: Role = Role.NURSE,
    @ColumnInfo(name = "salt") val salt: String,
)

enum class Role(val displayName: String) {
    DOCTOR("Doctor"),
    NURSE("Nurse"),
    ADMIN("Admin")
}
