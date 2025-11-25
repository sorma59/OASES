package com.unimib.oases.data.local.model.relation

import androidx.room.Embedded
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.VisitEntity

data class PatientWithVisitInfoEntity(
    // Prefixes needed to distinguish between patient's id and visit's
    @Embedded(prefix = "patient_")
    val patientEntity: PatientEntity,

    @Embedded(prefix = "visit_")
    val visitEntity: VisitEntity
)