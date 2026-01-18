package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

sealed class MalnutritionScreeningEvent {
    data class WeightChanged(val weight: String) : MalnutritionScreeningEvent()
    data class HeightChanged(val height: String) : MalnutritionScreeningEvent()
    data class MuacChanged(val muac: String) : MalnutritionScreeningEvent()

    data object EditButtonPressed: MalnutritionScreeningEvent()
    data object CreateButtonPressed: MalnutritionScreeningEvent()
    data object BackButtonPressed: MalnutritionScreeningEvent()
    data object NextButtonPressed: MalnutritionScreeningEvent()
    data object ReattemptSaving: MalnutritionScreeningEvent()

    data object ConfirmDialog: MalnutritionScreeningEvent()
    data object DismissDialog: MalnutritionScreeningEvent()

    data object Retry: MalnutritionScreeningEvent()
}