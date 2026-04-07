package com.unimib.oases.ui.screen.medical_visit.disposition

sealed class DispositionEvent {
    data class DispositionTypeSelected(val dispositionChoice: DispositionChoice): DispositionEvent()
    data class WardSelected(val wardChoice: WardChoice): DispositionEvent()
    data class PrescribedTreatmentsTextChanged(val newText: String): DispositionEvent()
    data class FinalDiagnosisTextChanged(val newText: String): DispositionEvent()
    data object CloseVisitClicked: DispositionEvent()

    data class CloseVisitError(val message: String): DispositionEvent()
}