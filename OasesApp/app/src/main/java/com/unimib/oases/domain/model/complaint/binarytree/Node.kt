package com.unimib.oases.domain.model.complaint.binarytree

import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.ImmediateTreatmentQuestionState

fun ManualNode.next(boolean: Boolean): ShowableNode {
    var node = if (boolean) children.left else children.right
    while (true){
        when (node) {
            is AutoNode -> node = if (node.predicate()) node.children.left else node.children.right
            is ShowableNode -> return node
        }
    }
}
sealed interface Node {
    val value: Any?
    val children: Children? // Full tree: 0 or 2 children
}

sealed interface ShowableNode: Node

fun ManualNode.toImmediateTreatmentQuestionState(): ImmediateTreatmentQuestionState{
    return ImmediateTreatmentQuestionState(this)
}

// Internal node
sealed interface InternalNode: Node {
    override val value: String?
    override val children: Children
}

// Internal node that evaluates automatically
data class AutoNode(
    override val value: String? = null,
    override val children: Children,
    val predicate: () -> Boolean
): InternalNode

// Internal node that needs the user input
data class ManualNode(
    override val value: String,
    override val children: Children
) : InternalNode, ShowableNode

// Leaf node: therapy suggested
data class LeafNode(
    override val value: ImmediateTreatment?,
    override val children: Nothing? = null
) : Node, ShowableNode

