package com.unimib.oases.domain.model

data class PatientFullData(
    val patientDetails: Patient,
    val visit: Visit,
    val patientDiseases: List<PatientDisease> = emptyList(),
    val vitalSigns: List<VisitVitalSign> = emptyList(),
    val triageEvaluation: TriageEvaluation? = null,
    val malnutritionScreening: MalnutritionScreening? = null,
    val evaluations: List<Evaluation> = emptyList(),
    val reassessments: List<Reassessment> = emptyList(),
)