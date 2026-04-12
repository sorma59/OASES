package com.unimib.oases.ui.screen.medical_visit.reassessment.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Reassessment
import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.ReassessmentRepository
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
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
class ReassessmentSummaryViewModel @Inject constructor(
    private val reassessmentRepository: ReassessmentRepository,
    private val patientRepository: PatientRepository,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
): ViewModel() {
    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message,
                isLoading = false,
            )
        }
    }

    private val mainContext = dispatcher + errorHandler

    private val args = savedStateHandle.toRoute<Route.Evaluation>()

    private val _state = MutableStateFlow(
        ReassessmentSummaryState(
            visitId = args.visitId,
            patientId = args.patientId,
            complaintId = args.complaintId,
        )
    )
    val state: StateFlow<ReassessmentSummaryState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch(mainContext){

            _state.update {
                it.copy(
                    error = null,
                    isLoading = true,
                )
            }

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
                    .getPatientById(patientId = state.value.patientId)
                    .firstSuccess()
            }


            val reassessment = reassessmentDeferred.await()
            val patient = patientDeferred.await()

            _state.update { initialState ->
                // Set complaint
                initialState.copy(
                    complaint = Complaint.getComplaint(
                        state.value.complaintId,
                        patient.ageInMonths,
                        patient.sex,
                        patient.category
                    ),
                ).let {
                    // Set reassessment data
                    reassessment!!.toReassessmentSummaryState(it)
                        // Stop loading
                        .copy(isLoading = false)
                }
            }
        }
    }

    fun onEvent(event: ReassessmentSummaryEvent) {

        when (event) {
            ReassessmentSummaryEvent.EditButtonClicked -> {
                viewModelScope.launch(mainContext) {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.Reassessment(
                                patientId = state.value.patientId,
                                visitId = state.value.visitId,
                                complaintId = state.value.complaintId,
                            )
                        )
                    )
                }
            }

            ReassessmentSummaryEvent.RetryButtonClicked -> {
                initialize()
            }
        }
    }

    private fun Reassessment.toReassessmentSummaryState(
        currentState: ReassessmentSummaryState,
    ): ReassessmentSummaryState {
        return currentState.copy(
            symptoms = symptoms,
            findings = findings,
            definitiveTherapies = definitiveTherapies.toSet(),
        )
    }

}