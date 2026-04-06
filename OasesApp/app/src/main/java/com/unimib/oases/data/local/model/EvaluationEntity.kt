package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.TherapyText
import kotlinx.serialization.Serializable

@Entity(
    tableName = TableNames.EVALUATION,
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
data class EvaluationEntity(
    @ColumnInfo(name = "visit_id") val visitId: String,
    @ColumnInfo(name = "complaint_id") val complaintId: String,

    // structured answers for editable reconstruction
    @ColumnInfo(name = "tree_answers") val treeAnswers: List<TreeAnswers>,
    @ColumnInfo(name = "detail_question_answers") val detailQuestionAnswers: List<DetailQuestionAnswer>,

    // snapshot for read-only display
    @ColumnInfo(name = "algorithms_questions_and_answers")
    val algorithmsQuestionsAndAnswers: List<List<QuestionAndAnswer>>,

    @ColumnInfo(name = "symptom_ids") val symptomIds: List<String>,

    // suggested snapshot + doctor selection
    @ColumnInfo(name = "suggested_tests") val suggestedTests: List<LabelledTest>,
    @ColumnInfo(name = "labelled_tests") val labelledTests: List<LabelledTest>,
    @ColumnInfo(name = "additional_tests") val additionalTests: String,

    // suggested, shown to doctor, no selection
    @ColumnInfo(name = "immediate_treatments") val immediateTreatments: List<ImmediateTreatment>,
    @ColumnInfo(name = "supportive_therapies") val supportiveTherapies: List<TherapyText>,
)

@Serializable
data class TreeAnswers(
    val treeId: String,
    val path: List<Boolean>
)

@Serializable
data class DetailQuestionAnswer(
    val question: String,
    val answerSymptomIds: List<String>
)