package com.unimib.oases.ui.screen.admin_screen.vital_signs_management

import com.unimib.oases.domain.model.VitalSign


data class VitalSignManagementState(
    val vitalSign: VitalSign = VitalSign(
        name = ""
    ),
    val vitalSigns: List<VitalSign> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null,
    val nameError: String? = null,
    val acronymError: String? = null,
    val unitError: String? = null
)

