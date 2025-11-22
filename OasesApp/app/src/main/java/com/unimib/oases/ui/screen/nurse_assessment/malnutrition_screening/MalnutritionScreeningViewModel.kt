package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.DefaultDispatcher
import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.domain.usecase.ValidateMalnutritionScreeningFormUseCase
import com.unimib.oases.domain.usecase.toFormErrors
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.Companion.STEP_COMPLETED_KEY
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.debounce
import com.unimib.oases.util.firstNullableSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

@HiltViewModel
class MalnutritionScreeningViewModel @Inject constructor(
    private val malnutritionScreeningRepository: MalnutritionScreeningRepository,
    private val validateMalnutritionScreeningFormUseCase: ValidateMalnutritionScreeningFormUseCase,
    savedStateHandle: SavedStateHandle,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message
            )
        }
    }

    val mainContext: CoroutineContext = defaultDispatcher + errorHandler

    val args: Route.MalnutritionScreening = savedStateHandle.toRoute()

    private val initialUiMode = if (args.isWizardMode)
        PatientRegistrationScreensUiMode.Wizard
    else
        PatientRegistrationScreensUiMode.Standalone()

    private val _state = MutableStateFlow(
        MalnutritionScreeningState(
            args.patientId,
            args.visitId,
            initialUiMode
        )
    )
    val state = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(mainContext) {
            refreshMalnutritionScreening(state.value.visitId)
        }
        viewModelScope.launch(mainContext) {
            _state
                .debounce(400L)
                .collect { state ->
                    state.editingData?.let { editingData ->
                        val bmi = editingData.toBmiOrNull()
                        val muacCategory = editingData.toMuacCategoryOrNull()
                        _state.update {
                            it.copy(
                                editingData = editingData.copy(
                                    bmi = bmi,
                                    muacState = editingData.muacState.copy(
                                        category = muacCategory
                                    )
                                ),
                            )
                        }
                    }
                }
        }
    }

    fun onEvent(event: MalnutritionScreeningEvent) {
        when (event) {
            is MalnutritionScreeningEvent.WeightChanged -> {
                _state.update {
                    check(it.editingData != null) {
                        "Editing data must not be null if the weight changes"
                    }
                    it.copy(
                        editingData = it.editingData.copy(
                            weight = event.weight
                        ),
                        formErrors = it.formErrors.copy(
                            weightError = null
                        )
                    )
                }
            }
            is MalnutritionScreeningEvent.HeightChanged -> {
                _state.update {
                    check(it.editingData != null) {
                        "Editing data must not be null if the height changes"
                    }
                    it.copy(
                        editingData = it.editingData.copy(
                            height = event.height
                        ),
                        formErrors = it.formErrors.copy(
                            heightError = null
                        )
                    )
                }
            }
            is MalnutritionScreeningEvent.MuacChanged -> {
                _state.update {
                    check(it.editingData != null) {
                        "Editing data must not be null if the muac changes"
                    }
                    it.copy(
                        editingData = it.editingData.copy(
                            muacState = it.editingData.muacState.copy(
                                value = event.muac
                            )
                        ),
                        formErrors = it.formErrors.copy(
                            muacValueError = null
                        )
                    )
                }
            }
            MalnutritionScreeningEvent.EditButtonPressed -> enterEditMode()

            MalnutritionScreeningEvent.CreateButtonPressed -> startMalnutritionScreening()

            MalnutritionScreeningEvent.NextButtonPressed -> {
                if (validateForm())
                    _state.update {
                        it.copy(showAlertDialog = true)
                    }
            }
            MalnutritionScreeningEvent.BackButtonPressed -> onCancel()
            MalnutritionScreeningEvent.ConfirmDialog -> {
                _state.update {
                    it.copy( savingState = it.savingState.copy(isLoading = true, error = null) )
                }
                viewModelScope.launch {
                    if (saveMalnutritionScreeningData()) {
                        _state.update {
                            it.copy(
                                showAlertDialog = false,
                                savingState = it.savingState.copy(isLoading = false)
                            )
                        }
                        goBack(true)
                    }
                    else
                        _state.update {
                            it.copy(
                                savingState = it.savingState.copy(
                                    error = "Save unsuccessful, try again",
                                    isLoading = false
                                )
                            )
                        }
                }
            }
            MalnutritionScreeningEvent.DismissDialog -> {
                _state.update {
                    it.copy(
                        showAlertDialog = false
                    )
                }
            }
            MalnutritionScreeningEvent.Retry -> {
                refreshMalnutritionScreening(state.value.visitId)
            }
        }
    }

    private suspend fun navigateBackWithResult(result: Boolean) {
        navigationEventsChannel.send(
            NavigationEvent.NavigateBackWithResult(
                STEP_COMPLETED_KEY,
                result
            )
        )
    }

    fun refreshMalnutritionScreening(visitId: String) {
        _state.update { it.copy(error = null, isLoading = true) }
        viewModelScope.launch(mainContext) {
            loadMalnutritionScreening(visitId)
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    private suspend fun loadMalnutritionScreening(visitId: String) {
        _state.update {
            it.copy(
                storedData = getMalnutritionScreening(visitId).toState()
            )
        }
    }

    private suspend fun getMalnutritionScreening(visitId: String) = malnutritionScreeningRepository
        .getMalnutritionScreening(visitId)
        .firstNullableSuccess()


    private fun MalnutritionScreening?.toState(): MalnutritionScreeningData? {
        return this?.let {
            MalnutritionScreeningData(
                weight = it.weight.toString(),
                height = it.height.toString(),
                muacState = MuacState(it.muac.value.toString(), it.muac.color),
                bmi = it.bmi
            )
        }
    }

    private fun validateForm(): Boolean {
        val validationResult = validateMalnutritionScreeningFormUseCase(
            state.value.editingData!!.weight,
            state.value.editingData!!.height,
            state.value.editingData!!.muacState.value
        )

        _state.update {
            it.copy(
                formErrors = validationResult.toFormErrors()
            )
        }

        return validationResult.isSuccessful
    }

    private fun onCancel() {
        goBack(false)
    }

    private fun goBack(result: Boolean) {
        if (state.value.uiMode is PatientRegistrationScreensUiMode.Wizard)
            viewModelScope.launch{ navigateBackWithResult(result) }
        else
            _state.update {
                it.copy(uiMode = PatientRegistrationScreensUiMode.Standalone(false))
            }
    }

    private suspend fun saveMalnutritionScreeningData(): Boolean {
        if (Random.nextBoolean()) //TODO("DEBUG ONLY: REMOVE")
            return false
        _state.update {
            it.copy(isLoading = true)
        }

        val malnutritionScreening = state.value.editingData!!
            .toMalnutritionScreening(state.value.visitId)
        val result = malnutritionScreeningRepository
            .insertMalnutritionScreening(malnutritionScreening)
        if (result is Outcome.Success)
            saveEdits()

        _state.update {
            it.copy(isLoading = false)
        }

        return (result is Outcome.Success)
    }

    private fun saveEdits() {
        _state.update {
            it.copy(
                storedData = it.editingData!!,
                editingData = null
            )
        }
    }

    private fun enterEditMode() {
        _state.update {
            it.copy(
                uiMode = PatientRegistrationScreensUiMode.Standalone(true),
                editingData = it.storedData
            )
        }
    }

    private fun startMalnutritionScreening() {
        _state.update {
            it.copy(
                uiMode = PatientRegistrationScreensUiMode.Standalone(true),
                editingData = MalnutritionScreeningData()
            )
        }
    }
}