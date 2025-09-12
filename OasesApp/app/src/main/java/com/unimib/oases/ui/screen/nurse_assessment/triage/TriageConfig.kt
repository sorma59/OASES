package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.symptom.TriageSymptom

data class TriageConfig(
    val redOptions: List<TriageSymptom>,
    val yellowOptions: List<TriageSymptom>
)
