package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import com.unimib.oases.domain.model.VisitVitalSign

data class VitalSignsState (
    val patientId: String,
    val vitalSigns: List<PatientVitalSignState> = emptyList(),
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

fun VitalSignsState.toVisitVitalSigns(visitId: String): List<VisitVitalSign> {
    val list = mutableListOf<VisitVitalSign>()
    for (vitalSign in vitalSigns) {
        if (vitalSign.value.isNotBlank()){
            list.add(
                VisitVitalSign(
                    visitId = visitId,
                    vitalSignName = vitalSign.name,
                    timestamp = System.currentTimeMillis().toString(),
                    value = vitalSign.value.toDouble(),
                )
            )
        }
    }
    return list.toList()
}