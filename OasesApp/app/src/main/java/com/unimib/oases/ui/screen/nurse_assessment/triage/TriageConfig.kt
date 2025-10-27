package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.symptom.TriageSymptom

data class TriageConfig(
    val redOptions: List<SymptomWithLabel>,
    val yellowOptions: List<SymptomWithLabel>
)

data class SymptomWithLabel(
    val symptom: TriageSymptom,
    val label: String
){
    val id: String
        get() = symptom.id
}
