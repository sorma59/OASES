package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.usecase.ValidateMalnutritionScreeningFormUseCase.ValidationResult
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.FormErrors
import javax.inject.Inject

fun ValidationResult.toFormErrors(): FormErrors {
    return FormErrors(
        weightErrorMessage,
        heightErrorMessage,
        muacValueErrorMessage
    )
}

class ValidateMalnutritionScreeningFormUseCase @Inject constructor() {
    operator fun invoke(weight: String, height: String, muacValue: String): ValidationResult{
        return ValidationResult(
            weightErrorMessage = validateWeight(weight),
            heightErrorMessage = validateHeight(height),
            muacValueErrorMessage = validateMuacValue(muacValue)
        )
    }

    private fun validateWeight(weight: String): String? {
        return if (weight.toDoubleOrNull() == null)
            "Weight must be valid"
        else
            null
    }

    private fun validateHeight(height: String): String? {
        return if (height.toDoubleOrNull() == null)
            "Height must be valid"
        else
            null
    }

    private fun validateMuacValue(muacValue: String): String? {
        return if (muacValue.toDoubleOrNull() == null)
            "Muac must be valid"
        else
            null
    }

    data class ValidationResult(
        val weightErrorMessage: String? = null,
        val heightErrorMessage: String? = null,
        val muacValueErrorMessage: String? = null
    ){
        val isSuccessful: Boolean
            get() = weightErrorMessage == null &&  heightErrorMessage == null && muacValueErrorMessage == null
    }
}