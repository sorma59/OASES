package com.unimib.oases.ui.screen.nurse_assessment.demographics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.usecase.InsertPatientLocallyUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase
import com.unimib.oases.domain.usecase.toFormErrors
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.Companion.DEMOGRAPHICS_COMPLETED_KEY
import com.unimib.oases.util.DateTimeFormatter
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
import kotlin.random.Random

@HiltViewModel
class DemographicsViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    private val insertPatientLocallyUseCase: InsertPatientLocallyUseCase,
    private val validatePatientInfoFormUseCase: ValidatePatientInfoFormUseCase,
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
            concludeDemographics()
        else
            _state.update {
                it.copy(uiMode = PatientRegistrationScreensUiMode.Standalone(false))
            }
    }

    private suspend fun savePatientData(): Boolean {
        if (Random.nextBoolean()) //TODO("DEBUG ONLY: REMOVE")
            return false
        _state.update {
            it.copy(isLoading = true)
        }
        val patient = _state.value.editingState!!.patientData.toModel()
        val result = insertPatientLocallyUseCase(patient)

        if (result is Outcome.Success)
            saveEdits(result.id!!)

        _state.update {
            it.copy(isLoading = false)
        }

        return (result is Outcome.Success)
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
                val newAgeInMonths = DateTimeFormatter().calculateAgeInMonths(event.birthDate)
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
                val newBirthDate = DateTimeFormatter().calculateBirthDate(event.ageInMonths)
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
                                    sex = event.sex
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

            DemographicsEvent.Retry -> {
                refreshPatientInfo()
            }

            DemographicsEvent.ConfirmDialog -> {
                _state.update {
                    it.copy(
                        editingState = it.editingState!!.copy(
                            savingState = it.editingState.savingState.copy(
                                isLoading = true,
                                error = null
                            )
                        )
                    )
                }
                viewModelScope.launch {
                    if (savePatientData()) {
                        _state.update {
                            it.copy(
                                showAlertDialog = false,
                                editingState = it.editingState!!.copy(
                                    savingState = it.editingState.savingState.copy(
                                        isLoading = false
                                    )
                                ),
                            )
                        }
                        goBack()
                    }
                    else
                        _state.update {
                            it.copy(
                                editingState = it.editingState!!.copy(
                                    savingState = it.editingState.savingState.copy(
                                        error = "Save unsuccessful, try again",
                                        isLoading = false
                                    )
                                )
                            )
                        }
                }
            }
            DemographicsEvent.DismissDialog -> {
                _state.update {
                    it.copy(
                        showAlertDialog = false,
                    )
                }
            }
            DemographicsEvent.EditButtonPressed -> enterEditMode()
        }
    }

    private suspend fun navigateBackWithResult() {
        navigationEventsChannel.send(
            NavigationEvent.NavigateBackWithResult(
                DEMOGRAPHICS_COMPLETED_KEY,
                state.value.storedData.id
            )
        )
    }

    private fun validateForm(): Boolean {

        val state = state.value.editingState!!.patientData

        // Validate
        val result = validatePatientInfoFormUseCase(
            name = state.name,
            birthDate = state.birthDate,
            sex = state.sex
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

    private fun concludeDemographics(){
        viewModelScope.launch{ navigateBackWithResult() }
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
}

private fun PatientData.toModel(): Patient {
    return if (id != null)
        Patient(
            id = id,
            name = name,
            birthDate = birthDate,
            ageInMonths = ageInMonths,
            sex = sex,
            village = village,
            parish = parish,
            subCounty = subCounty,
            district = district,
            nextOfKin = nextOfKin,
            contact = contact,
            arrivalTime = arrivalTime,
            code = TriageCode.NONE,
            roomName = "",
            status = PatientStatus.WAITING_FOR_TRIAGE
        )
    else
        Patient(
            name = name,
            birthDate = birthDate,
            ageInMonths = ageInMonths,
            sex = sex,
            village = village,
            parish = parish,
            subCounty = subCounty,
            district = district,
            nextOfKin = nextOfKin,
            contact = contact,
            arrivalTime = arrivalTime,
            code = TriageCode.NONE,
            roomName = "",
            status = PatientStatus.WAITING_FOR_TRIAGE
        )
}

private fun Patient.toState(): PatientData {
    return PatientData(
        id = id,
        name = name,
        birthDate = birthDate,
        ageInMonths = ageInMonths,
        sex = sex,
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact
    )
}