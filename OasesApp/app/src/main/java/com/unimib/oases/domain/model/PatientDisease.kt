package com.unimib.oases.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PatientDisease(
    val patientId: String,
    val diseaseName: String,
    val diagnosisDate: String = "",
    val additionalInfo: String = ""
): Parcelable