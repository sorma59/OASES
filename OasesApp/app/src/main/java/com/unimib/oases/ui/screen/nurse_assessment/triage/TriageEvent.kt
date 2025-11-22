package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.Room

sealed class TriageEvent {

    data class FieldToggled(val fieldId: String) : TriageEvent()

    data class RoomClicked(val room: Room): TriageEvent()

    data object EditButtonPressed: TriageEvent()

    data object CreateButtonPressed: TriageEvent()

    data object Retry: TriageEvent()
    data object ToastShown: TriageEvent()

    data object NextButtonPressed: TriageEvent()
    data object BackButtonPressed: TriageEvent()

    data object ConfirmDialog: TriageEvent()
    data object DismissDialog: TriageEvent()
}