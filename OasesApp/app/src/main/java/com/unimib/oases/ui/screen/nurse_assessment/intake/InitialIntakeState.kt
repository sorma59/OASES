package com.unimib.oases.ui.screen.nurse_assessment.intake

import com.unimib.oases.domain.model.Patient

data class InitialIntakeState(
    val isReturn: Boolean = false,
    val patients: List<Patient> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null
)