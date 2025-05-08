package com.unimib.oases.ui.screen.homepage

import com.unimib.oases.data.model.PatientEntity

data class HomeScreenUiState(
    val patients: List<PatientEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedPatient: PatientEntity? = null,
    val isRefreshing: Boolean = false,
    // Add more screen-specific UI flags as needed
)