package com.unimib.oases.ui.screen.medical_visit.evaluation.summary

import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.ComplaintQuestionWithImmediateTreatment
import com.unimib.oases.domain.model.complaint.Condition
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.domain.model.complaint.binarytree.LeafNode
import com.unimib.oases.domain.model.complaint.binarytree.Tree
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.screen.medical_visit.evaluation.TreeSummary

data class EvaluationSummaryState(
    val patientId: String,
    val visitId: String,
    val complaintId: String,
    val complaint: Complaint? = null,

    val immediateTreatmentAlgorithms: List<Tree> = emptyList(),
    val immediateTreatmentQuestions: List<TreeSummary> = emptyList(),
    val leaves: List<LeafNode?> = emptyList(),

    val detailsQuestions: List<ComplaintQuestion> = emptyList(),

    val symptoms: Set<Symptom> = emptySet(),

    val conditions: List<Condition> = emptyList(),

    val requestedTests: Set<LabelledTest> = emptySet(),

    val additionalTestsText: String = "",

    val supportiveTherapies: List<TherapyText>? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
){
    val immediateTreatments : List<ImmediateTreatment>
        get() = leaves.mapNotNull { it?.value } +
                detailsQuestions
                    .filterIsInstance<ComplaintQuestionWithImmediateTreatment>()
                    .filter { it.shouldShowTreatment(symptoms) }
                    .map { it.treatment}

}