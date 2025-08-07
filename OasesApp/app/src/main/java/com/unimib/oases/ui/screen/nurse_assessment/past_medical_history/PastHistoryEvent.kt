package com.unimib.oases.ui.screen.nurse_assessment.past_medical_history

sealed class PastHistoryEvent {

    data class RadioButtonClicked(val disease: String, val isDiagnosed: Boolean): PastHistoryEvent()
    data class AdditionalInfoChanged(val disease: String, val additionalInfo: String): PastHistoryEvent()
    data class DateChanged(val disease: String, val date: String): PastHistoryEvent()

    object NurseClicked: PastHistoryEvent()
    object ToastShown: PastHistoryEvent()
    object Retry: PastHistoryEvent()
}