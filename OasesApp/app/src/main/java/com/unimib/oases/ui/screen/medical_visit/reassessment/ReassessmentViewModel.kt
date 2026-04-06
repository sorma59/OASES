package com.unimib.oases.ui.screen.medical_visit.reassessment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Reassessment
import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.domain.repository.EvaluationRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.ReassessmentRepository
import com.unimib.oases.domain.usecase.GenerateDefinitiveTherapiesUseCase
import com.unimib.oases.domain.usecase.SubmitReassessmentUseCase
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.util.snackbar.SnackbarData.Companion.SaveError
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
import com.unimib.oases.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReassessmentViewModel @Inject constructor(
    private val generateDefinitiveTherapiesUseCase: GenerateDefinitiveTherapiesUseCase,
    private val patientRepository: PatientRepository,
    private val evaluationRepository: EvaluationRepository,
    private val reassessmentRepository: ReassessmentRepository,
    private val submitReassessmentUseCase: SubmitReassessmentUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
): ViewModel() {
    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message
            )
        }
    }

    private val mainContext = dispatcher + errorHandler

    private val args = savedStateHandle.toRoute<Route.Evaluation>()

    private val _state = MutableStateFlow(
        ReassessmentState(
            patientId = args.patientId,
            visitId = args.visitId,
            complaintId = args.complaintId
        )
    )
    val state: StateFlow<ReassessmentState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch(mainContext) {

            val reassessmentDeferred = async {
                reassessmentRepository
                    .getReassessment(
                        visitId = state.value.visitId,
                        complaintId = state.value.complaintId,
                    )
                    .firstNullableSuccess()
            }

            val patientDeferred = async {
                patientRepository
                    .getPatientById(state.value.patientId)
                    .firstSuccess()
            }
            val symptomsDeferred = async {
                retrieveSymptoms(state.value.visitId, state.value.complaintId)
            }

            val reassessment = reassessmentDeferred.await()
            val patient = patientDeferred.await()
            val symptoms = symptomsDeferred.await()

            _state.update {
                it.copy(
                    patient = patient,
                    symptoms = symptoms,
                    complaint = Complaint.getComplaint(
                        complaintId = state.value.complaintId,
                        ageInMonths = patient.ageInMonths,
                        sex = patient.sex,
                        patientCategory = patient.category,
                    )
                )
            }
            if (reassessment != null) {
                _state.update {
                    reassessment.toReassessmentState()
                }
            }

        }
    }

    fun onEvent(event: ReassessmentEvent) {
        when (event) {
            is ReassessmentEvent.FindingSelected -> {
                _state.update {
                    it.copy(
                        findings = it.findings.toggle(event.finding)
                    )
                }
            }
            ReassessmentEvent.GenerateDefinitiveTherapiesClicked -> {
                viewModelScope.launch(mainContext) {
                    _state.update {
                        it.copy(
                            definitiveTherapies = generateDefinitiveTherapiesUseCase(it)
                        )
                    }
                }
            }
            ReassessmentEvent.RetryButtonClicked -> {
                _state.update {
                    it.copy(error = null)
                }
                initialize()
            }
            ReassessmentEvent.SubmitClicked -> {
                submitReassessment()
            }

            ReassessmentEvent.ReattemptSaving -> {
                onEvent(ReassessmentEvent.SubmitClicked)
            }
        }
    }

    private fun submitReassessment() {
        viewModelScope.launch(mainContext) {
            when (submitReassessmentUseCase(state.value)) {
                is Outcome.Error -> uiEventsChannel.send(
                    UiEvent.ShowSnackbar(
                        snackbarData = SaveError.copy(
                            onAction = {
                                onEvent(ReassessmentEvent.ReattemptSaving)
                            }
                        )
                    )
                )

                is Outcome.Success -> navigationEventsChannel.send(NavigationEvent.NavigateBack)
            }
        }
    }

    private suspend fun retrieveSymptoms(visitId: String, complaintId: String): Set<Symptom> {
        return evaluationRepository
            .getEvaluation(
                visitId = visitId,
                complaintId = complaintId,
            ).firstSuccess()
            .symptoms
    }

    fun Reassessment.toReassessmentState(): ReassessmentState {

        val complaint = state.value.complaint ?: error("Complaint id $complaintId not found")

        val findingSet = findings.map { snapshot ->
            complaint.findings.findings.find { it.id == snapshot.id }
                ?: error("Finding not found ${snapshot.id}")
        }.toSet()

        return state.value.copy(
            symptoms = symptoms,
            findings = findingSet,
        )
    }
}