package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.common.FormErrorMessages
import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase.ValidationResult
import com.unimib.oases.ui.screen.nurse_assessment.demographics.FormErrors
import com.unimib.oases.ui.screen.nurse_assessment.demographics.SexOption
import javax.inject.Inject

class ValidatePatientInfoFormUseCase @Inject constructor() {

    operator fun invoke(name: String, birthDate: String, sexOption: SexOption): ValidationResult {

        val nameError = validateName(name)
        val birthDateError = validateBirthDate(birthDate)
        val sexError = validateSex(sexOption)

        return ValidationResult(
            nameError,
            birthDateError,
            sexError
        )
    }

    private fun validateName(name: String): String? {
        if (name.isBlank()) {
            return FormErrorMessages.NAME_BLANK
        }
        return null
    }

    private fun validateBirthDate(birthDate: String): String? {
        if (birthDate.isBlank()) {
            return FormErrorMessages.BIRTHDATE_BLANK
        }
        return null
    }

    private fun validateSex(sexOption: SexOption): String? {
        if (sexOption.sex == null)
            return FormErrorMessages.SEX_NOT_SELECTED
        return null
    }

    data class ValidationResult(
        val nameErrorMessage: String? = null,
        val birthDateErrorMessage: String? = null,
        val sexErrorMessage: String? = null
    ){
        val isSuccessful: Boolean
            get() = nameErrorMessage == null && birthDateErrorMessage == null && sexErrorMessage == null
    }

}

fun ValidationResult.toFormErrors(): FormErrors {
    return FormErrors(
        nameErrorMessage,
        birthDateErrorMessage,
        sexErrorMessage
    )
}