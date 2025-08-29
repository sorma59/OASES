package com.unimib.oases.ui.screen.dashboard.patient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDashboardViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel(){

    private val _state = MutableStateFlow(PatientDashboardState())
    val state: StateFlow<PatientDashboardState> = _state

    init {
        getPatientFromSSH()
    }

    private fun getPatientFromSSH() {
        val patientId = savedStateHandle.get<String>("patientId")
        _state.update { it.copy(receivedId = patientId) }
        if (patientId == null)
            return

        viewModelScope.launch(dispatcher) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val resource = patientRepository.getPatientById(patientId).first {
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
            is PatientDashboardEvent.ActionButtonClicked -> {}
            PatientDashboardEvent.PatientItemClicked -> {
                if (_state.value.patient == null)
                    getPatientFromSSH()
            }
        }

    }
}