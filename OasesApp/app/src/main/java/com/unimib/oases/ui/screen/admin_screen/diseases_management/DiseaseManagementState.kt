package com.unimib.oases.ui.screen.admin_screen.diseases_management

import com.unimib.oases.domain.model.Disease


data class DiseaseManagementState(
    val disease: Disease = Disease(
        name = ""
    ),
    val diseases: List<Disease> = emptyList(),
    val isLoading: Boolean = false,
    var error: String? = null,
    val message: String? = null
)

