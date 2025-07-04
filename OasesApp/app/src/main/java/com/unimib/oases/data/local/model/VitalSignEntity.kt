package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(tableName = TableNames.VITAL_SIGN)
data class VitalSignEntity(
    @PrimaryKey
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "acronym") var acronym: String,
    @ColumnInfo(name = "unit") var unit: String,
)