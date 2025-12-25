package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.auth.AuthManager
import com.unimib.oases.domain.usecase.GetChronicDiseasesInfo
import com.unimib.oases.domain.usecase.ReloadPastVisitsUseCase
import com.unimib.oases.domain.usecase.SavePastMedicalHistoryUseCase
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getChronicDiseasesInfo: GetChronicDiseasesInfo,
    private val reloadPastVisits: ReloadPastVisitsUseCase,
    private val savePastMedicalHistoryUseCase: SavePastMedicalHistoryUseCase,
    private val authManager: AuthManager,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val pastMedicalHistoryErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                pastMedicalHistoryState = it.pastMedicalHistoryState.copy(
                    error = e.message
                )
            )
        }
    }

    private val pastVisitsErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                pastMedicalHistoryState = it.pastMedicalHistoryState.copy(
                    error = e.message
                )
            )
        }
    }

    val pastMedicalHistoryContext: CoroutineContext = ioDispatcher + pastMedicalHistoryErrorHandler

    val pastVisitsContext: CoroutineContext = ioDispatcher + pastVisitsErrorHandler

    val args = savedStateHandle.toRoute<Route.History>()

    private val _state = MutableStateFlow(
        HistoryState(
            patientId = args.patientId,
            selectedTab = HistoryScreenTab.PAST_MEDICAL_HISTORY,
            tabs = HistoryScreenTab.entries.toList()
        )
    )
    val state = _state.asStateFlow()

    init {
        loadChronicDiseases()
        loadPastVisits()
    }

    private fun loadChronicDiseases() {
        viewModelScope.launch(pastMedicalHistoryContext) {
            val diseases = getChronicDiseasesInfo(state.value.patientId)
            updatePastMedicalHistoryState {
                it.copy(
                    diseases = diseases,
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.TabSelected -> {
                _state.update {
                    it.copy(
                        selectedTab = event.tab
                    )
                }
            }

            HistoryEvent.EditButtonPressed -> {
                if (authManager.getCurrentRole() == Role.DOCTOR) {
                    updatePastMedicalHistoryState {
                        it.copy(
                            isEditing = true,
                            editingDiseases = it.diseases
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            toastMessage = "Only doctors can edit the PMH"
                        )
                    }
                }
            }

            HistoryEvent.ReloadPastVisits -> {
                loadPastVisits()
            }

            HistoryEvent.ToastShown -> {
                _state.update {
                    it.copy(
                        toastMessage = null
                    )
                }
            }

            is HistoryEvent.RadioButtonClicked -> {
                updatePastMedicalHistoryState {
                    val diseases = it.diseases.map { diseaseState ->
                        if (diseaseState.disease == event.disease)
                            diseaseState.copy(isDiagnosed = event.isDiagnosed)
                        else
                            diseaseState
                    }
                    it.copy(
                        editingDiseases = diseases
                    )
                }
            }

            HistoryEvent.Cancel -> {
                updatePastMedicalHistoryState {
                    it.copy(
                        isEditing = false
                    )
                }
            }

            HistoryEvent.Save -> {
                updatePastMedicalHistoryState {
                    it.copy(
                        isLoading = true
                    )
                }

                val diseasesToSave = state.value.pastMedicalHistoryState.editingDiseases

                viewModelScope.launch(pastMedicalHistoryContext){
                    val outcome = savePastMedicalHistoryUseCase(
                        diseasesStates = diseasesToSave,
                        patientId = state.value.patientId
                    )
                    when (outcome) {
                        is Outcome.Error -> {
                            _state.update {
                                it.copy(
                                    toastMessage = "Saving failed",
                                )
                            }
                        }
                        is Outcome.Success -> {
                            updatePastMedicalHistoryState {
                                it.copy(
                                    diseases = diseasesToSave,
                                    isEditing = false
                                )
                            }
                        }
                        else -> Unit
                    }

                    updatePastMedicalHistoryState {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            is HistoryEvent.AdditionalInfoChanged -> {
                updatePastMedicalHistoryState {
                    val diseases = it.editingDiseases.map { diseaseState ->
                        if (diseaseState.disease == event.disease)
                            diseaseState.copy(additionalInfo = event.additionalInfo)
                        else
                            diseaseState
                    }
                    it.copy(
                        editingDiseases = diseases
                    )
                }
            }
            is HistoryEvent.DateChanged -> {
                updatePastMedicalHistoryState {
                    val diseases = it.editingDiseases.map { diseaseState ->
                        if (diseaseState.disease == event.disease)
                            diseaseState.copy(date = event.date)
                        else
                            diseaseState
                    }
                    it.copy(
                        editingDiseases = diseases
                    )
                }
            }
        }
    }

    private fun loadPastVisits() {
        viewModelScope.launch(pastVisitsContext) {
            val visits = reloadPastVisits(state.value.patientId)
            updatePastVisitsState {
                it.copy(
                    visits = visits,
                    isLoading = false
                )
            }
        }
    }

    private fun updatePastMedicalHistoryState(update: (PastMedicalHistoryState) -> PastMedicalHistoryState) {
        _state.update {
            it.copy(
                pastMedicalHistoryState = update(it.pastMedicalHistoryState)
            )
        }
    }

    private fun updatePastVisitsState(update: (PastVisitsState) -> PastVisitsState) {
        _state.update {
            it.copy(
                pastVisitsState = update(it.pastVisitsState)
            )
        }
    }
}