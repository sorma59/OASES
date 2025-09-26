package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.Condition
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.SupportiveTherapy
import com.unimib.oases.domain.model.complaint.Test
import com.unimib.oases.domain.model.complaint.binarytree.Branch
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

/**
 * Converts the current state of the main complaint to a [Branch].
 *
 * This function constructs a [Branch] by combining the nodes from the current list of questions
 * with the leaf node of the complaint. It assumes that the `leaf` node is not null.
 *
 * @return A [Branch] representing the current path of questions and the final leaf node.
 * @throws IllegalStateException if the `leaf` node is null.
 */
//fun MainComplaintState.toBranch(): Branch{
//    check(this.leaf != null) {("Leaf node is null")}
//    return Branch(
//        nodes = this.immediateTreatmentQuestions.map { it.node } + listOf(this.leaf)
//    )
//}

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

    val requestedTests: Set<Test> = emptySet(),

    val additionalTestsText: String = "",

    val supportiveTherapies: List<SupportiveTherapy>? = null,

    val shouldShowSubmitButton: Boolean = false,

    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val error: String? = null
){
    val immediateTreatments : List<ImmediateTreatment?>
        get() = leaves.map { it?.value }
}

data class ImmediateTreatmentQuestionState(
    val node: ManualNode,
    val answer: Boolean? = null
)