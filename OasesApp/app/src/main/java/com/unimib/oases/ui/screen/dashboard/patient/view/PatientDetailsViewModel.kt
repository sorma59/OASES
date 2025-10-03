package com.unimib.oases.ui.screen.dashboard.patient.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.GetCurrentVisitMainComplaintUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
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
class PatientDetailsViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val getCurrentVisitMainComplaintUseCase: GetCurrentVisitMainComplaintUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message
            )
        }
    }

    private val _state = MutableStateFlow(
        PatientDetailsState(patientId = savedStateHandle["patientId"]!!)
    )
    val state: StateFlow<PatientDetailsState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    init {
        getPatientData()
    }

    private fun getPatientData() {
        _state.update { it.copy(isLoading = true, error = null) }
        getPatientDemographics()
        getCurrentVisitComplaintsSummaries()
        _state.update { it.copy(isLoading = false) }
    }

    private fun getCurrentVisitComplaintsSummaries() {
        viewModelScope.launch(dispatcher + errorHandler) {
            try {
                val resource = getCurrentVisitMainComplaintUseCase(_state.value.patientId)
                when (resource) {
                    is Resource.Success -> {
                        _state.update { it.copy(mainComplaintsSummaries = resource.data!!) }
                    }

                    is Resource.Error -> {
                        _state.update { it.copy(error = resource.message) }
                    }

                    else -> Unit
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun getPatientDemographics() {
        viewModelScope.launch(dispatcher + errorHandler) {
            try {
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
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun onEvent(event: PatientDetailsEvent){

        viewModelScope.launch(dispatcher + errorHandler){
            when (event) {
                PatientDetailsEvent.OnRetry -> getPatientData()
            }
        }

    }
}