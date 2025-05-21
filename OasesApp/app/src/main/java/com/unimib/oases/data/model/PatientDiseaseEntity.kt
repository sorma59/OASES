package com.unimib.oases.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "patient_diseases",
    primaryKeys = ["patient_id", "disease_name"],
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patient_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = DiseaseEntity::class,
            parentColumns = ["name"],
            childColumns = ["disease_name"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class PatientDiseaseEntity (
    @ColumnInfo(name = "patient_id") var patientId: String,
    @ColumnInfo(name = "disease_name") var diseaseName: String,
    @ColumnInfo(name = "diagnosis_date") var diagnosisDate: String,
    @ColumnInfo(name = "additional_info") var additionalInfo: String,
)