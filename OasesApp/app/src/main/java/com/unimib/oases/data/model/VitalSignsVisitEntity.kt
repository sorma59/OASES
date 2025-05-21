package com.unimib.oases.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "visit_vitalSigns",
    primaryKeys = ["visit_id", "vitalSigns_name", "timestamp"],
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visit_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VitalSignsEntity::class,
            parentColumns = ["name"],
            childColumns = ["vitalSigns_name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VitalSignsVisitEntity (
    @ColumnInfo(name = "visit_id") var visitId: String,
    @ColumnInfo(name = "vitalSigns_name") var vitalSingsName: String,
    @ColumnInfo(name = "timestamp") var timestamp: String,
    @ColumnInfo(name = "value") var value: Double,
)
