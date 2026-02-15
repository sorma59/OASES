package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import androidx.compose.ui.graphics.Color

data class VitalSignsSummaryState (
    val patientId: String,
    val visitId: String,
    val visitDate: String = "",
    val vitalSigns: List<VitalSignState> = emptyList(),
    val visitVitalSigns: List<VisitVitalSignUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class VitalSignState(
    val name: String,
    val acronym: String,
    val unit: String
)

data class VisitVitalSignUI(
    val name: String,
    val value: String = "",
    val timestamp: String,
    val color: Color? = Color.Transparent
)



