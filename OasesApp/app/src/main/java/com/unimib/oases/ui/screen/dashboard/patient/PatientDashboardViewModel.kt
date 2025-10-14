package com.unimib.oases.ui.screen.dashboard.patient

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.ConfigPatientDashboardButtonsUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDashboardViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val configurePatientDashboardButtonsUseCase: ConfigPatientDashboardButtonsUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
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

    private val _state = MutableStateFlow(
        PatientDashboardState(
            patientId = savedStateHandle["patientId"]!!,
            buttons = emptyList(),
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
        viewModelScope.launch(dispatcher + errorHandler) {
            _state.update { it.copy(isLoading = true, error = null) }
            getPatientData()
            getButtons()
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun getButtons() {
        val buttons = configurePatientDashboardButtonsUseCase(_state.value.patientId)
        _state.update {
            it.copy(buttons = buttons)
        }
    }

    private suspend fun getPatientData() {

        val resource = patientRepository.getPatientById(_state.value.patientId).first {
            it is Resource.Success || it is Resource.Error
        }

        when (resource) {
            is Resource.Success -> {
                _state.update { it.copy(patient = resource.data) }
            }
            is Resource.Error -> {
                _state.update { it.copy(error = resource.message) }
            }
            else -> Unit
        }
    }


    fun onEvent(event: PatientDashboardEvent){

        when (event){
            is PatientDashboardEvent.ActionButtonClicked -> {
                viewModelScope.launch(dispatcher) {
                    when (event.button) {
                        PatientDashboardButton.DELETE -> {
                            uiEventsChannel.send(UiEvent.ShowDialog)
                        }

                        else -> {
                            navigationEventsChannel.send(
                                NavigationEvent.Navigate(
                                    event.button.route + _state.value.patientId
                                )
                            )
                        }
                    }
                }
            }
            PatientDashboardEvent.PatientItemClicked -> {
                if (_state.value.patient == null) {
                    viewModelScope.launch(dispatcher + errorHandler){
                        getPatientData()
                    }
                }
            }

            PatientDashboardEvent.PatientDeletionConfirmed -> {
                viewModelScope.launch(dispatcher) {

                    val result = patientRepository.deletePatientById(_state.value.patientId)

                    if (result is Resource.Success){
                        uiEventsChannel.send(UiEvent.HideDialog)
                        _state.update { it.copy(toastMessage = "Patient successfully deleted") }
                        navigationEventsChannel.send(NavigationEvent.NavigateBack)
                    }
                    else if (result is Resource.Error)
                        _state.update { it.copy(toastMessage = "Patient deletion failed") }
                }
            }

            PatientDashboardEvent.OnBack -> {
                viewModelScope.launch(dispatcher){
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