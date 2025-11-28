package com.unimib.oases.ui.screen.nurse_assessment.triage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.triageSymptoms
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.RoomRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.usecase.ConfigTriageUseCase
import com.unimib.oases.domain.usecase.EvaluateTriageCodeUseCase
import com.unimib.oases.domain.usecase.GetCurrentVisitUseCase
import com.unimib.oases.domain.usecase.SaveTriageDataUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.Companion.STEP_COMPLETED_KEY
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
import com.unimib.oases.util.toggle
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

private fun TriageState.toggleRoom(room: Room): TriageState{
    return if (this.editingState!!.triageData.selectedRoom == room)
        this.copy(editingState = this.editingState.copy(triageData = this.editingState.triageData.copy(selectedRoom = null)))
    else
        this.copy(editingState = this.editingState.copy(triageData = this.editingState.triageData.copy(selectedRoom = room)))
}

@HiltViewModel
class TriageViewModel @Inject constructor(
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val roomRepository: RoomRepository,
    private val visitRepository: VisitRepository,
    private val patientRepository: PatientRepository,
    private val configTriageUseCase: ConfigTriageUseCase,
    private val evaluateTriageCodeUseCase: EvaluateTriageCodeUseCase,
    private val saveTriageDataUseCase: SaveTriageDataUseCase,
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update {
            it.copy(
                error = e.message
            )
        }
    }

    private val roomsErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update {
            it.copy(
               editingState = it.editingState?.copy(
                   roomsState = it.editingState.roomsState.copy(
                       error = e.message
                   )
               )
            )
        }
    }

    private val coroutineContext = ioDispatcher + errorHandler

    private val roomsContext = ioDispatcher + roomsErrorHandler

    val args: Route.Triage = savedStateHandle.toRoute()

    private val initialUiMode = if (args.isWizardMode)
        PatientRegistrationScreensUiMode.Wizard
    else
        PatientRegistrationScreensUiMode.Standalone()

    private val _state = MutableStateFlow(
        TriageState(
            args.patientId,
            args.visitId,
            initialUiMode
        )
    )
    val state = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(coroutineContext) {
            getPatientData()
            refresh()
            if (state.value.uiMode is PatientRegistrationScreensUiMode.Wizard) {
                _state.update {
                    check(it.patient != null) {
                        "Patient absent"
                    }
                    it.copy(
                        editingState = EditingState(
                            configTriageUseCase(it.patient.ageInMonths)
                        )
                    )
                }
                collectRooms()
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun getCurrentVisitData() {
        val visit = getCurrentVisitUseCase(state.value.patientId)
        _state.update {
            it.copy(
                visit = visit,
                storedData = it.storedData?.copy(
                    triageCode = visit.triageCode,
                    selectedRoom = visit.roomName?.let { name -> Room(name) }
                )
            )
        }
    }

    private suspend fun getPatientData() {
        val patient = patientRepository
            .getPatientById(state.value.patientId)
            .firstSuccess()
        _state.update {
            it.copy(
                patient = patient
            )
        }
    }

    private suspend fun collectRooms() {
        val rooms = roomRepository
            .getAllRooms()
            .firstSuccess()
        _state.update { triageState ->
            triageState.copy(
                editingState = triageState.editingState!!.copy(
                    roomsState = triageState.editingState.roomsState.copy(
                        rooms = rooms
                    ),
                    triageData = triageState.editingState.triageData.copy(
                        selectedRoom = state.value.visit?.roomName?.let { Room(it) }
                    )
                )
            )
        }
    }

    fun onEvent(event: TriageEvent) {
        when(event){

            is TriageEvent.FieldToggled -> handleTriageFieldToggle(event.fieldId)

            is TriageEvent.RoomClicked -> {
                _state.update {
                    it.toggleRoom(event.room)
                }
            }

            is TriageEvent.ToastShown -> {
                _state.update {
                    it.copy(toastMessage = null)
                }
            }

            is TriageEvent.Retry -> {

            }

            TriageEvent.EditButtonPressed -> enterEditMode()

            TriageEvent.CreateButtonPressed -> enterEditMode()

            TriageEvent.NextButtonPressed -> onNext()

            TriageEvent.BackButtonPressed -> onBack()

            TriageEvent.ConfirmDialog -> {
                setSavingStateToLoading()
                viewModelScope.launch {
                    if (state.value.uiMode is PatientRegistrationScreensUiMode.Wizard)
                        handleWizardDialogConfirmation()
                    else
                        handleStandaloneDialogConfirmation()
                }
            }

            TriageEvent.DismissDialog -> {
                _state.update {
                    it.copy(
                        showAlertDialog = false,
                    )
                }
            }

            TriageEvent.ToastShown -> {
                _state.update {
                    it.copy(
                        toastMessage = null
                    )
                }
            }
        }
    }

    private suspend fun handleWizardDialogConfirmation() {
        if (saveTriageDataUseCase(state.value) is Outcome.Success)
            goBackToWizard()
        else
            showSavingError()
    }

    private suspend fun handleStandaloneDialogConfirmation() {
        if (saveTriageDataUseCase(state.value) is Outcome.Success){
            saveEdits()
            goBackToViewMode()
        }
        else
            showSavingError()
    }

    private fun showSavingError(error: String? = null) {
        _state.update {
            it.copy(
                savingState = it.savingState.copy(
                    error = error ?: "Save unsuccessful, try again",
                    isLoading = false
                )
            )
        }
    }

    private fun setSavingStateToLoading() {
        _state.update {
            it.copy(savingState = it.savingState.copy(isLoading = true, error = null))
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

    fun refresh() {
        viewModelScope.launch(coroutineContext) {
            if (state.value.uiMode is PatientRegistrationScreensUiMode.Wizard)
                loadVisit(state.value.visitId)
            else{
                loadTriageEvaluationAndVisit(state.value.visitId)
                updateTriageCode()
            }
        }
    }

    private suspend fun loadTriageEvaluationAndVisit(visitId: String){
        loadVisit(visitId)
        val visit = state.value.visit
        check(visit != null) {
            "Visit cannot be null here"
        }
        val triageEvaluation = triageEvaluationRepository
            .getTriageEvaluation(visitId)
            .firstNullableSuccess()
        triageEvaluation?.let {
            _state.update {
                it.copy(
                    storedData = TriageData(
                        selectedReds = triageEvaluation.redSymptomIds.toSet(),
                        selectedYellows = triageEvaluation.yellowSymptomIds.toSet(),
                        triageCode = visit.triageCode,
                        selectedRoom = Room(visit.roomName!!)
                    )
                )
            }
        }
    }

    private suspend fun loadVisit(visitId: String) {
        val visit = visitRepository
            .getVisitById(visitId)
            .firstSuccess()
        _state.update {
            it.copy(
                visit = visit
            )
        }
    }

    private fun handleTriageFieldToggle(fieldId: String) {
        val symptom = triageSymptoms[fieldId]

        check (symptom != null) {
            "Symptom $fieldId not found in the triage symptoms map"
        }
        if (symptom.isComputed)
            _state.update { it.copy(toastMessage = "Be aware: this field is computed") }
        toggleField(fieldId)
    }

    private fun toggleField(fieldId: String) {
        _state.update {
            check(it.editingState != null) {
                "Editing data must not be null here"
            }

            if (it.editingState.triageConfig.redOptions.any { option -> option.id == fieldId }) {
                it.copy(
                    editingState = it.editingState.copy(
                        triageData = it.editingState.triageData.copy(
                            selectedReds = it.editingState.triageData.selectedReds.toggle(fieldId)
                        ),
                    )
                )
            } else {
                it.copy(
                    editingState = it.editingState.copy(
                        triageData = it.editingState.triageData.copy(
                            selectedYellows = it.editingState.triageData.selectedYellows.toggle(fieldId)
                        ),
                    )
                )
            }
        }
    }

    private fun updateTriageCode(){
        _state.update {
            when (it.uiMode) {
                is PatientRegistrationScreensUiMode.Standalone -> {
                    if (it.uiMode.isEditing)
                        it.copy(
                            editingState = it.editingState!!.copy(
                                triageData = it.editingState.triageData.copy(
                                    triageCode = evaluateTriageCodeUseCase(it.editingState.triageData.selectedReds, it.editingState.triageData.selectedYellows)
                                )
                            )
                        )
                    else
                        it.copy(
                            storedData = it.storedData?.copy(
                                triageCode = evaluateTriageCodeUseCase(it.storedData.selectedReds, it.storedData.selectedYellows)
                            )
                        )
                }
                is PatientRegistrationScreensUiMode.Wizard -> {
                    it.copy(
                        editingState = it.editingState!!.copy(
                            triageData = it.editingState.triageData.copy(
                                triageCode = evaluateTriageCodeUseCase(it.editingState.triageData.selectedReds, it.editingState.triageData.selectedYellows)
                            )
                        )
                    )
                }
            }
        }
    }

    private fun onNext(){
        updateTriageCode()
        val nextTab = state.value.editingState!!.nextTab()

        viewModelScope.launch(coroutineContext) {
            if (nextTab == null)
                if (state.value.editingState!!.triageData.selectedRoom == null)
                    _state.update {
                        it.copy(
                            toastMessage = "Select a room"
                        )
                    }
                else
                    _state.update {
                        it.copy(
                            showAlertDialog = true
                        )
                    }
            else
                _state.update {
                    it.copy(
                        editingState = it.editingState!!.copy(
                            tabStack = it.editingState.tabStack + listOf(nextTab)
                        )
                    )
                }
        }
    }

    private fun onBack(){
        if (state.value.editingState!!.tabStack.size == 1) {
            goBack()
        }
        else {
            _state.update {
                it.copy(
                    editingState = it.editingState!!.copy(
                        tabStack = it.editingState.tabStack.dropLast(1)
                    )
                )
            }
        }
    }

    private fun goBack() {
        if (state.value.uiMode is PatientRegistrationScreensUiMode.Wizard)
            concludeTriage(false)
        else
            _state.update {
                it.copy(uiMode = PatientRegistrationScreensUiMode.Standalone(false))
            }
    }

    private fun goBackToViewMode() {
        _state.update {
            it.copy(
                uiMode = PatientRegistrationScreensUiMode.Standalone(false)
            )
        }
    }

    private fun goBackToWizard() {
        concludeTriage(true)
    }

//    private fun onNext(){
//        if (state.value.currentStep == state.value.tabs.lastIndex)
//            concludeTriage()
//        else if(state.value.symptomsWereToggledSinceLastUpdate)
//            updateTriageCode()
//            if (state.value.triageCode in setOf(TriageCode.RED, TriageCode.YELLOW) )
//                goToRoomSelection()
//    }

    private fun concludeTriage(result: Boolean){
        viewModelScope.launch{ navigateBackWithResult(result) }
    }

//    private fun TriageEvaluation.toState(): TriageState {
//        return TriageState(
//            patientId = patient,
//            uiMode = TODO(),
//            patient = TODO(),
//            currentStep = TODO(),
//            selectedReds = redSymptomIds.toSet(),
//            selectedYellows = yellowSymptomIds.toSet(),
//            symptomsWereToggledSinceLastUpdate = true,
//            isLoading = false,
//        )
//    }

    private fun saveEdits() {
        _state.update {
            it.copy(
                storedData = it.editingState!!.triageData
            )
        }
    }

    private fun enterEditMode() {
        val patient = state.value.patient ?: return

        _state.update {
            val triageConfig = configTriageUseCase(patient.ageInMonths)

            val editingState = if (it.storedData == null) {
                EditingState(triageConfig = triageConfig)
            } else {
                EditingState(
                    triageConfig = triageConfig,
                    triageData = it.storedData
                )
            }

            it.copy(
                uiMode = PatientRegistrationScreensUiMode.Standalone(true),
                editingState = editingState
            )
        }

        viewModelScope.launch(roomsContext) {
            collectRooms()
        }
    }

}