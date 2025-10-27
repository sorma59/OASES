package com.unimib.oases.ui.screen.dashboard.patient.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.GetCurrentVisitMainComplaintUseCase
import com.unimib.oases.domain.usecase.GetCurrentVisitUseCase
import com.unimib.oases.domain.usecase.GetPatientChronicDiseasesUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstNullableSuccess
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
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val getCurrentVisitMainComplaintUseCase: GetCurrentVisitMainComplaintUseCase,
    private val getPatientChronicDiseasesUseCase: GetPatientChronicDiseasesUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                currentVisitRelatedError = e.message
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
        _state.update { it.copy(isLoading = true, currentVisitRelatedError = null, demographicsError = null) }
        viewModelScope.launch(dispatcher + errorHandler) {
            getPatientDemographics()
            getPastMedicalHistory()
            getCurrentVisit()
            getComplaintsSummaries()
        }
        _state.update { it.copy(isLoading = false) }
    }

    private suspend fun getPastMedicalHistory() {
        try {
            val patientDiseases = getPatientChronicDiseasesUseCase(
                _state.value.patientId
            )
            _state.update { it.copy(chronicDiseases = patientDiseases) }
        } catch (e: Exception) {
            _state.update { it.copy(currentVisitRelatedError = e.message) }
        }
    }

    private suspend fun getComplaintsSummaries() {
        try {
            val resource = getCurrentVisitMainComplaintUseCase(
                _state.value.patientId,
                _state.value.currentVisit
            )
            when (resource) {
                is Resource.Success -> {
                    _state.update { it.copy(mainComplaintsSummaries = resource.data) }
                }

                is Resource.Error -> {
                    _state.update { it.copy(currentVisitRelatedError = resource.message) }
                }

                else -> Unit
            }
        } catch (e: Exception) {
            _state.update { it.copy(currentVisitRelatedError = e.message) }
        }

    }

    private suspend fun getPatientDemographics() {
        try {
            val resource = patientRepository.getPatientById(_state.value.patientId).first {
                it is Resource.Success || it is Resource.Error
            }

            when (resource) {
                is Resource.Success -> {
                    _state.update { it.copy(patient = resource.data) }
                }

                is Resource.Error -> {
                    _state.update { it.copy(currentVisitRelatedError = resource.message) }
                }

                else -> Unit
            }
        } catch (e: Exception) {
            _state.update { it.copy(currentVisitRelatedError = e.message) }
        }
    }

    private suspend fun getCurrentVisit() {
        try {
            val visit = getCurrentVisitUseCase(_state.value.patientId).firstNullableSuccess()
            _state.update { it.copy(currentVisit = visit) }
        } catch (e: Exception) {
            _state.update { it.copy(currentVisitRelatedError = e.message) }
        }
    }

    fun onEvent(event: PatientDetailsEvent){

        viewModelScope.launch(dispatcher + errorHandler){
            when (event) {
                PatientDetailsEvent.OnRetryPatientDetails -> getPatientDemographics()
                PatientDetailsEvent.OnRetryCurrentVisitRelated -> getComplaintsSummaries()
            }
        }

    }
}