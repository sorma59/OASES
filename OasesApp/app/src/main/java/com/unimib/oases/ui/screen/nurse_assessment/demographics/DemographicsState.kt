package com.unimib.oases.ui.screen.nurse_assessment.demographics

import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode

data class DemographicsState(
    val storedData: PatientData,
    val uiMode: PatientRegistrationScreensUiMode,

    val editingState: EditingState? = if (uiMode is PatientRegistrationScreensUiMode.Wizard)
        EditingState(storedData)
    else
        null,

    val showAlertDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class EditingState(
    val patientData: PatientData,
    val formErrors: FormErrors = FormErrors(),
    val savingState: SavingState = SavingState()
)

data class FormErrors(
    val nameError: String?= null,
    val birthDateError: String?= null,
    val sexError: String?= null,
)

data class PatientData(
    val id: String?, // Null means the patient does not exist yet
    val name: String = "",
    val birthDate: String = "",
    val ageInMonths: Int = 0,
    val sexOption: SexOption = SexOption.UNSPECIFIED_OPTION,
    val village: String = "",
    val parish: String = "",
    val subCounty: String = "",
    val district: String = "",
    val nextOfKin: String = "",
    val contact: String = ""
) {
    val age: Int
        get() = ageInMonths / 12
}

data class SavingState(
    val isLoading: Boolean = false,
    val error: String? = null
)