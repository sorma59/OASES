package com.unimib.oases.ui.screen.medical_visit.evaluation.summary

sealed class EvaluationSummaryEvent {
    data object EditButtonClicked: EvaluationSummaryEvent()
    data object RetryButtonClicked: EvaluationSummaryEvent()
}