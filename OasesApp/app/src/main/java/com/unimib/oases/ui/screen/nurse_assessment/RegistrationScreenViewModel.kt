package com.unimib.oases.ui.screen.nurse_assessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
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
class RegistrationScreenViewModel @Inject constructor(
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(
        RegistrationState()
    )
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message
            )
        }
    }

    val mainContext = ioDispatcher + errorHandler

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.PatientAndVisitCreated -> {
               _state.update {
                   it.copy(
                       patientId = event.patientId,
                       visitId = event.visitId
                   )
               }
                onEvent(RegistrationEvent.StepCompleted)
            }

            RegistrationEvent.StepCompleted -> {
                _state.update {
                    it.copy(
                        currentStep = it.currentStep + 1
                    )
                }
            }

            RegistrationEvent.Retry -> {
                _state.update {
                    it.copy(
                        error = null
                    )
                }
            }
        }
    }

    // -----------------Vital Signs---------------------------

//    fun onVitalSignsEvent(event: VitalSignsEvent) {
//        when (event) {
//            is VitalSignsEvent.Retry -> {
////                refreshVitalSigns()
//            }
//
//            is VitalSignsEvent.ValueChanged -> {
//                updateVitalSignsState {
//                    it.copy(
//                        vitalSigns = it.vitalSigns.map { vitalSign ->
//                            if (vitalSign.name == event.vitalSign) {
//                                vitalSign.copy(value = event.value)
//                            } else {
//                                vitalSign
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }

//    private fun refreshVitalSigns(){
//        viewModelScope.launch(vitalSignsContext) {
//            updateVitalSignsState { it.copy(error = null, isLoading = true) }
//            loadVitalSigns()
//            getCurrentVisit(_state.value.patientId)?.let {
//                if (_state.value.vitalSignsState.error == null)
//                    loadVisitVitalSigns(it.id)
//            }
//            updateVitalSignsState { it.copy(isLoading = false) }
//        }
//    }

//    private suspend fun loadVitalSigns() {
//        val vitalSigns = vitalSignsUseCases
//            .getVitalSigns()
//            .firstSuccess()
//        val newStates = vitalSigns.map { vitalSign -> // More efficient mapping
//            PatientVitalSignState(vitalSign.name, vitalSign.acronym, vitalSign.unit)
//        }
//        updateVitalSignsState {
//            it.copy(
//                vitalSigns = newStates, // Set the list, don't append if it's initial load
//            )
//        }
//    }

    // --------------Visit History------------------

//    fun onVisitHistoryEvent(event: VisitHistoryEvent) {
//        when (event) {
//            is VisitHistoryEvent.Retry -> {
//                refreshVisitHistory(state.value.demographicsState.patient.id)
//            }
//        }
//    }

//    fun refreshVisitHistory(patientId: String) {
//        updateVisitHistoryState { it.copy(error = null, isLoading = true) }
//        viewModelScope.launch(visitContext) {
//            loadVisits(patientId)
//        }
//    }
//
//    private suspend fun loadVisits(patientId: String) {
//
//        patientUseCase.getPatientVisits(patientId).collect { visits ->
//            if (visits is Resource.Success)
//                updateVisitHistoryState { it.copy(visits = visits.data, isLoading = false) }
//            else if (visits is Resource.Error)
//                updateVisitHistoryState { it.copy(error = visits.message, isLoading = false) }
//        }
//
//    }

//    private fun handlePatientSubmission(){
//
//        val ageInMonths = _state.value.demographicsState.patient.ageInMonths
//        val id = _state.value.demographicsState.patient.id
//
//        applicationScope.launch(mainContext) {
//            val currentVisit = getCurrentVisit(id)
//            _state.update {
//                _state.value.copy(
//                    currentVisit = currentVisit
//                )
//            }
//            updateTriageState {
//                it.copy(
//                    ageInMonths = ageInMonths,
//                    triageConfig = configTriageUseCase(ageInMonths),
//                    patientCategory = getPatientCategoryUseCase(ageInMonths)
//                )
//            }
//            refreshVisitHistory(id)
//            if (currentVisit != null) {
//                refreshTriage(currentVisit.id)
//                refreshMalnutritionScreening(currentVisit.id)
//            }
//        }
//    }

//    private fun handleVitalSignsSubmission(){
//        fun getVitalSignValue(name: String) =
//            _state.value.vitalSignsState.vitalSigns.firstOrNull {
//                it.name == name && it.value.isNotEmpty()
//            }?.value?.toDouble()
//
//        val vitalSigns = VitalSigns(
//            sbp = getVitalSignValue("Systolic Blood Pressure")?.toInt(),
//            dbp = getVitalSignValue("Diastolic Blood Pressure")?.toInt(),
//            hr = getVitalSignValue("Heart Rate")?.toInt(),
//            rr = getVitalSignValue("Respiratory Rate")?.toInt(),
//            spo2 = getVitalSignValue("Oxygen Saturation")?.toInt(),
//            temp = getVitalSignValue("Temperature")
//        )
//
//        updateTriageState {
//            val ageInMonths = _state.value.demographicsState.patient.ageInMonths
//            it.copy(
//                selectedReds = computeSymptomsUseCase.computeRedSymptoms(
//                    selectedReds = it.selectedReds,
//                    ageInMonths = ageInMonths,
//                    vitalSigns = vitalSigns
//                ),
//                selectedYellows = computeSymptomsUseCase.computeYellowSymptoms(
//                    selectedYellows = it.selectedYellows,
//                    ageInMonths = ageInMonths,
//                    vitalSigns = vitalSigns
//                )
//            )
//        }
//        updateTriageCode()
//    }

//    private fun handleFinalSubmission(){
//        applicationScope.launch(mainContext) {
//            val patient = _state.value.demographicsState.patient
//            val currentVisit = _state.value.currentVisit
//            val triageCode =
//                if (_state.value.triageState.loaded)
//                    _state.value.triageState.triageCode
//                else
//                    null
//            val assignedRoom = _state.value.roomsState.currentRoom?.name ?: ""
//            val visit =
//                currentVisit?.copy(triageCode = triageCode ?: currentVisit.triageCode)
//                    ?: Visit(
//                        patientId = patient.id,
//                        triageCode = triageCode!!,
//                        date = LocalDate.now(),
//                        description = ""
//                    )
//            visitUseCase.addVisit(visit)
//
//            val vitalSigns = _state.value.vitalSignsState.toVisitVitalSigns(visit.id)
//
//            vitalSigns.forEach {
//                visitVitalSignsUseCase.addVisitVitalSign(it)
//            }
//
//            patientUseCase.updateStatus(patient, PatientStatus.WAITING_FOR_VISIT.displayValue, triageCode.toString(), assignedRoom)
//
//            if (state.value.triageState.loaded)
//                triageEvaluationRepository.insertTriageEvaluation(_state.value.triageState.mapToTriageEvaluation(visit.id))
//
//            val screening = _state.value.malnutritionScreeningState.toMalnutritionScreeningOrNull(visit.id)
//            if (screening != null) {
//                val result = malnutritionScreeningRepository.insertMalnutritionScreening(screening)
//                if (result is Outcome.Error)
//                    Log.e("RegistrationScreenViewModel", result.message)
//            }
//        }
//    }

    // --------------Flow-----------------

    fun onNext() {
        viewModelScope.launch(mainContext) {
            val currentTab = state.value.currentTab

            val destination: Route? = when (currentTab.destinationClass) {
                Route.Demographics::class -> Route.Demographics()

                Route.Triage::class -> {
                    val patientId = state.value.patientId
                    requireNotNull(patientId) { "Cannot navigate to Triage without a patientId." }
                    val visitId = state.value.visitId
                    requireNotNull(visitId) { "Cannot navigate to Malnutrition Screening without a visitId." }
                    Route.Triage(patientId, visitId, isWizardMode = true)
                }

                Route.MalnutritionScreening::class -> {
                    val patientId = state.value.patientId
                    requireNotNull(patientId) { "Cannot navigate to Malnutrition Screening without a patientId." }
                    val visitId = state.value.visitId
                    requireNotNull(visitId) { "Cannot navigate to Malnutrition Screening without a visitId." }
                    Route.MalnutritionScreening(patientId, visitId, true)
                }

                null -> null

                else -> throw IllegalStateException("Unhandled route class in wizard: ${currentTab.destinationClass}")
            }

            destination?.let {
                navigationEventsChannel.send(
                    NavigationEvent.Navigate(
                        it
                    )
                )
            } ?: navigationEventsChannel.send(NavigationEvent.NavigateBack)
        }
    }

    fun onBack() {
        viewModelScope.launch(mainContext){
            navigationEventsChannel.send(NavigationEvent.NavigateBack)
        }
    }

    companion object {
        const val DEMOGRAPHICS_COMPLETED_KEY = "demographics_completed"
        const val STEP_COMPLETED_KEY = "step_completed"
    }
}