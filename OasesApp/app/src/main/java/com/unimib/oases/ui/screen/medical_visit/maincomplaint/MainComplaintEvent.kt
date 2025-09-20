package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import com.unimib.oases.domain.model.complaint.Test
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.symptom.Symptom

sealed class MainComplaintEvent {

    data class NodeAnswered(val answer: Boolean, val node: ManualNode): MainComplaintEvent()

    data class SymptomSelected(val symptom: Symptom): MainComplaintEvent()

    data class TestSelected(val test: Test): MainComplaintEvent()

    data object RetryButtonClicked: MainComplaintEvent()

    data object GenerateTestsPressed: MainComplaintEvent()

    data object ToastShown: MainComplaintEvent()
}
