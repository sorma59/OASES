package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.VisitVitalSign

sealed class TriageEvent {
    data class FieldToggled(val fieldId: String) : TriageEvent()
    data class RoomClicked(val room: Room): TriageEvent()
    data object EditButtonPressed: TriageEvent()
    data object CreateButtonPressed: TriageEvent()
    data object Retry: TriageEvent()
    data class NextButtonPressed(val vitalSigns: List<VisitVitalSign>? = null): TriageEvent()
    data object ReattemptSaving: TriageEvent()
    data object BackButtonPressed: TriageEvent()
    data class ConfirmDialog(val vitalSigns: List<VisitVitalSign>): TriageEvent()
    data object DismissDialog: TriageEvent()
    data class RetrySaving(val vitalSigns: List<VisitVitalSign>): TriageEvent()
}