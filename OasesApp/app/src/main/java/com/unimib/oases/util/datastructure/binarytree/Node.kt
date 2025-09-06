package com.unimib.oases.util.datastructure.binarytree

interface Node {
    val value: Any?
    val children: Children? // Full tree: 0 or 2 children
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
    override val children: Children,
) : InternalNode

// Leaf node: therapy suggested
data class LeafNode(
    override val value: TreatmentPlan?,
    override val children: Nothing? = null
) : Node