package com.unimib.oases.ui.screen.medical_visit

sealed class MedicalVisitEvent {
    data class ComplaintClicked(val complaintId: String): MedicalVisitEvent()
}