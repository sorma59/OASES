package com.unimib.oases.ui.screen.nurse_assessment.handler

import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoEffect
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoEvent
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoState
import javax.inject.Inject

class PatientInfoHandler @Inject constructor(
    private val validatePatientInfoFormUseCase: ValidatePatientInfoFormUseCase,
) {
    fun handle(
        state: PatientInfoState,
        event: PatientInfoEvent
    ): Pair<PatientInfoState, PatientInfoEffect?> {

        val patientState = state.patient

        return when (event) {
            is PatientInfoEvent.NameChanged -> {
                state.copy(
                    patient = patientState.copy(
                        name = event.name
                    ),
                    nameError = null,
                    isEdited = true
                ) to null
            }
            is PatientInfoEvent.BirthDateChanged -> {
                state.copy(
                    patient = patientState.copy(
                        birthDate = event.birthDate
                    ),
                    birthDateError = null,
                    isEdited = true
                ) to PatientInfoEffect.ComputeAge
            }
            is PatientInfoEvent.BirthDateComputed -> {
                state.copy(
                    patient = patientState.copy(
                        birthDate = event.birthDate
                    )
                ) to null
            }
            is PatientInfoEvent.AgeChanged -> {
                state.copy(
                    patient = patientState.copy(
                        ageInMonths = event.ageInMonths
                    ),
                    birthDateError = null
                ) to PatientInfoEffect.ComputeBirthDate
            }
            is PatientInfoEvent.AgeComputed -> {
                state.copy(
                    patient = patientState.copy(
                        ageInMonths = event.ageInMonths
                    )
                ) to null
            }
            is PatientInfoEvent.SexChanged -> {
                state.copy(
                    patient = patientState.copy(
                        sex = event.sex
                    ),
                    sexError = null,
                    isEdited = true
                ) to null
            }
            is PatientInfoEvent.VillageChanged -> {
                state.copy(
                    patient = patientState.copy(
                        village = event.village
                    ),
                    isEdited = true
                ) to null
            }
            is PatientInfoEvent.ParishChanged -> {
                state.copy(
                    patient = patientState.copy(
                        parish = event.parish
                    ),
                    isEdited = true
                ) to null
            }
            is PatientInfoEvent.SubCountyChanged -> {
                state.copy(
                    patient = patientState.copy(
                        subCounty = event.subCounty
                    ),
                    isEdited = true
                ) to null
            }
            is PatientInfoEvent.DistrictChanged -> {
                state.copy(
                    patient = patientState.copy(
                        district = event.district
                    ),
                    isEdited = true
                ) to null
            }
            is PatientInfoEvent.NextOfKinChanged -> {
                state.copy(
                    patient = patientState.copy(
                        nextOfKin = event.nextOfKin
                    ),
                    isEdited = true
                ) to null
            }
            is PatientInfoEvent.ContactChanged -> {
                state.copy(
                    patient = patientState.copy(
                        contact = event.contact
                    ),
                    isEdited = true
                ) to null
            }
            PatientInfoEvent.NextButtonPressed -> {
                val (state, isValidated) = validateForm(state)
                if (isValidated)
                    state to PatientInfoEffect.SendValidationResult
                else
                    state to null
            }
            PatientInfoEvent.ConfirmSubmission -> {
                state to PatientInfoEffect.SavePatientData
            }

            PatientInfoEvent.Retry -> {
                state.copy(isLoading = true) to PatientInfoEffect.Retry
            }
        }
    }

    private fun validateForm(state: PatientInfoState)
    : Pair<PatientInfoState, Boolean> {
        return if (!state.isNew && !state.isEdited)
            // patient is not new and it was not edited: validated
            state to true
        else { // Patient is new or it was edited: validate
            // Validate
            val result = validatePatientInfoFormUseCase(
                name = state.patient.name,
                birthDate = state.patient.birthDate,
                sex = state.patient.sex
            )

            // Update state with errors
            val newState = state.copy(
                nameError = result.nameErrorMessage,
                birthDateError = result.birthDateErrorMessage,
                sexError = result.sexErrorMessage
            )

            newState to (result.successful)
        }
    }
}