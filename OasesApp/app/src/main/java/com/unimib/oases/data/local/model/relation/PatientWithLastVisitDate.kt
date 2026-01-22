package com.unimib.oases.data.local.model.relation

import androidx.room.Embedded
import com.unimib.oases.data.local.model.PatientEntity
import java.time.LocalDate

data class PatientWithLastVisitDateEntity(
    @Embedded
    val patientEntity: PatientEntity,

    val lastVisitDate: LocalDate
)