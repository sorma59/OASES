package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.patient_registration.info.Sex
import javax.inject.Inject

class ValidatePatientInfoFormUseCase @Inject constructor() {

    fun invoke(name: String, birthDate: String, sex: String): ValidationResult {

        val nameError = validateName(name)
        val birthDateError = validateBirthDate(birthDate)
        val sexError = validateSex(sex)

        val hasError = listOf(
            nameError,
            birthDateError,
            sexError
        ).any { it != null }

        if(hasError){
            return ValidationResult(
                successful = false,
                nameErrorMessage = nameError,
                birthDateErrorMessage = birthDateError,
                sexErrorMessage = sexError
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateName(name: String): String? {
        if (name.isBlank()) {
            return "The name can't be blank"
        }
        return null
    }

    private fun validateBirthDate(birthDate: String): String? {
        if (birthDate.isBlank()) {
            return "The birth date can't be blank"
        }
        return null
    }

    private fun validateSex(sex: String): String? {
        if (sex == Sex.UNSPECIFIED.displayName) {
            return "The sex must be specified"
        }
        return null
    }

    data class ValidationResult(
        val successful: Boolean,
        val nameErrorMessage: String? = null,
        val birthDateErrorMessage: String? = null,
        val sexErrorMessage: String? = null
    )

}