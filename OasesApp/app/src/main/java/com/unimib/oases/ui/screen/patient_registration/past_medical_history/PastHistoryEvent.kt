package com.unimib.oases.ui.screen.patient_registration.past_medical_history

sealed class PastHistoryEvent {
    data class CheckChanged(val disease: String): PastHistoryEvent()
    data class AdditionalInfoChanged(val disease: String, val additionalInfo: String): PastHistoryEvent()
    data class DateChanged(val disease: String, val date: String): PastHistoryEvent()

    object Retry: PastHistoryEvent()
}