package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.domain.model.complaint.TherapyText
import kotlinx.serialization.Serializable

@Entity(
    tableName = TableNames.REASSESSMENT,
    primaryKeys = ["visit_id", "complaint_id"],
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReassessmentEntity(
    @ColumnInfo(name = "visit_id") val visitId: String,
    @ColumnInfo(name = "complaint_id") val complaintId: String,
    @ColumnInfo(name = "symptom_ids") val symptomIds: List<String>,
    @ColumnInfo(name = "findings") val findings: List<FindingSnapshot>,
    @ColumnInfo(name = "definitive_therapies") val definitiveTherapies: List<TherapyText>,
)

@Serializable
data class FindingSnapshot(
    val id: String,
    val description: String
)
