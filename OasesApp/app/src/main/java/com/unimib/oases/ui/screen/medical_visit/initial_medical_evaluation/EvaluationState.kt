package com.unimib.oases.ui.screen.medical_visit.initial_medical_evaluation

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.ComplaintQuestionWithImmediateTreatment
import com.unimib.oases.domain.model.complaint.Condition
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.SupportiveTherapy
import com.unimib.oases.domain.model.complaint.binarytree.LeafNode
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.Tree
import com.unimib.oases.domain.model.symptom.Symptom

fun TreeSummary.rebranch(
    node: ManualNode,
    answer: Boolean
): TreeSummary {
    val index = this.answers.indexOfFirst { it.node == node }

    if (index == -1) return this // should not happen though

    return this.copy(
        answers = this.answers.take(index) + ImmediateTreatmentQuestionState(node, answer)
    )
}

fun TreeSummary.appendQuestion(
    question: ImmediateTreatmentQuestionState,
): TreeSummary {
    return this.copy(
        answers = this.answers + question
    )
}

data class EvaluationState(
    val patientId: String,
    val visitId: String,
    val complaintId: String,
    val patient: Patient? = null,
    val complaint: Complaint? = null,

    val immediateTreatmentAlgorithmsToShow: Int = 0,
    val immediateTreatmentAlgorithms: List<Tree> = emptyList(),
    val immediateTreatmentQuestions: List<TreeSummary> = emptyList(),
    val leaves: List<LeafNode?> = emptyList(),

    val detailsQuestions: List<ComplaintQuestion> = emptyList(),
    val detailsQuestionsToShow: Int = 0,

    val symptoms: Set<Symptom> = emptySet(),

    val conditions: List<Condition> = emptyList(),

    val requestedTests: Set<LabelledTest> = emptySet(),

    val additionalTestsText: String = "",

    val supportiveTherapies: List<SupportiveTherapy>? = null,

    val wereTestsGenerated: Boolean = false,

    val isLoading: Boolean = false,
    val error: String? = null,
){
    val immediateTreatments : List<ImmediateTreatment?>
        get() = leaves.map { it?.value } +
            detailsQuestions
                .filterIsInstance<ComplaintQuestionWithImmediateTreatment>()
                .filter { it.shouldShowTreatment(symptoms) }
                .map { it.treatment}

    val shouldShowGenerateTestsButton: Boolean
        get() = detailsQuestions.isNotEmpty()
                && detailsQuestionsToShow == detailsQuestions.size
}

data class ImmediateTreatmentQuestionState(
    val node: ManualNode,
    val answer: Boolean? = null
)

data class TreeSummary(
    val treeId: String,
    val answers: List<ImmediateTreatmentQuestionState>
)