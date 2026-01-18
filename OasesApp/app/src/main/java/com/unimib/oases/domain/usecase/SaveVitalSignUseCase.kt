package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.VitalSign
import com.unimib.oases.domain.repository.VitalSignRepository
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SaveVitalSignUseCase @Inject constructor(
    private val repo: VitalSignRepository
)
{
    suspend operator fun invoke(name: String, acronym: String, unit: String): SaveVitalSignUseCaseResult{

        val nameError = validateName(name)
        val acronymError = validateAcronym(acronym)
        val unitError = validateUnit(unit)

        if (nameError != null || acronymError != null || unitError != null) {
            return SaveVitalSignUseCaseResult.ValidationFailure(
                nameError = nameError,
                acronymError = acronymError,
                unitError = unitError
            )
        }

        return when (repo.addVitalSign(VitalSign(name, acronym, unit))){
            is Outcome.Error -> {
                SaveVitalSignUseCaseResult.RepositoryFailure
            }
            is Outcome.Success -> {
                SaveVitalSignUseCaseResult.Success
            }
        }
    }

    private fun validateName(name: String): String? {
        if (name.isBlank()) {
            return "The name can't be blank"
        }
        return null
    }

    private fun validateAcronym(acronym: String): String? {
        if (acronym.isBlank()) {
            return "The password can't be blank"
        }
        return null
    }

    private fun validateUnit(unit: String): String? {
        if (unit.isBlank()) {
            return "The password can't be blank"
        }
        return null
    }

    sealed class SaveVitalSignUseCaseResult {
        data object Success : SaveVitalSignUseCaseResult()
        data class ValidationFailure(val nameError: String?, val acronymError: String?, val unitError: String?) : SaveVitalSignUseCaseResult()
        data object RepositoryFailure : SaveVitalSignUseCaseResult()
        data object UnknownError : SaveVitalSignUseCaseResult()
    }
}