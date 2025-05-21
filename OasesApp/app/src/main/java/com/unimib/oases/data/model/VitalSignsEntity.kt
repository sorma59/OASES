package com.unimib.oases.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vitalSings")
data class VitalSignsEntity(
    @PrimaryKey
    @ColumnInfo(name = "name") var name: String,
)