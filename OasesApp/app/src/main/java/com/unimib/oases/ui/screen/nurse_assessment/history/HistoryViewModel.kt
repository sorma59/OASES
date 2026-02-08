package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.util.snackbar.SnackbarData
import com.unimib.oases.ui.util.snackbar.SnackbarType
import com.unimib.oases.util.Outcome
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

    val isDoctor by derivedStateOf {
        authManager.getCurrentRole() == Role.DOCTOR
    }

    private val _state = MutableStateFlow(
        HistoryState(
            patientId = args.patientId,
            selectedTab = HistoryScreenTab.PAST_MEDICAL_HISTORY,
            tabs = HistoryScreenTab.entries.toList()
        )
    )
    val state = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    private var diseasesBeforeDenyingAll: List<PatientDiseaseState>? = null

    init {
        loadChronicDiseases()
        loadPastVisits()
    }

    private fun loadChronicDiseases() {
        viewModelScope.launch(pastMedicalHistoryContext) {
            val diseases = getChronicDiseasesInfo(state.value.patientId)
            updatePastMedicalHistoryState {
                it.copy(
                    mode = when (it.mode) {
                        is PmhMode.View -> {
                            PmhMode.View(
                                freeTextDiseases = diseases.first,
                                selectionDiseases = diseases.second
                            )
                        }
                        is PmhMode.Edit -> {
                            PmhMode.Edit(
                                originalFreeTextDiseases = diseases.first,
                                originalSelectionDiseases = diseases.second
                            )
                        }
                    },
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.TabSelected -> {
                _state.update { it.copy(selectedTab = event.tab) }
            }

            HistoryEvent.EditButtonPressed -> {
                val currentMode = state.value.pastMedicalHistoryState.mode
                if (currentMode is PmhMode.View) {
                    updatePastMedicalHistoryState {
                        it.copy(
                            mode = PmhMode.Edit(
                                originalFreeTextDiseases = currentMode.freeTextDiseases,
                                originalSelectionDiseases = currentMode.selectionDiseases,
                            )
                        )
                    }
                }
            }

            HistoryEvent.DenyAllClicked -> {
                updatePmhEditState {
                    diseasesBeforeDenyingAll = it.editingSelectionDiseases
                    it.copy(
                        editingSelectionDiseases = it.editingSelectionDiseases.map { diseaseState ->
                            diseaseState.copy(
                                isDiagnosed = false
                            )
                        }
                    )
                }
            }

            HistoryEvent.ReloadPastVisits -> {
                loadPastVisits()
            }

            HistoryEvent.ReloadPastMedicalHistory -> {
                loadChronicDiseases()
            }

            is HistoryEvent.RadioButtonClicked -> {
                updatePmhEditState {
                    val diseases = it.editingSelectionDiseases.map { diseaseState ->
                        if (diseaseState.disease == event.disease)
                            diseaseState.copy(isDiagnosed = event.isDiagnosed)
                        else
                            diseaseState
                    }
                    it.copy(
                        editingSelectionDiseases = diseases
                    )
                }
            }

            HistoryEvent.Cancel -> {
                goBackToViewMode()
            }

            HistoryEvent.Save -> {
                if (state.value.pastMedicalHistoryState.isSaveable)
                    savePastMedicalHistory()
                else
                    showCannotSaveError()
            }

            is HistoryEvent.AdditionalInfoChanged -> {
                updatePmhEditState {
                    val diseases = it.editingSelectionDiseases.map { diseaseState ->
                        if (diseaseState.disease == event.disease)
                            diseaseState.copy(additionalInfo = event.additionalInfo)
                        else
                            diseaseState
                    }
                    it.copy(
                        editingSelectionDiseases = diseases
                    )
                }
            }
            is HistoryEvent.DateChanged -> {
                updatePmhEditState {
                    val diseases = it.editingSelectionDiseases.map { diseaseState ->
                        if (diseaseState.disease == event.disease)
                            diseaseState.copy(date = event.date)
                        else
                            diseaseState
                    }
                    it.copy(
                        editingSelectionDiseases = diseases
                    )
                }
            }
            is HistoryEvent.FreeTextChanged -> {
                updatePmhEditState {
                    val diseases = it.editingFreeTextDiseases.map { diseaseState ->
                        if (diseaseState.disease == event.disease)
                            diseaseState.copy(freeTextValue = event.text)
                        else
                            diseaseState
                    }
                    it.copy(
                        editingFreeTextDiseases = diseases
                    )
                }
            }

            HistoryEvent.CreateButtonClicked -> {
                val currentMode = state.value.pastMedicalHistoryState.mode
                if (currentMode !is PmhMode.View) return // Safety check
                updatePastMedicalHistoryState {
                    it.copy(
                        mode = PmhMode.Edit(
                            originalFreeTextDiseases = currentMode.freeTextDiseases,
                            originalSelectionDiseases = currentMode.selectionDiseases
                        )
                    )
                }
            }

            HistoryEvent.ReattemptSaving -> {
                onEvent(HistoryEvent.Save)
            }

            HistoryEvent.UndoMarkingAllAsNos -> {
                diseasesBeforeDenyingAll?.let { previousState ->
                    updatePmhEditState {
                        it.copy(
                            editingSelectionDiseases = previousState
                        )
                    }
                }
                diseasesBeforeDenyingAll = null
            }
        }
    }

    fun shouldShowCreateButton() = isDoctor

    fun shouldShowEditButton() = isDoctor

    private fun goBackToViewMode() {
        val currentMode = state.value.pastMedicalHistoryState.mode
        if (currentMode !is PmhMode.Edit) return // Safety check
        updatePastMedicalHistoryState {
            it.copy(
                mode = PmhMode.View(
                    freeTextDiseases = currentMode.originalFreeTextDiseases,
                    selectionDiseases = currentMode.originalSelectionDiseases
                )
            )
        }
    }

    private fun savePastMedicalHistory() {
        val currentMode = state.value.pastMedicalHistoryState.mode
        if (currentMode !is PmhMode.Edit) return // Safety check

        val currentFreeTextDiseases = currentMode.editingFreeTextDiseases
        val currentSelectionDiseases = currentMode.editingSelectionDiseases

        val diseasesToSave = currentFreeTextDiseases + currentSelectionDiseases

        updatePastMedicalHistoryState { it.copy(isLoading = true) }

        viewModelScope.launch(pastMedicalHistoryContext) {
            val outcome = savePastMedicalHistoryUseCase(
                diseasesStates = diseasesToSave,
                patientId = state.value.patientId
            )
            when (outcome) {
                is Outcome.Error -> {
                    showSavingError()
                }

                is Outcome.Success -> {
                    // Switch back to View mode with the newly saved data
                    updatePastMedicalHistoryState {
                        it.copy(
                            mode = PmhMode.View(
                                freeTextDiseases = currentFreeTextDiseases,
                                selectionDiseases = currentSelectionDiseases
                            )
                        )
                    }
                }
            }

            updatePastMedicalHistoryState { it.copy(isLoading = false) }
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

    private fun showCannotSaveError() {
        viewModelScope.launch(pastMedicalHistoryContext) {
            uiEventsChannel.send(
                UiEvent.ShowSnackbar(
                    SnackbarData(
                        message = "At least one disease needs to have an answer",
                        type = SnackbarType.ERROR
                    )
                )
            )
        }
    }

    private fun showSavingError(error: String? = null) {
        updatePastMedicalHistoryState {
            it.copy(isLoading = false)
        }

        viewModelScope.launch(pastMedicalHistoryContext) {
            uiEventsChannel.send(
                UiEvent.ShowSnackbar(
                    SnackbarData(
                        message = error ?: "Save unsuccessful, try again",
                        type = SnackbarType.ERROR,
                        actionLabel = "Try again",
                    ) {
                        onEvent(HistoryEvent.ReattemptSaving)
                    }
                )
            )
        }
    }

//    private fun showMarkedAllAsNosSnackbar(diseasesBeforeDenyingAll: List<PatientDiseaseState>) {
//        viewModelScope.launch(pastMedicalHistoryContext) {
//            uiEventsChannel.send(
//                UiEvent.ShowSnackbar(
//                    SnackbarData(
//                        message = "All chronic diseases set no",
//                        type = SnackbarType.INFO,
//                        actionLabel = "Undo",
//                    ) {
//                        onEvent(HistoryEvent.UndoMarkingAllAsNos(diseasesBeforeDenyingAll))
//                    }
//                )
//            )
//        }
//    }

    private fun updatePastMedicalHistoryState(update: (PastMedicalHistoryState) -> PastMedicalHistoryState) {
        _state.update {
            it.copy(
                pastMedicalHistoryState = update(it.pastMedicalHistoryState)
            )
        }
    }

    /**
     * A helper function that safely updates the PMH state only if it's currently in Edit mode.
     */
    private fun updatePmhEditState(update: (PmhMode.Edit) -> PmhMode.Edit) {
        updatePastMedicalHistoryState { currentState ->
            if (currentState.mode is PmhMode.Edit) {
                currentState.copy(mode = update(currentState.mode))
            } else {
                // If not in edit mode, do nothing. This prevents illegal state changes.
                currentState
            }
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