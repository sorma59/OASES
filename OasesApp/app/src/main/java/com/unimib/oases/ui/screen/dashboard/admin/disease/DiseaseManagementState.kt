package com.unimib.oases.ui.screen.dashboard.admin.disease

import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.SexSpecificity


data class DiseaseManagementState(
    val disease: Disease = Disease(
        name = "",
        sexSpecificity = SexSpecificity.ALL,
        ageSpecificity = AgeSpecificity.ALL
    ),
    val diseases: List<Disease> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

