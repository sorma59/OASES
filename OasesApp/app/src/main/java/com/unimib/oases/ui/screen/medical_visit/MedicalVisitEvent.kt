package com.unimib.oases.ui.screen.medical_visit

sealed class MedicalVisitEvent {
    data class EvaluationClicked(val complaintId: String): MedicalVisitEvent()
    data class ReassessmentClicked(val complaintId: String): MedicalVisitEvent()
}