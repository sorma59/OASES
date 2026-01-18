package com.unimib.oases.ui.screen.dashboard.patient

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.di.MainDispatcher
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.usecase.ConfigPatientDashboardActionsUseCase
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDashboardViewModel @Inject constructor(
    private val visitRepository: VisitRepository,
    private val patientRepository: PatientRepository,
    private val configurePatientDashboardActionsUseCase: ConfigPatientDashboardActionsUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher
): ViewModel(){

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("ViewModelError", "Coroutine exception", e)

        // Safely update on Main (Must when doing UI work other than updating state)
        viewModelScope.launch(mainDispatcher) {
            _state.update {
                it.copy(
                    error = e.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }
    }

    private val deletionErrorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("ViewModelError", "Coroutine exception while deleting patient:", e)

        // Safely update on Main (Must when doing UI work other than updating state)
        viewModelScope.launch(mainDispatcher) {
            _state.update {
                it.copy(
                    deletionError = e.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }
    }

    private val mainContext = ioDispatcher + errorHandler

    private val deletionContext = ioDispatcher + deletionErrorHandler

    private val args = savedStateHandle.toRoute<Route.PatientDashboard>()

    private val _state = MutableStateFlow(
        PatientDashboardState(
            patientId = args.patientId,
            visitId = args.visitId,
            actions = emptyList(),
        )
    )
    val state: StateFlow<PatientDashboardState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(mainContext) { refresh() }
    }

    private suspend fun refresh() {
        _state.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        val patientWithVisitInfo = getPatientAndVisitData()
        val buttons = getButtons()
        _state.update {
            it.copy(
                isLoading = false,
                patientWithVisitInfo = patientWithVisitInfo,
                actions = buttons
            )
        }
    }

    private fun getButtons(): List<PatientDashboardAction> {
        return configurePatientDashboardActionsUseCase(_state.value.patientId)
    }

    private suspend fun getPatientAndVisitData(): PatientWithVisitInfo {
        return visitRepository
            .getVisitWithPatientInfo(state.value.visitId)
            .firstSuccess()
    }

    private fun dismissDialog() {
        _state.update {
            it.copy(
                showAlertDialog = false
            )
        }
    }

    fun onEvent(event: PatientDashboardEvent) {

        when (event) {
            is PatientDashboardEvent.ActionButtonClicked -> handleActionButtonClick(event.action)

            PatientDashboardEvent.PatientDeletionConfirmed -> {
                dismissDialog()
                deletePatient()
            }

            PatientDashboardEvent.ReattemptPatientDeletion -> deletePatient()

            PatientDashboardEvent.PatientDeletionCancelled -> dismissDialog()

            PatientDashboardEvent.OnBack -> {
                viewModelScope.launch(mainContext) {
                    navigationEventsChannel.send(NavigationEvent.NavigateBack)
                }
            }

            PatientDashboardEvent.Refresh -> {
                viewModelScope.launch(mainContext) { refresh() }
            }
        }
    }

    private fun handleActionButtonClick(
        action: PatientDashboardAction,
    ) {
        viewModelScope.launch(mainContext) {
            when (action) {
                is PatientDashboardAction.PatientNavigable -> {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            action.createRoute(
                                state.value.patientId
                            )
                        )
                    )
                }

                is PatientDashboardAction.Triage -> {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            action.createRoute(
                                state.value.patientId,
                                state.value.visitId
                            )
                        )
                    )
                }

                is PatientDashboardAction.MalnutritionScreening -> {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            action.createRoute(
                                state.value.patientId,
                                state.value.visitId
                            )
                        )
                    )
                }

                is PatientDashboardAction.Send -> {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            action.createRoute(
                                state.value.patientId,
                                state.value.visitId
                            )
                        )
                    )
                }

                PatientDashboardAction.Delete -> {
                    _state.update {
                        it.copy(
                            showAlertDialog = true
                        )
                    }
                }
            }
        }
    }

    private fun deletePatient() {
        viewModelScope.launch(deletionContext) {

            _state.update {
                it.copy(
                    isLoading = true,
                    deletionError = null
                )
            }

            val result = patientRepository.deletePatientById(state.value.patientId)

            _state.update {
                it.copy(isLoading = false)
            }

            when (result) {
                is Outcome.Success -> {
                    uiEventsChannel.send(
                        UiEvent.ShowSnackbar(
                            SnackbarData("Patient successfully deleted")
                        )
                    )
                    navigationEventsChannel.send(NavigationEvent.NavigateBack)
                }

                is Outcome.Error -> {
                    uiEventsChannel.send(
                        UiEvent.ShowSnackbar(
                            SnackbarData(
                                message = "Patient deletion failed",
                                actionLabel = "Try again"
                            ) {
                                onEvent(PatientDashboardEvent.ReattemptPatientDeletion)
                            }
                        )
                    )
                }
            }
        }
    }
}

//when (result) {
//    is Outcome.Success -> it.copy(
//    toastMessage = "Patient successfully deleted"
//    )
//
//    is Outcome.Error -> it.copy(
//    deletionError = "Patient deletion failed"
//    )
//}