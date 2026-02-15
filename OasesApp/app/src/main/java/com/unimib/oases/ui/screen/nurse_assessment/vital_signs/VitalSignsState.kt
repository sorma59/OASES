package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import androidx.compose.ui.graphics.Color
import com.unimib.oases.domain.model.VisitVitalSign


data class VitalSignsState (
    val patientId: String,
    val visitId: String,
    val visitDate: String = "",
    val vitalSigns: List<PatientVitalSignState> = emptyList(),
    val visitVitalSigns: List<VisitVitalSignUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PatientVitalSignState(
    val name: String,
    val acronym: String,
    val unit: String,
    val value: String = "",
    val error: String? = null
)


data class VisitVitalSignUI(
    val name: String,
    val value: String = "",
    val timestamp: String,
    val color: Color? = Color.Transparent
)


fun VitalSignsState.toVisitVitalSigns(visitId: String): List<VisitVitalSign> {
    val list = mutableListOf<VisitVitalSign>()
    val currentTime = System.currentTimeMillis().toString()
    for (vitalSign in vitalSigns) {
        if (vitalSign.value.isNotBlank()){
            list.add(
                VisitVitalSign(
                    visitId = visitId,
                    vitalSignName = vitalSign.name,
                    timestamp = currentTime,
                    value = vitalSign.value.toDouble(),
                )
            )
        }
    }
    return list.toList()
}



