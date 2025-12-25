package com.unimib.oases.domain.model

import androidx.compose.ui.graphics.Color
import com.unimib.oases.util.DateAndTimeUtils
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Visit(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val triageCode: TriageCode = TriageCode.NONE,
    val patientStatus: PatientStatus = PatientStatus.WAITING_FOR_TRIAGE,
    val roomName: String? = null,
    val arrivalTime: LocalTime = DateAndTimeUtils.getCurrentTime(),
    val date: LocalDate = DateAndTimeUtils.getCurrentDate(),
    val description: String = ""
)

enum class TriageCode{
    RED,
    YELLOW,
    GREEN,
    NONE;
    fun getColor() = when(this) {
        RED -> Color.Red
        YELLOW -> Color.Yellow
        GREEN -> Color.Green
        NONE -> Color.Gray
    }
}