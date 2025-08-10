package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "malnutrition_screenings",
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MalnutritionScreeningEntity(
    @PrimaryKey
    @ColumnInfo(name = "visit_id")
    val visitId: String,
    @ColumnInfo(name = "weight_in_kg")
    val weightInKg: Double,
    @ColumnInfo(name = "height_in_cm")
    val heightInCm: Double,
    @ColumnInfo(name = "muac_in_cm")
    val muacInCm: Double,
    @ColumnInfo(name = "muac_category")
    val muacCategory: String,
    val bmi: Double,
)