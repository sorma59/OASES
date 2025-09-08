package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.util.datastructure.binarytree.ManualNode
import com.unimib.oases.util.datastructure.binarytree.TreatmentPlan
import com.unimib.oases.util.datastructure.binarytree.Tree

data class MainComplaintState(
    val receivedId: String,
    val complaintId: String,
    val tree: Tree? = null,
    val patient: Patient? = null,
    val questions: List<QuestionState> = emptyList(),
    val treatmentPlan: TreatmentPlan? = null,


    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val error: String? = null
)

data class QuestionState(
    val node: ManualNode,
    val answer: Boolean?,
    val callback: ((Boolean) -> Unit)? = null
)