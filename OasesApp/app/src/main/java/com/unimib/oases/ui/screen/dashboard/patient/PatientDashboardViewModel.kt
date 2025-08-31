package com.unimib.oases.ui.screen.dashboard.patient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel(){

    private val _state = MutableStateFlow(
        PatientDashboardState(receivedId = savedStateHandle["patientId"]!!)
    )
    val state: StateFlow<PatientDashboardState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        getPatientData()
    }

    private fun getPatientData() {

        viewModelScope.launch(dispatcher) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val resource = patientRepository.getPatientById(_state.value.receivedId).first {
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
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
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
                                    event.button.route + _state.value.receivedId
                                )
                            )
                        }
                    }
                }
            }
            PatientDashboardEvent.PatientItemClicked -> {
                if (_state.value.patient == null)
                    getPatientData()
            }

            PatientDashboardEvent.PatientDeletionConfirmed -> {
                viewModelScope.launch(dispatcher) {

                    val result = patientRepository.deletePatientById(_state.value.receivedId)

                    if (result is Resource.Success){
                        uiEventsChannel.send(UiEvent.HideDialog)
                        _state.update { it.copy(toastMessage = "Patient successfully deleted") }
                        navigationEventsChannel.send(NavigationEvent.NavigateBack)
                    }
                    else if (result is Resource.Error)
                        _state.update { it.copy(toastMessage = "Patient deletion failed") }
                }
            }
        }

    }

    sealed class UiEvent {
        data object ShowDialog: UiEvent()
        data object HideDialog: UiEvent()
    }

    sealed class NavigationEvent{
        data class Navigate(val route: String): NavigationEvent()
        data object NavigateBack: NavigationEvent()
    }
}