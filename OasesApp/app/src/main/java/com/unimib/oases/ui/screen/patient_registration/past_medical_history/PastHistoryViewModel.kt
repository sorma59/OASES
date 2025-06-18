package com.unimib.oases.ui.screen.patient_registration.past_medical_history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.DiseaseUseCase
import com.unimib.oases.domain.usecase.PatientDiseaseUseCase
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
class PastHistoryViewModel @Inject constructor(
    private val diseaseUseCases: DiseaseUseCase,
    private val patientDiseaseUseCases: PatientDiseaseUseCase,
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

    private val _state = MutableStateFlow(PastHistoryState())
    val state: StateFlow<PastHistoryState> = _state.asStateFlow()

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
                loadDiseases()
                loadPatientDiseases(patientId)
            } else
                loadDiseases()
        }
    }

    private suspend fun loadDiseases() {

        _state.update { it.copy(isLoading = true) }

        try {
            val diseasesResource = diseaseUseCases.getDiseases().first {
                it is Resource.Success || it is Resource.Error
            }

            if (diseasesResource is Resource.Success) {
                val diseasesData = diseasesResource.data

                if (diseasesData != null) {
                    val newStates = diseasesData.map { disease -> // More efficient mapping
                        PatientDiseaseState(disease.name)
                    }
                    _state.update {
                        it.copy(
                            diseases = newStates, // Set the list, don't append if it's initial load
                            error = null // Clear previous error
                        )
                    }
                } else {
                    // Handle case where Resource.Success has null data, if possible
                    _state.update { it.copy(error = "Successful fetch but no data.") }
                }
            } else if (diseasesResource is Resource.Error) {
                _state.update { it.copy(error = diseasesResource.message) }
            }
        } catch (e: Exception){
            // This can catch NoSuchElementException if the Flow completes
            // or any other exception from the Flow upstream or the first() operator itself.
            _state.update { it.copy(error = e.message ?: "An unexpected error occurred") }
        } finally {
            _state.update { it.copy(isLoading = false) } // Ensure isLoading is false in all cases
        }
    }

    private suspend fun loadPatientDiseases(patientId: String) {

        _state.update { it.copy(isLoading = true) }

        try {
            val patientDiseases = patientDiseaseUseCases.getPatientDiseases(patientId).first {
                it is Resource.Success || it is Resource.Error
            }

            if (patientDiseases is Resource.Success) {

                val patientDiseasesFromDb = patientDiseases.data!!
                val patientDiseasesDbMap = patientDiseasesFromDb.associateBy { it.diseaseName }

                _state.update { currentState ->

                    val updatedDiseasesList =
                        currentState.diseases.map { uiState -> // uiState is PatientDiseaseState

                            val patientSpecificDiseaseData = patientDiseasesDbMap[uiState.disease]

                            if (patientSpecificDiseaseData != null) {
                                // This disease from the UI list is also a disease recorded for the patient
                                uiState.copy(
                                    isChecked = true,
                                    additionalInfo = patientSpecificDiseaseData.additionalInfo,
                                    date = patientSpecificDiseaseData.diagnosisDate
                                )
                            } else {
                                // This disease from the UI list is NOT recorded for this specific patient in the DB.
                                // Reset its patient-specific details or return it as is if its default is unchecked.
                                uiState
                            }
                        }
                    currentState.copy(diseases = updatedDiseasesList, isLoading = false)
                }
            } else if (patientDiseases is Resource.Error) {
                _state.update { it.copy(error = patientDiseases.message) }
            }
        } catch (e: Exception) {
            // This can catch NoSuchElementException if the Flow completes
            // or any other exception from the Flow upstream or the first() operator itself.
            _state.update { it.copy(error = e.message ?: "An unexpected error occurred") }
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: PastHistoryEvent) {
        when (event) {

            is PastHistoryEvent.CheckChanged -> {
                _state.update {
                    it.copy(
                        diseases = it.diseases.map {
                            if (it.disease == event.disease) {
                                it.copy(isChecked = !it.isChecked)
                            } else {
                                it
                            }
                        }
                    )
                }
            }

            is PastHistoryEvent.AdditionalInfoChanged -> {
                _state.update {
                    it.copy(
                        diseases = it.diseases.map {
                            if (it.disease == event.disease) {
                                it.copy(additionalInfo = event.additionalInfo)
                            } else {
                                it
                            }
                        }
                    )
                }
            }

            is PastHistoryEvent.DateChanged -> {
                _state.update {
                    it.copy(
                        diseases = it.diseases.map {
                            if (it.disease == event.disease) {
                                it.copy(date = event.date)
                            } else {
                                it
                            }
                        }
                    )
                }
            }

            is PastHistoryEvent.Retry -> {
                refresh()
            }
        }
    }
}