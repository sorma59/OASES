package com.unimib.oases.ui.screen.nurse_assessment.demographics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.SavePatientDataAndCreateVisitUseCase
import com.unimib.oases.domain.usecase.SavePatientDataUseCase
import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase
import com.unimib.oases.domain.usecase.toFormErrors
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.Companion.DEMOGRAPHICS_COMPLETED_KEY
import com.unimib.oases.ui.util.snackbar.SnackbarData
import com.unimib.oases.util.DateAndTimeUtils
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.firstSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemographicsViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    private val validatePatientInfoFormUseCase: ValidatePatientInfoFormUseCase,
    private val savePatientDataUseCase: SavePatientDataUseCase,
    private val savePatientDataAndCreateVisitUseCase: SavePatientDataAndCreateVisitUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel(){

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    private val coroutineContext = ioDispatcher + errorHandler

    private val args: Route.Demographics = savedStateHandle.toRoute()

    private val initialUiMode = if (args.patientId == null)
        PatientRegistrationScreensUiMode.Wizard
    else
        PatientRegistrationScreensUiMode.Standalone()

    private val _state = MutableStateFlow(
        DemographicsState(
            PatientData(
                args.patientId
            ),
            initialUiMode
        )
    )
    val state: StateFlow<DemographicsState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val snackbarEventsChannel = Channel<SnackbarData>()
    val snackbarEvents = snackbarEventsChannel.receiveAsFlow()

    init {
        if (state.value.uiMode is PatientRegistrationScreensUiMode.Standalone)
            refreshPatientInfo()
    }

    private fun refreshPatientInfo() {
        viewModelScope.launch(coroutineContext) {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val patientId = _state.value.storedData.id

            patientId?.let {
                val patient = patientUseCase
                    .getPatient(patientId)
                    .firstSuccess()

                _state.update {
                    it.copy(
                        storedData = patient.toState()
                    )
                }
            }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun goBack() {
        if (state.value.uiMode is PatientRegistrationScreensUiMode.Wizard)
            goBackToWizard(null)
        else
            goBackToViewMode()
    }

    private fun goBackToViewMode() {
        _state.update {
            it.copy(
                uiMode = PatientRegistrationScreensUiMode.Standalone(false)
            )
        }
    }

    private fun goBackToWizard(ids: PatientAndVisitIds?) {
        concludeDemographics(ids)
    }

    fun onEvent(
        event: DemographicsEvent
    ) {

        when (event) {
            is DemographicsEvent.NameChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    name = event.name
                                ),
                                formErrors = editing.formErrors.copy(
                                    nameError = null
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.BirthDateChanged -> {
                val newAgeInMonths = DateAndTimeUtils.calculateAgeInMonths(event.birthDate)
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    birthDate = event.birthDate,
                                    ageInMonths = newAgeInMonths ?: editing.patientData.ageInMonths
                                ),
                                formErrors = editing.formErrors.copy(
                                    birthDateError = null
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.AgeChanged -> {
                val newBirthDate = DateAndTimeUtils.calculateBirthDate(event.ageInMonths)
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    ageInMonths = event.ageInMonths,
                                    birthDate = newBirthDate ?: it.editingState.patientData.birthDate
                                ),
                                formErrors = editing.formErrors.copy(
                                    birthDateError = null
                                ),
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.SexChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    sexOption = event.sexOption
                                ),
                                formErrors = editing.formErrors.copy(
                                    sexError = null
                                ),
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.VillageChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    village = event.village
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.ParishChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    parish = event.parish
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.SubCountyChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    subCounty = event.subCounty
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.DistrictChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    district = event.district
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.NextOfKinChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    nextOfKin = event.nextOfKin
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            is DemographicsEvent.ContactChanged -> {
                _state.update {
                    it.editingState?.let { editing ->
                        it.copy(
                            editingState = editing.copy(
                                patientData = editing.patientData.copy(
                                    contact = event.contact
                                )
                            )
                        )
                    } ?: it.copy(
                        error = "Invalid state, editingState cannot be null"
                    )
                }
            }
            DemographicsEvent.CancelButtonPressed -> goBack()
            DemographicsEvent.NextButtonPressed -> {
                if (validateForm())
                    _state.update {
                        it.copy(showAlertDialog = true)
                    }
            }

            DemographicsEvent.Retry -> refreshPatientInfo()

            DemographicsEvent.ConfirmDialog -> {
                dismissDialog()
                savePatient()
            }
            DemographicsEvent.DismissDialog -> dismissDialog()
            DemographicsEvent.EditButtonPressed -> enterEditMode()
        }
    }

    private fun savePatient() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (state.value.uiMode is PatientRegistrationScreensUiMode.Wizard)
                savePatientFromWizard()
            else
                savePatientFromStandalone()
        }
    }

    private suspend fun savePatientFromWizard() {
        val patientData = state.value.editingState!!.patientData
        val result = savePatientDataAndCreateVisitUseCase(patientData)
        stopLoading()
        if (result is Outcome.Success)
            goBackToWizard(result.data)
        else
            showSavingError()
    }

    private suspend fun showSavingError(error: String? = null) {
        snackbarEventsChannel.send(
            SnackbarData(
                message = error ?: "Save unsuccessful, try again",
                actionLabel = "Try again",
                onAction = { savePatient() }
            )
        )
    }

    private suspend fun showSavingSuccess() {
        snackbarEventsChannel.send(
            SnackbarData(
                message = "Patient saved successfully",
            )
        )
    }

    private suspend fun savePatientFromStandalone() {
        val patientData = state.value.editingState!!.patientData
        val result = savePatientDataUseCase(patientData)
        stopLoading()
        if (result is Outcome.Success) {
            saveEdits(result.data)
            goBackToViewMode()
            showSavingSuccess()
        }
        else
            showSavingError()
    }

    private fun dismissDialog() {
        _state.update {
            it.copy(showAlertDialog = false)
        }
    }

    private suspend fun navigateBackWithResult(result: PatientAndVisitIds?) {
        navigationEventsChannel.send(
            NavigationEvent.NavigateBackWithResult(
                DEMOGRAPHICS_COMPLETED_KEY,
                result
            )
        )
    }

    private fun validateForm(): Boolean {

        val state = state.value.editingState!!.patientData

        // Validate
        val result = validatePatientInfoFormUseCase(
            name = state.name,
            birthDate = state.birthDate,
            sexOption = state.sexOption
        )

        // Update state with errors
        _state.update {
            it.copy(
                editingState = it.editingState!!.copy(
                    formErrors = result.toFormErrors()
                )
            )
        }

        return result.isSuccessful
    }

    private fun concludeDemographics(ids: PatientAndVisitIds?) {
        viewModelScope.launch { navigateBackWithResult(ids) }
    }

    private fun saveEdits(patientId: String) {
        _state.update {
            it.copy(
                storedData = it.editingState!!.patientData.copy(
                    id = patientId
                )
            )
        }
    }

    private fun enterEditMode() {
        _state.update {
            it.copy(
                uiMode = PatientRegistrationScreensUiMode.Standalone(true),
                editingState = EditingState(
                    patientData = it.storedData
                )
            )
        }
    }

    private fun stopLoading() = _state.update { it.copy(isLoading = false) }
}