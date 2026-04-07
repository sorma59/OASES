package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.domain.model.Ward
import com.unimib.oases.ui.screen.medical_visit.disposition.HomeTreatment

@Entity(
    tableName = TableNames.DISPOSITION,
    primaryKeys = ["visit_id"],
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DispositionEntity(
    @ColumnInfo(name = "visit_id") val visitId: String,
    @ColumnInfo(name = "disposition_type_label") val dispositionTypeLabel: String,
    @ColumnInfo(name = "ward") val ward: Ward?,
    @ColumnInfo(name = "home_treatments") val homeTreatments: List<HomeTreatment>,
    @ColumnInfo(name = "prescribed_therapies_text") val prescribedTherapiesText: String,
    @ColumnInfo(name = "final_diagnosis_text") val finalDiagnosisText: String,
)