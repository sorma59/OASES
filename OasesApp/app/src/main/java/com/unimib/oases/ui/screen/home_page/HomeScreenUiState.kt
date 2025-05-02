package com.unimib.oases.ui.screen.home_page

import com.unimib.oases.data.model.Patient

data class HomeScreenUiState(
    val patients: List<Patient> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedPatient: Patient? = null,
    val isRefreshing: Boolean = false,
    // Add more screen-specific UI flags as needed
)