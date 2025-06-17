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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PastHistoryViewModel @Inject constructor(
    private val diseaseUseCases: DiseaseUseCase,
    private val patientDiseaseUseCases: PatientDiseaseUseCase,
    savedStateHandle: SavedStateHandle,
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
        loadDiseases()
        savedStateHandle.get<String>("patientId")?.let { id ->
            loadPatientDiseases(id)
        }
    }

    private fun loadDiseases() {
        viewModelScope.launch(ioDispatcher + errorHandler) {

            _state.update { it.copy(isLoading = true) }

            diseaseUseCases.getDiseases().collect { diseases ->

                if (diseases is Resource.Success){

                    for (disease in diseases.data!!){
                        _state.update {
                            it.copy(
                                diseases = it.diseases + PatientDiseaseState(disease.name)
                            )
                        }
                    }

                } else if (diseases is Resource.Error){
                    _state.update { it.copy(error = diseases.message) }
                }

                _state.update { it.copy(isLoading = false) }
            }

        }
    }

    private fun loadPatientDiseases(patientId: String) {
        viewModelScope.launch(ioDispatcher + errorHandler) {
            _state.update { it.copy(isLoading = true) } // You might want to move this isLoading update earlier

            patientDiseaseUseCases.getPatientDiseases(patientId).collect { patientDiseasesResource ->
                if (patientDiseasesResource is Resource.Success) {
                    val patientDiseasesFromDb = patientDiseasesResource.data!! // List<com.unimib.oases.domain.model.PatientDisease>
                    val patientDiseaseDbMap = patientDiseasesFromDb.associateBy { it.diseaseName }

                    _state.update { currentState ->
                        val updatedDiseasesList = currentState.diseases.map { existingUiDiseaseState -> // existingUiDiseaseState is PatientDiseaseState
                            val patientSpecificDiseaseData = patientDiseaseDbMap[existingUiDiseaseState.disease]

                            if (patientSpecificDiseaseData != null) {
                                // This disease from the UI list is also a disease recorded for the patient
                                existingUiDiseaseState.copy(
                                    isChecked = true,
                                    additionalInfo = patientSpecificDiseaseData.additionalInfo,
                                    date = patientSpecificDiseaseData.diagnosisDate
                                )
                            } else {
                                // This disease from the UI list is NOT recorded for this specific patient in the DB.
                                // Reset its patient-specific details or return it as is if its default is unchecked.
                                existingUiDiseaseState
                            }
                        }
                        currentState.copy(diseases = updatedDiseasesList, isLoading = false) // Update isLoading here too
                    }
                } else if (patientDiseasesResource is Resource.Error) {
                    _state.update {
                        it.copy(
                            error = patientDiseasesResource.message,
                            isLoading = false
                        )
                    }
                }
                 _state.update { it.copy(isLoading = false) }
            }
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

        }
    }
}