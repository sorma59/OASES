package com.unimib.oases.ui.screen.admin_screen.vital_signs_management

import com.unimib.oases.domain.model.VitalSign


data class VitalSignManagementState(
    val vitalSign: VitalSign = VitalSign(
        name = ""
    ),
    val vitalSigns: List<VitalSign> = emptyList(),
    val isLoading: Boolean = false,
    var error: String? = null,
    val message: String? = null
)

