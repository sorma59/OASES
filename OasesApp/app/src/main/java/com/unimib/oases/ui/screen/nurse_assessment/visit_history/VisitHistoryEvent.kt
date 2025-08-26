package com.unimib.oases.ui.screen.nurse_assessment.visit_history

sealed class VisitHistoryEvent {
    data object Retry : VisitHistoryEvent()
}