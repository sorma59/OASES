package com.unimib.oases.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisitVitalSign (
    val visitId: String,
    val vitalSignName: String,
    val timestamp: String = "",
    val value: Double
): Parcelable