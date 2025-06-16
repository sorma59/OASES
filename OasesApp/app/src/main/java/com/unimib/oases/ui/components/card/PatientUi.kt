package com.unimib.oases.ui.components.card

import com.unimib.oases.domain.model.Patient

data class PatientUi(
    val isOptionsRevealed: Boolean,
    val item: Patient,
)
