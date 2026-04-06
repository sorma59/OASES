package com.unimib.oases.ui.screen.medical_visit.reassessment

import com.unimib.oases.domain.model.complaint.Finding

sealed class ReassessmentEvent {
    data class FindingSelected(val finding: Finding): ReassessmentEvent()
    data object GenerateDefinitiveTherapiesClicked: ReassessmentEvent()
    data object SubmitClicked: ReassessmentEvent()
    data object ReattemptSaving: ReassessmentEvent()
    data object RetryButtonClicked: ReassessmentEvent()
}