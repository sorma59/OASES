package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.Diarrhea
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.AnswerTreatmentPlanQuestionUseCase
import com.unimib.oases.util.Resource
import com.unimib.oases.util.toggle
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
class MainComplaintViewModel @Inject constructor(
    private val answerTreatmentPlanQuestionUseCase: AnswerTreatmentPlanQuestionUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val patientRepository: PatientRepository
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
        MainComplaintState(
            receivedId = savedStateHandle["patientId"]!!,
            complaintId = savedStateHandle["complaintId"]!!
        )
    )
    val state: StateFlow<MainComplaintState> = _state.asStateFlow()

    init {
        initialize()
    }

    private fun initialize(){
        viewModelScope.launch(dispatcher + errorHandler){
            updateError(null)
            getPatientData()
            getComplaint()
            handleComplaint()
        }
    }

    private fun getComplaint() {

        val patient = _state.value.patient
        patient?.let {
            val age = it.ageInMonths / 12

            when (_state.value.complaintId) {
                ComplaintId.DIARRHEA.id -> {
                    _state.update{
                        it.copy(complaint = Diarrhea(age))
                    }
                }

                else -> {
                    updateError("Complaint data not found")
                }
            }

        } ?: run {
            updateError("Patient not found")
        }

    }

    private fun handleComplaint() {

        when (_state.value.complaintId) {
            ComplaintId.DIARRHEA.id -> {
                showFirstQuestion((_state.value.complaint!! as Diarrhea).tree.root)
            }

            else -> {
                updateError("Complaint data not found")
            }
        }

    }

    private fun updateError(error: String?) {
        _state.update {
            it.copy(
                error = error
            )
        }
    }

    private fun showFirstQuestion(firstNode: ManualNode) {
        _state.update {
            it.copy(
                treatmentPlanQuestions = listOf(
                    TreatmentPlanQuestionState(firstNode)
                )
            )
        }
    }

    private suspend fun getPatientData() {

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

    fun onEvent(event: MainComplaintEvent){
        when(event){
            is MainComplaintEvent.NodeAnswered -> {
                _state.update {
                    answerTreatmentPlanQuestionUseCase(event.answer, event.node, it)
                }
            }

            is MainComplaintEvent.SymptomSelected -> {
                _state.update {
                    it.copy(
                        symptoms = it.symptoms.toggle(event.symptom)
                    )
                }
            }

            is MainComplaintEvent.DurationSelected -> {
                _state.update {
                    it.copy(
                        durationOption = event.duration
                    )
                }
            }

            is MainComplaintEvent.FrequencySelected -> {
                _state.update {
                    it.copy(
                        frequencyOption = event.frequency
                    )
                }
            }

            is MainComplaintEvent.AspectSelected -> {
                _state.update {
                    it.copy(
                        aspectOption = event.aspect
                    )
                }
            }

            MainComplaintEvent.ToastShown -> {
                _state.update {
                    it.copy(
                        toastMessage = null
                    )
                }
            }

            MainComplaintEvent.RetryButtonClicked -> {
                initialize()
            }
        }
    }
}

