package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.SupportiveTherapyText

@Entity(
    tableName = TableNames.COMPLAINT_SUMMARY,
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
data class ComplaintSummaryEntity(
    @ColumnInfo(name = "visit_id") val visitId: String,
    @ColumnInfo(name = "complaint_id") val complaintId: String,
    @ColumnInfo(name = "algorithms_questions_and_answers")
    val algorithmsQuestionsAndAnswers: List<List<QuestionAndAnswer>>,
    val symptoms: List<String>,
    @ColumnInfo(name = "labelled_tests") val labelledTests: List<LabelledTest>,
    @ColumnInfo(name = "immediate_treatments") val immediateTreatments: List<ImmediateTreatment>,
    @ColumnInfo(name = "supportive_therapies") val supportiveTherapies: List<SupportiveTherapyText>,
    @ColumnInfo(name = "additional_tests") val additionalTests: String
)