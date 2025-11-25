package com.unimib.oases.ui.screen.dashboard.patient

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.usecase.ConfigPatientDashboardActionsUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.firstSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
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
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel(){

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        // Log the error
        Log.e("ViewModelError", "Coroutine exception", e)

        // Safely update on Main (Must when doing UI work other than updating state)
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                it.copy(
                    error = e.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }
    }

    private val mainContext = dispatcher + errorHandler

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
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch(mainContext) {
            _state.update { it.copy(isLoading = true, error = null) }
            getPatientAndVisitData()
            getButtons()
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun getButtons() {
        val actions = configurePatientDashboardActionsUseCase(_state.value.patientId)
        _state.update {
            it.copy(actions = actions)
        }
    }

    private suspend fun getPatientAndVisitData() {
        val patientWithVisitInfo = visitRepository
            .getVisitWithPatientInfo(state.value.visitId)
            .firstSuccess()
        _state.update { it.copy(patientWithVisitInfo = patientWithVisitInfo) }
    }


    fun onEvent(event: PatientDashboardEvent){

        when (event){
            is PatientDashboardEvent.ActionButtonClicked -> {
                viewModelScope.launch(mainContext) {
                    // The enum's createRoute function cleanly separates navigation from other actions.
                    when (event.action) {
                        is PatientDashboardAction.PatientNavigable -> {
                            navigationEventsChannel.send(
                                NavigationEvent.Navigate(
                                    event.action.createRoute(
                                        state.value.patientId
                                    )
                                )
                            )
                        }

                        is PatientDashboardAction.Triage -> {
                            navigationEventsChannel.send(
                                NavigationEvent.Navigate(
                                    event.action.createRoute(
                                        state.value.patientId,
                                        state.value.visitId
                                    )
                                )
                            )
                        }

                        is PatientDashboardAction.MalnutritionScreening -> {
                            navigationEventsChannel.send(
                                NavigationEvent.Navigate(
                                    event.action.createRoute(
                                        state.value.patientId,
                                        state.value.visitId
                                    )
                                )
                            )
                        }

                        is PatientDashboardAction.Send -> {
                            navigationEventsChannel.send(
                                NavigationEvent.Navigate(
                                    event.action.createRoute(
                                        state.value.patientId,
                                        state.value.visitId
                                    )
                                )
                            )
                        }

                        PatientDashboardAction.Delete -> {
                            uiEventsChannel.send(UiEvent.ShowDialog)
                        }
                    }
                }
            }
            PatientDashboardEvent.PatientItemClicked -> {
                if (_state.value.patientWithVisitInfo == null) {
                    viewModelScope.launch(mainContext){
                        getPatientAndVisitData()
                    }
                }
            }

            PatientDashboardEvent.PatientDeletionConfirmed -> {
                viewModelScope.launch(mainContext) {

                    val result = patientRepository.deletePatientById(_state.value.patientId)

                    if (result is Outcome.Success){
                        uiEventsChannel.send(UiEvent.HideDialog)
                        _state.update { it.copy(toastMessage = "Patient successfully deleted") }
                        navigationEventsChannel.send(NavigationEvent.NavigateBack)
                    }
                    else if (result is Outcome.Error)
                        _state.update { it.copy(toastMessage = "Patient deletion failed") }
                }
            }

            PatientDashboardEvent.OnBack -> {
                viewModelScope.launch(mainContext){
                    navigationEventsChannel.send(NavigationEvent.NavigateBack)
                }
            }

            PatientDashboardEvent.Refresh -> {
                refresh()
            }
        }

    }

    sealed class UiEvent {
        data object ShowDialog: UiEvent()
        data object HideDialog: UiEvent()
    }
}