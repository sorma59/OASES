package com.unimib.oases.domain.model

data class PatientFullData(
    val patientDetails: Patient,
    val visit: Visit?,
    val patientDiseases: List<PatientDisease>,
    val vitalSigns: List<VisitVitalSign>,
    val triageEvaluation: TriageEvaluation?,
    val malnutritionScreening: MalnutritionScreening?,
    val complaintsSummaries: List<ComplaintSummary>
)