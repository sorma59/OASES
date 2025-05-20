package com.unimib.oases.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "visits",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
        )
    ]
)
data class VisitEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val triageCode: String,
    val date: String,
    val description: String,
)