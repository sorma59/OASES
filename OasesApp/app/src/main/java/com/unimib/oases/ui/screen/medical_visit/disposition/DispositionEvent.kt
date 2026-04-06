package com.unimib.oases.ui.screen.medical_visit.disposition

sealed class DispositionEvent {
    data class DispositionTypeSelected(val dispositionType: DispositionType): DispositionEvent()
    data class WardSelected(val ward: Ward): DispositionEvent()
    data class PrescribedTreatmentsTextChanged(val newText: String): DispositionEvent()
    data class FinalDiagnosisTextChanged(val newText: String): DispositionEvent()
    data object CloseVisitClicked: DispositionEvent()
}