package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.domain.model.NumericPrecision

@Entity(tableName = TableNames.VITAL_SIGN)
data class VitalSignEntity(
    @PrimaryKey
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "acronym") val acronym: String,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "precision") val precision: NumericPrecision,
)