package com.unimib.oases.data.local.model.relation

import androidx.room.Embedded
import com.unimib.oases.data.local.model.PatientEntity

data class PatientWithVisitInfoEntity(
    // Patient Details
    @Embedded
    val patientEntity: PatientEntity,

    // Latest Visit Details
    val status: String,
    val code: String,
    val room: String?,
    val arrivalTime: String
)