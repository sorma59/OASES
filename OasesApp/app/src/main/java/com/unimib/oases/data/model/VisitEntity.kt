package com.unimib.oases.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(
    tableName = TableNames.VISIT,
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patient_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class VisitEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("patient_id") val patientId: String,
    @ColumnInfo("triage_code") val triageCode: String,
    @ColumnInfo("date") val date: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("status") val status: String
)