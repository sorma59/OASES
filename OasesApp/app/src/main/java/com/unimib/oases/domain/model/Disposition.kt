package com.unimib.oases.domain.model

import com.unimib.oases.ui.screen.medical_visit.disposition.HomeTreatment


data class Disposition(
    val visitId: String,
    val dispositionType: DispositionType,
    val dispositionTypeLabel: String,
    val homeTreatments: Set<HomeTreatment>,
    val prescribedTherapiesText: String,
    val finalDiagnosisText: String,
)

sealed class DispositionType {
    data object Discharge: DispositionType()
    data class Hospitalization(val ward: Ward): DispositionType()
}

enum class Ward(val label: String) {
    MEDICAL("Medical ward"),
    SURGICAL("Surgical ward"),
    CHILDREN("Children ward"),
    MATERNITY("Maternity ward"),
    HIGH_DEPENDENCY_UNIT("High-dependency unit"),
}