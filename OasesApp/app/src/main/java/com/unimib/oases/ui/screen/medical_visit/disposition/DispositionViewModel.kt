package com.unimib.oases.ui.screen.medical_visit.disposition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.ReassessmentRepository
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
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

@HiltViewModel
class DispositionViewModel @Inject constructor(
    private val reassessmentRepository: ReassessmentRepository,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
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

    private val args = savedStateHandle.toRoute<Route.Disposition>()

    private val _state = MutableStateFlow(
        DispositionState(
            args.visitId
        )
    )
    val state = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch(mainContext) {
            _state.update {
                it.copy(
                    error = null,
                    isLoading = true,
                )
            }

            _state.update {
                it.copy(
                    complaintIds = reassessmentRepository
                        .getVisitReassessments(it.visitId)
                        .firstSuccess()
                        .map { reassessment ->  reassessment.complaintId }
                        .toSet(),
                    isLoading = false,
                )
            }
        }
    }

    fun onEvent(event: DispositionEvent) {
        when (event) {
            is DispositionEvent.DispositionTypeSelected -> {
                _state.update {
                    it.copy(
                        dispositionType = event.dispositionType
                    )
                }
            }
            is DispositionEvent.WardSelected -> {
                _state.update {
                    it.copy(
                        dispositionType = DispositionType.Hospitalization(event.ward)
                    )
                }
            }
            is DispositionEvent.PrescribedTreatmentsTextChanged -> {
                _state.update {
                    it.copy(
                        prescribedTreatmentsText = event.newText
                    )
                }
            }
            is DispositionEvent.FinalDiagnosisTextChanged -> {
                _state.update {
                    it.copy(
                        finalDiagnosisText = event.newText
                    )
                }
            }
            DispositionEvent.CloseVisitClicked -> {}
        }

    }
}