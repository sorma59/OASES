package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.unimib.oases.data.local.TableNames

@Entity(
    tableName = TableNames.PATIENT_DISEASE,
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
    @ColumnInfo(name = "patient_id") val patientId: String,
    @ColumnInfo(name = "disease_name") val diseaseName: String,
    @ColumnInfo(name = "is_diagnosed") val isDiagnosed: Boolean?,
    @ColumnInfo(name = "diagnosis_date") val diagnosisDate: String,
    @ColumnInfo(name = "additional_info") val additionalInfo: String,
    @ColumnInfo(name = "free_text_value") val freeTextValue: String
)