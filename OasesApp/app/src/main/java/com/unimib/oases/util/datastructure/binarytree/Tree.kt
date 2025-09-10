package com.unimib.oases.util.datastructure.binarytree

data class Branch(
    val nodes: List<ShowableNode> = emptyList()
)

sealed interface Tree {
    val root: ManualNode
}