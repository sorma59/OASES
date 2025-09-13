package com.unimib.oases.domain.model.complaint.binarytree

data class Branch(
    val nodes: List<ShowableNode> = emptyList()
)

sealed interface Tree {
    val root: ManualNode
}