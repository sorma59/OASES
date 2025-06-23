package com.unimib.oases.domain.usecase

import javax.inject.Inject

class ValidatePatientInfoFormUseCase @Inject constructor() {

    fun invoke(name: String, birthDate: String): ValidationResult {

        val nameError = validateName(name)
        val birthDateError = validateBirthDate(birthDate)

        val hasError = listOf(
            nameError,
            birthDateError
        ).any { it != null }

        if(hasError){
            return ValidationResult(
                successful = false,
                nameErrorMessage = nameError,
                birthDateErrorMessage = birthDateError
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

    data class ValidationResult(
        val successful: Boolean,
        val nameErrorMessage: String? = null,
        val birthDateErrorMessage: String? = null
    )

}