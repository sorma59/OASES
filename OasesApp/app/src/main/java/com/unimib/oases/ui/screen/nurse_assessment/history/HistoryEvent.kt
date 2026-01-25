package com.unimib.oases.ui.screen.nurse_assessment.history

sealed class HistoryEvent {
    data class TabSelected(val tab: HistoryScreenTab): HistoryEvent()
    data object EditButtonPressed: HistoryEvent()
    data object DenyAllClicked: HistoryEvent()
    data class RadioButtonClicked(val disease: String, val isDiagnosed: Boolean): HistoryEvent()
    data class AdditionalInfoChanged(val disease: String, val additionalInfo: String): HistoryEvent()
    data class DateChanged(val disease: String, val date: String): HistoryEvent()
    data object CreateButtonClicked: HistoryEvent()
    data object Save: HistoryEvent()
    data object Cancel: HistoryEvent()

    data object ToastShown: HistoryEvent()
    data object ReloadPastVisits: HistoryEvent()

    data object ReloadPastMedicalHistory: HistoryEvent()
}