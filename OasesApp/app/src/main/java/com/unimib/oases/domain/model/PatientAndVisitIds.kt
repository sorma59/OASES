package com.unimib.oases.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PatientAndVisitIds(
    val patientId: String,
    val visitId: String
): Parcelable