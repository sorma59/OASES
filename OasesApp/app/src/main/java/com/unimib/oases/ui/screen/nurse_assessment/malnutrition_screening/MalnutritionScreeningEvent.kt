package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

sealed class MalnutritionScreeningEvent {
    data class WeightChanged(val weight: String) : MalnutritionScreeningEvent()
    data class HeightChanged(val height: String) : MalnutritionScreeningEvent()
    data class MuacChanged(val muac: String) : MalnutritionScreeningEvent()

    data object Retry: MalnutritionScreeningEvent()
}