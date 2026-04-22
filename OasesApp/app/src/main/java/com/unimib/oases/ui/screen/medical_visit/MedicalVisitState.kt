package com.unimib.oases.ui.screen.medical_visit

import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.model.Reassessment

data class MedicalVisitState(
    val patientId: String,
    val visitId: String,
    val evaluations: Map<String, Evaluation> = emptyMap(),
    val reassessments: Map<String, Reassessment> = emptyMap(),

    val isTriageMissing: Boolean = false,

    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val isDispositionUnlocked: Boolean
        get() = reassessments.isNotEmpty()
}