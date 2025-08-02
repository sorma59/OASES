package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(
    tableName = TableNames.TRIAGE_EVALUATION,
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TriageEvaluationEntity(

    @PrimaryKey
    @ColumnInfo(name = "visit_id")
    val visitId: String,

    @ColumnInfo(name = "red_symptom_ids")
    val redSymptomIds: List<String>,

    @ColumnInfo(name = "yellow_symptom_ids")
    val yellowSymptomIds: List<String>,
)
