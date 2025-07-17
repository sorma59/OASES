package com.unimib.oases.ui.screen.patient_registration.visit_history

sealed class VisitHistoryEvent {
    object Retry : VisitHistoryEvent()
}