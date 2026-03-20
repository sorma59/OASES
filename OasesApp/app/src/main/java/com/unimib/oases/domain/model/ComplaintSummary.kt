package com.unimib.oases.domain.model

import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.domain.model.symptom.Symptom
import kotlinx.serialization.Serializable

data class ComplaintSummary(
    val visitId: String,
    val complaintId: String,
    val algorithmsQuestionsAndAnswers: List<List<QuestionAndAnswer>>,
    val symptoms: Set<Symptom>,
    val tests: Set<LabelledTest>,
    val immediateTreatments: Set<ImmediateTreatment>,
    val supportiveTherapies: Set<TherapyText>,
    val additionalTests: String
)

@Serializable
data class QuestionAndAnswer(
    val question: String,
    val answer: String
)