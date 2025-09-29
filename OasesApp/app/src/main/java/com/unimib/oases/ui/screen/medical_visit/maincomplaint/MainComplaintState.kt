package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import com.unimib.oases.domain.model.ComplaintSummary
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

fun List<ImmediateTreatmentQuestionState>.rebranch(node: ManualNode, answer: Boolean)
: List<ImmediateTreatmentQuestionState> {
    var list = this.toMutableList()
    while(list.last().node != node){
        list.removeAt(list.lastIndex)
    }
    list.removeAt(list.lastIndex)
    list.add(ImmediateTreatmentQuestionState(node, answer))
    return list.toList()
}

fun MainComplaintState.toComplaintSummary(): ComplaintSummary{
    check(this.immediateTreatments.all { it != null })
    check(this.supportiveTherapies != null)
    return ComplaintSummary(
        complaintId = this.complaintId,
        symptoms = this.symptoms,
        tests = this.requestedTests,
        immediateTreatments = this.immediateTreatments.toSet() as Set<ImmediateTreatment>,
        supportiveTherapies = this.supportiveTherapies.map { it.therapy }.toSet(),
        additionalTests = this.additionalTestsText
    )
}

data class MainComplaintState(
    val patientId: String,
    val complaintId: String,
    val patient: Patient? = null,
    val complaint: Complaint? = null,

    val immediateTreatmentAlgorithmsToShow: Int = 0,
    val immediateTreatmentAlgorithms: List<Tree> = emptyList(),
    val immediateTreatmentQuestions: List<List<ImmediateTreatmentQuestionState>> = emptyList(),
    val leaves: List<LeafNode?> = emptyList(),

    val detailsQuestions: List<ComplaintQuestion> = emptyList(),
    val detailsQuestionsToShow: Int = 0,

    val symptoms: Set<Symptom> = emptySet(),

    val conditions: List<Condition> = emptyList(),

    val requestedTests: Set<LabelledTest> = emptySet(),

    val additionalTestsText: String = "",

    val supportiveTherapies: List<SupportiveTherapy>? = null,

    val shouldShowSubmitButton: Boolean = false,

    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val error: String? = null
){
    val immediateTreatments : List<ImmediateTreatment?>
        get() = leaves.map { it?.value } +
            detailsQuestions
                .filterIsInstance<ComplaintQuestionWithImmediateTreatment>()
                .filter { it.shouldShowTreatment(symptoms) }
                .map { it.treatment}
}

data class ImmediateTreatmentQuestionState(
    val node: ManualNode,
    val answer: Boolean? = null
)