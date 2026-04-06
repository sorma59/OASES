package com.unimib.oases.domain.model.complaint.binarytree

data class Branch(
    val nodes: List<ShowableNode> = emptyList()
)

sealed interface Tree {
    val id: TreeId
    val root: ManualNode
}

enum class TreeId(val value: String) {
    DIARRHEA("diarrhea"),
    AIRWAY_OBSTRUCTION("airway_obstruction"),
    SEVERE_RESPIRATORY_DISTRESS("severe_respiratory_distress"),
    WHEEZING("wheezing"),
    SEIZURES("seizures"),
}