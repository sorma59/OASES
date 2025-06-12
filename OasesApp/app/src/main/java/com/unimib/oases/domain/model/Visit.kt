package com.unimib.oases.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Visit(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val triageCode: String = TriageCode.GREEN.name,
    val date: String = "",
    val description: String = "",
): Parcelable

enum class TriageCode(val code: String)
{
    RED("RED"),
    YELLOW("YELLOW"),
    GREEN("GREEN")
}