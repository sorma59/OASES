package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import com.unimib.oases.util.datastructure.binarytree.ManualNode

sealed class MainComplaintEvent {

    data class NodeAnswered(val answer: Boolean, val node: ManualNode): MainComplaintEvent()

    data object ToastShown: MainComplaintEvent()
}
