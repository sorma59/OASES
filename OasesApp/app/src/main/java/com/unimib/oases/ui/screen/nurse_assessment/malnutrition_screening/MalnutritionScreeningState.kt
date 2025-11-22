package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.domain.model.Muac
import com.unimib.oases.domain.model.MuacCategory
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode

// =====================================================================================
// 1. A dedicated data class to hold ONLY the core screening data.
// =====================================================================================

/**
 * Represents the core data points of a malnutrition screening. This is a pure data holder.
 */
data class MalnutritionScreeningData(
    val weight: String = "", // Kg
    val height: String = "", // cm
    val bmi: Double? = null,
    val muacState: MuacState = MuacState(),
)

data class FormErrors(
    val weightError: String? = null,
    val heightError: String? = null,
    val muacValueError: String? = null
)

// =====================================================================================
// 2. The top-level UI state class for the ENTIRE screen.
//    It CONTAINS MalnutritionScreeningData instead of duplicating its fields.
// =====================================================================================

/**
 * Represents the complete UI state for the Malnutrition Screening screen.
 */
data class MalnutritionScreeningState(
    // --- Core Identifiers ---
    val patientId: String,
    val visitId: String,

    // --- UI State ---
    val uiMode: PatientRegistrationScreensUiMode,
    val error: String? = null,
    val isLoading: Boolean = true,
    val showAlertDialog: Boolean = false,
    val formErrors: FormErrors = FormErrors(),

    // --- Domain and Editing State ---
    // Holds the current, saved, screening data.
    val storedData: MalnutritionScreeningData? = null,

    // Holds the editing data, potentially unsaved.
    // It's nullable because it only exists when editing an existing record.
    val editingData: MalnutritionScreeningData? = if (uiMode is PatientRegistrationScreensUiMode.Wizard)
        MalnutritionScreeningData()
    else
        null,

    val savingState: SavingState = SavingState()
)

data class MuacState(
    val value: String = "",
    val category: MuacCategory? = null,
)

data class SavingState(
    val isLoading: Boolean = false,
    val error: String? = null
)

// =====================================================================================
// 4. Extension functions are now on the more specific `MalnutritionScreeningData` class.
// =====================================================================================

/**
 * Calculates the BMI from the weight and height in MalnutritionScreeningData.
 * Returns null if the inputs are not valid numbers or if height is zero.
 */
fun MalnutritionScreeningData.toBmiOrNull(): Double? {
    val weight = this.weight.toDoubleOrNull() ?: return null
    val height = this.height.toDoubleOrNull() ?: return null
    // Avoid division by zero
    if (height == 0.0) return null
    // Convert height from cm to meters for BMI calculation
    return weight / ((height / 100) * (height / 100))
}

/**
 * Determines the MUAC category based on the MUAC value.
 */
fun MalnutritionScreeningData.toMuacCategoryOrNull(): MuacCategory? {
    val muac = this.muacState.value.toDoubleOrNull() ?: return null
    return Muac(muac).color
}

/**
 * Converts the MalnutritionScreeningData into a MalnutritionScreening domain model.
 * Returns null if any of the required fields are invalid.
 */
fun MalnutritionScreeningData.toMalnutritionScreening(visitId: String): MalnutritionScreening {
    val weight = this.weight.toDoubleOrNull()
    val height = this.height.toDoubleOrNull()
    val muacValue = this.muacState.value.toDoubleOrNull()

    // Ensure all necessary data is present before creating the domain model
    check (weight != null && height != null && muacValue != null) {
        "Every field must be filled in"
    }

    return MalnutritionScreening(
        visitId = visitId,
        weight = weight,
        height = height,
        muac = Muac(muacValue)
    )
}
