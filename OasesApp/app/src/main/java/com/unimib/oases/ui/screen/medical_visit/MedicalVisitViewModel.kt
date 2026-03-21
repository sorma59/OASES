package com.unimib.oases.ui.screen.medical_visit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.Resource
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
class MedicalVisitViewModel @Inject constructor(
    private val complaintSummaryRepository: ComplaintSummaryRepository,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
):ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message
            )
        }
    }

    private val mainContext = dispatcher + errorHandler

    private val args = savedStateHandle.toRoute<Route.MedicalVisit>()

    private val _state = MutableStateFlow(
        MedicalVisitState(
            patientId = args.patientId,
            visitId = args.visitId,
        )
    )
    val state: StateFlow<MedicalVisitState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(mainContext) {
            complaintSummaryRepository
                .getVisitComplaintsSummaries(state.value.visitId)
                .collect { complaintSummaries ->
                    when (complaintSummaries) {
                        is Resource.Error -> _state.update { it.copy(error = complaintSummaries.message) }
                        is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                        is Resource.NotFound -> throw NoSuchElementException("Complaint summaries not found")
                        is Resource.Success -> _state.update {
                            it.copy(
                                complaintSummaries = complaintSummaries.data.associateBy {
                                    complaint -> complaint.complaintId
                                }
                            )
                        }
                    }
                }
        }
    }

    fun onEvent(event: MedicalVisitEvent) {
        when (event) {
            is MedicalVisitEvent.EvaluationClicked -> {
                viewModelScope.launch {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.Evaluation(
                                patientId = state.value.patientId,
                                visitId = state.value.visitId,
                                complaintId = event.complaintId,
                            )
                        )
                    )
                }
            }

            is MedicalVisitEvent.ReassessmentClicked -> {
                viewModelScope.launch {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.Reassessment(
                                patientId = state.value.patientId,
                                visitId = state.value.visitId,
                                complaintId = event.complaintId,
                            )
                        )
                    )
                }
            }
        }
    }
}