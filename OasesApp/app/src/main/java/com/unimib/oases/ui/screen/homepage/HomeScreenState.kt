package com.unimib.oases.ui.screen.homepage

import com.unimib.oases.domain.model.Patient

data class HomeScreenState(
    val receivedPatients: List<Patient> = emptyList(),
    val patients: List<Patient> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Add more screen-specific UI flags as needed
)