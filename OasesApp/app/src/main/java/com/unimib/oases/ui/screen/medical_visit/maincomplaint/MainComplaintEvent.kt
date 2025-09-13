package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import com.unimib.oases.domain.model.complaint.Option
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.symptom.Symptom

sealed class MainComplaintEvent {

    data class NodeAnswered(val answer: Boolean, val node: ManualNode): MainComplaintEvent()

    data class SymptomSelected(val symptom: Symptom): MainComplaintEvent()

    data class DurationSelected(val duration: Option): MainComplaintEvent()

    data class FrequencySelected(val frequency: Option): MainComplaintEvent()

    data class AspectSelected(val aspect: Option): MainComplaintEvent()

    data object ToastShown: MainComplaintEvent()

    data object RetryButtonClicked: MainComplaintEvent()
}
