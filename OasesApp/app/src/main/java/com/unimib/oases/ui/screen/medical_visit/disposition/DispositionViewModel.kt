package com.unimib.oases.ui.screen.medical_visit.disposition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.ReassessmentRepository
import com.unimib.oases.domain.usecase.CloseVisitUseCase
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.util.snackbar.SnackbarData
import com.unimib.oases.util.Outcome
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
    private val closeVisitUseCase: CloseVisitUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
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

    private val closeVisitErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                closeVisitError = e.message,
                isLoading = false,
            )
        }
    }

    private val mainContext = dispatcher + errorHandler

    private val closeVisitContext = dispatcher + closeVisitErrorHandler
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
                        dispositionChoice = event.dispositionChoice
                    )
                }
            }
            is DispositionEvent.WardSelected -> {
                _state.update {
                    it.copy(
                        wardChoice = event.wardChoice
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
            DispositionEvent.CloseVisitClicked -> {
                viewModelScope.launch(closeVisitContext) {
                    when (closeVisitUseCase(state.value)) {
                        is Outcome.Error -> {
                            onEvent(DispositionEvent.CloseVisitError("Failed to close the visit"))
                        }
                        is Outcome.Success -> {
                            uiEventsChannel.send(
                                UiEvent.ShowSnackbar(
                                    SnackbarData.SaveSuccess
                                )
                            )
                            navigationEventsChannel.send(
                                NavigationEvent.PopUpTo(
                                    Route.Home
                                )
                            )
                        }
                    }
                }
            }

            is DispositionEvent.CloseVisitError -> {
                viewModelScope.launch {
                    uiEventsChannel.send(
                        UiEvent.ShowSnackbar(
                            SnackbarData.SaveError.copy(
                                message = event.message,
                                onAction = {
                                    onEvent(DispositionEvent.CloseVisitClicked)
                                }
                            )
                        )
                    )
                }
            }
        }

    }
}