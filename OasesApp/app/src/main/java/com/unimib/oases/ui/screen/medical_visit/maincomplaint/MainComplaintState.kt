package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.util.datastructure.binarytree.Branch
import com.unimib.oases.util.datastructure.binarytree.LeafNode
import com.unimib.oases.util.datastructure.binarytree.ManualNode
import com.unimib.oases.util.datastructure.binarytree.TreatmentPlan
import com.unimib.oases.util.datastructure.binarytree.Tree

fun List<QuestionState>.rebranch(node: ManualNode, answer: Boolean): List<QuestionState> {
    var list = this.toMutableList()
    while(list.last().node != node){
        list.removeAt(list.lastIndex)
    }
    list.removeAt(list.lastIndex)
    list.add(QuestionState(node, answer))
    return list.toList()
}

/**
 * Converts the current state of the main complaint to a [Branch].
 *
 * This function constructs a [Branch] by combining the nodes from the current list of questions
 * with the leaf node of the complaint. It assumes that the `leaf` node is not null.
 *
 * @return A [Branch] representing the current path of questions and the final leaf node.
 * @throws Exception if the `leaf` node is null.
 */
fun MainComplaintState.toBranch(): Branch{
    if (this.leaf == null) throw Exception("Leaf node is null")
    return Branch(
        nodes = this.questions.map { it.node } + listOf(this.leaf)
    )
}

data class MainComplaintState(
    val receivedId: String,
    val complaintId: String,
    val tree: Tree? = null,
    val patient: Patient? = null,
    val questions: List<QuestionState> = emptyList(),
    val leaf: LeafNode? = null,

    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val error: String? = null
){
    val treatmentPlan : TreatmentPlan?
        get() = leaf?.value
}

data class QuestionState(
    val node: ManualNode,
    val answer: Boolean? = null
)