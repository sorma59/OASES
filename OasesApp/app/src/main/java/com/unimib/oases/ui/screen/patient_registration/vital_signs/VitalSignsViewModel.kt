package com.unimib.oases.ui.screen.patient_registration.vital_signs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.VisitUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import com.unimib.oases.domain.usecase.VitalSignUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalSignsViewModel @Inject constructor(
    private val vitalSignsUseCases: VitalSignUseCase,
    private val visitUseCases: VisitUseCase,
    private val visitVitalSignsUseCases: VisitVitalSignsUseCase,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    private val _state = MutableStateFlow(VitalSignsState())
    val state: StateFlow<VitalSignsState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh(){
        _state.value = _state.value.copy(
            error = null,
            isLoading = true
        )
        val patientId = savedStateHandle.get<String>("patientId")
        viewModelScope.launch(ioDispatcher + errorHandler) {
            if (patientId != null) {
                val currentVisit = visitUseCases.getCurrentVisit(patientId)
                if (currentVisit != null) {
                    loadVitalSigns()
                    if (_state.value.error == null)
                        loadVisitVitalSigns(currentVisit.id)
                } else
                    loadVitalSigns()
            } else
                loadVitalSigns()
        }

    }

    private suspend fun loadVitalSigns() {

        _state.update { it.copy(isLoading = true) }

        try {
            // Collect only the first emission from the Flow
            val vitalSignsResource = vitalSignsUseCases.getVitalSigns().first {
                it is Resource.Success || it is Resource.Error
            }

            if (vitalSignsResource is Resource.Success) {

                val vitalSignsData = vitalSignsResource.data

                if (vitalSignsData != null) {
                    val newStates = vitalSignsData.map { vitalSign -> // More efficient mapping
                        PatientVitalSignState(vitalSign.name, vitalSign.acronym, vitalSign.unit)
                    }
                    _state.update {
                        it.copy(
                            vitalSigns = newStates, // Set the list, don't append if it's initial load
                            error = null // Clear previous error
                        )
                    }
                } else {
                    // Handle case where Resource.Success has null data, if possible
                    _state.update { it.copy(error = "Successful fetch but no data.") }
                }
            } else if (vitalSignsResource is Resource.Error) {
                _state.update { it.copy(error = vitalSignsResource.message) }
            }
        } catch (e: Exception) {
            // This can catch NoSuchElementException if the Flow completes
            // or any other exception from the Flow upstream or the first() operator itself.
            _state.update { it.copy(error = e.message ?: "An unexpected error occurred") }
        } finally {
            _state.update { it.copy(isLoading = false) } // Ensure isLoading is false in all cases
        }

    }

    private suspend fun loadVisitVitalSigns(visitId: String) {

        _state.update { it.copy(isLoading = true) }

        try {
            val visitVitalSigns = visitVitalSignsUseCases.getVisitVitalSigns(visitId).first {
                it is Resource.Success || it is Resource.Error
            }

            if (visitVitalSigns is Resource.Success) {

                val visitVitalSignsFromDb = visitVitalSigns.data!!
                val visitVitalSignsDbMap = visitVitalSignsFromDb.associateBy { it.vitalSignName }

                _state.update { currentState ->
                    val updatedVitalSignsList =
                        currentState.vitalSigns.map { uiState ->

                            val visitSpecificVitalSignData =visitVitalSignsDbMap[uiState.name]

                            if (visitSpecificVitalSignData != null) {
                                uiState.copy(
                                    value = visitSpecificVitalSignData.value.toString()
                                )
                            } else {
                                uiState
                            }
                        }
                    currentState.copy(vitalSigns = updatedVitalSignsList, isLoading = false)
                }
            } else if (visitVitalSigns is Resource.Error) {
                _state.update { it.copy(error = visitVitalSigns.message) }
            }
        } catch (e: Exception) {
            // This can catch NoSuchElementException if the Flow completes
            // or any other exception from the Flow upstream or the first() operator itself.
            _state.update { it.copy(error = e.message ?: "An unexpected error occurred") }
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: VitalSignsEvent) {
        when (event) {
            is VitalSignsEvent.ValueChanged -> {
                _state.update {
                    it.copy(
                        vitalSigns = it.vitalSigns.map {
                            if (it.name == event.vitalSign) {
                                it.copy(value = event.value)
                            } else {
                                it
                            }
                        }
                    )
                }
            }

            is VitalSignsEvent.Retry -> {
                refresh()
            }

            is VitalSignsEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {


    }
}
