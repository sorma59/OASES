package com.unimib.oases.ui.screen.medical_visit

import com.unimib.oases.domain.model.symptom.Symptom

data class MedicalVisitState(
    val patientId: String,
    val symptoms: Set<Symptom> = emptySet()
)