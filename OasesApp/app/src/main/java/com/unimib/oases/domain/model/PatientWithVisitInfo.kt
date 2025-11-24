package com.unimib.oases.domain.model

import java.time.LocalTime

data class PatientWithVisitInfo(
    val patient: Patient,

    // Latest Visit Details
    val status: PatientStatus,
    val code: TriageCode,
    val room: String?,
    val arrivalTime: LocalTime
)