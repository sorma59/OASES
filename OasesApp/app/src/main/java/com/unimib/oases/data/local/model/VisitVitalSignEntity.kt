package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.unimib.oases.data.local.TableNames

@Entity(
    tableName = TableNames.VISIT_VITAL_SIGN,
    primaryKeys = ["visit_id", "vital_sign_name", "timestamp"],
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visit_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VitalSignEntity::class,
            parentColumns = ["name"],
            childColumns = ["vital_sign_name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VisitVitalSignEntity (
    @ColumnInfo(name = "visit_id") val visitId: String,
    @ColumnInfo(name = "vital_sign_name") val vitalSignName: String,
    @ColumnInfo(name = "timestamp") val timestamp: String,
    @ColumnInfo(name = "value") val value: Double,
)
