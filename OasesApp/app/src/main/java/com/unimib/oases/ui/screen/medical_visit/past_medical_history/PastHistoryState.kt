package com.unimib.oases.ui.screen.medical_visit.past_medical_history

data class ChronicConditionsCheckboxesState(
    val diabetes: Boolean = false,
    val hypertension: Boolean = false,
    val asthma: Boolean = false,
    val arthritis: Boolean = false,
    val depression: Boolean = false,
)

data class ChronicConditionsDates(
    val diabetes: String = "",
    val hypertension: String = "",
    val asthma: String = "",
    val arthritis: String = "",
    val depression: String = "",
)

data class ChronicConditionAdditionalInfo(
    val diabetes: String = "",
    val hypertension: String = "",
    val asthma: String = "",
    val arthritis: String = "",
    val depression: String = "",
)