package com.unimib.oases.domain.usecase

import javax.inject.Inject

class ValidatePatientInfoFormUseCase @Inject constructor() {

    fun invoke(name: String, age: Int): ValidationResult {

        val nameError = validateName(name)
        val ageError = validateAge(age)

        val hasError = listOf(
            nameError,
            ageError
        ).any { it != null }

        if(hasError){
            return ValidationResult(
                successful = false,
                nameErrorMessage = nameError,
                ageErrorMessage = ageError
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

    private fun validateAge(age: Int): String? {
        if (age < 1) {
            return "The age can't be blank"
        }
        return null
    }

    data class ValidationResult(
        val successful: Boolean,
        val nameErrorMessage: String? = null,
        val ageErrorMessage: String? = null
    )

}