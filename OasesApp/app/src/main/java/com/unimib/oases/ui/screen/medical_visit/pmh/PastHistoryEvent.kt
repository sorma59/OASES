package com.unimib.oases.ui.screen.medical_visit.pmh

sealed class PastHistoryEvent {

    data class RadioButtonClicked(val disease: String, val isDiagnosed: Boolean): PastHistoryEvent()
    data class AdditionalInfoChanged(val disease: String, val additionalInfo: String): PastHistoryEvent()
    data class DateChanged(val disease: String, val date: String): PastHistoryEvent()
    data object Save: PastHistoryEvent()
    data object Cancel: PastHistoryEvent()

    data object NurseClicked: PastHistoryEvent()
    data object ToastShown: PastHistoryEvent()
    data object Retry: PastHistoryEvent()
}