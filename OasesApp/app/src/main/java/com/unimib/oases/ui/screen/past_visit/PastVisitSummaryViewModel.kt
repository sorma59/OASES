package com.unimib.oases.ui.screen.past_visit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.usecase.GetChronicDiseasesInfo
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.mapper.toState
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.nurse_assessment.demographics.toState
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageData
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
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
class PastVisitSummaryViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    private val visitRepository: VisitRepository,
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val malnutritionScreeningRepository: MalnutritionScreeningRepository,
    private val getChronicDiseasesInfo: GetChronicDiseasesInfo,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
): ViewModel(){
    private val patientErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                patientError = e.message,
                isPatientDataLoading = false,
            )
        }
    }
    private val triageErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                triageError = e.message,
                isTriageDataLoading = false,
            )
        }
    }
    private val malnutritionErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                malnutritionError = e.message,
                isMalnutritionDataLoading = false,
            )
        }
    }
    private val chronicDiseasesErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                chronicDiseasesError = e.message,
                isChronicDiseasesDataLoading = false,
            )
        }
    }

    val patientContext: CoroutineContext = ioDispatcher + patientErrorHandler
    val triageContext: CoroutineContext = ioDispatcher + triageErrorHandler
    val malnutritionContext: CoroutineContext = ioDispatcher + malnutritionErrorHandler
    val chronicDiseasesContext: CoroutineContext = ioDispatcher + chronicDiseasesErrorHandler

    val args: Route.MalnutritionScreening = savedStateHandle.toRoute()

    private val _state = MutableStateFlow(
        PastVisitSummaryState(
            args.visitId,
            args.patientId,
        )
    )
    val state = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isPatientDataLoading = true,
                    isTriageDataLoading = true,
                    isMalnutritionDataLoading = true,
                    isChronicDiseasesDataLoading = true,
                )
            }
            launch(patientContext) { getPatientData() }
            launch(triageContext) { getTriageAndVisitData() }
            launch(malnutritionContext) { getMalnutritionData() }
            launch(chronicDiseasesContext) { getChronicDiseasesData() }
        }
    }

    private suspend fun getChronicDiseasesData() {
        val diseases = getChronicDiseasesInfo(state.value.patientId)
        _state.update {
            it.copy(
                chronicDiseasesData = diseases,
                isChronicDiseasesDataLoading = false
            )
        }
    }

    private suspend fun getMalnutritionData() {
        _state.update {
            it.copy(
                malnutritionData = malnutritionScreeningRepository
                    .getMalnutritionScreening(state.value.visitId)
                    .firstNullableSuccess()
                    .toState(),
                isMalnutritionDataLoading = false,
            )
        }
    }

    private suspend fun getTriageAndVisitData() {
        loadVisit(state.value.visitId)
        val visit = state.value.visit
        check(visit != null) {
            "Visit cannot be null here"
        }
        val triageEvaluation = triageEvaluationRepository
            .getTriageEvaluation(state.value.visitId)
            .firstNullableSuccess()
        _state.update {
            it.copy(
                triageData = TriageData(
                    selectedReds = triageEvaluation?.redSymptomIds?.toSet().orEmpty(),
                    selectedYellows = triageEvaluation?.yellowSymptomIds?.toSet().orEmpty(),
                    triageCode = visit.triageCode,
                    selectedRoom = Room(visit.roomName ?: "No room")
                ),
                isTriageDataLoading = false,
            )
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

    private suspend fun getPatientData() {
        val patient = patientUseCase
            .getPatient(state.value.patientId)
            .firstSuccess()

        _state.update {
            it.copy(
                patientData = patient.toState(),
                isPatientDataLoading = false,
            )
        }
    }

    fun onEvent(event: PastVisitSummaryEvent) {
        when (event) {
            else -> Unit
        }
    }
}