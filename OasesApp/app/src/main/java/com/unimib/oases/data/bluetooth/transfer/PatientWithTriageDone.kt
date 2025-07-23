package com.unimib.oases.data.bluetooth.transfer

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.model.VisitVitalSign
import kotlinx.serialization.Serializable

@Serializable
data class PatientFullData(
    val patientDetails: Patient,
    val visit: Visit,
    val patientDiseases: List<PatientDisease>,
    val vitalSigns: List<VisitVitalSign>,
    val triageEvaluation: TriageEvaluation,
)