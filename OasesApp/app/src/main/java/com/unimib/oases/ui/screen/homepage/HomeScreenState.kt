package com.unimib.oases.ui.screen.homepage

import com.unimib.oases.domain.model.Patient

data class HomeScreenState(
    val patients: List<Patient> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null
)