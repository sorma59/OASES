package com.unimib.oases.domain.model

import com.unimib.oases.data.local.model.DetailQuestionAnswer
import com.unimib.oases.data.local.model.TreeAnswers
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.domain.model.symptom.Symptom
import kotlinx.serialization.Serializable

data class Evaluation(
    val visitId: String,
    val complaintId: ComplaintId,
    val symptoms: Set<Symptom>,

    val algorithmsQuestionsAndAnswers: List<List<QuestionAndAnswer>>,

    val suggestedTests: Set<LabelledTest>,  // snapshot of what was suggested
    val requestedTests: Set<LabelledTest>,  // what the doctor actually picked
    val additionalTestsText: String,

    val immediateTreatments: Set<ImmediateTreatment>,  // suggested & shown, no selection
    val supportiveTherapies: Set<TherapyText>,  // suggested & shown, no selection

    // new structured answers for editable reconstruction
    val treeAnswers: List<TreeAnswers>,
    val detailQuestionAnswers: List<DetailQuestionAnswer>,
)

@Serializable
data class QuestionAndAnswer(
    val question: String,
    val answer: String
)