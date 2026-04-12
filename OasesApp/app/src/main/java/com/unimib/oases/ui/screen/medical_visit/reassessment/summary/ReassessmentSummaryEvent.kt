package com.unimib.oases.ui.screen.medical_visit.reassessment.summary

sealed class ReassessmentSummaryEvent {
    data object EditButtonClicked: ReassessmentSummaryEvent()
    data object RetryButtonClicked: ReassessmentSummaryEvent()
}