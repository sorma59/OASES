package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.complaints.ComplaintId
import com.unimib.oases.domain.model.complaints.Diarrhea
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.AnswerTreatmentPlanQuestionUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
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
        getPatientData()

        handleComplaint()
    }

    private fun handleComplaint() {
        when (_state.value.complaintId) {
            ComplaintId.DIARRHEA.id -> {
                handleDiarrhea()
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

    private fun handleDiarrhea() {
        viewModelScope.launch(dispatcher) {

            delay(500) // let the patient be found

            val patient = _state.value.patient
            patient?.let {
                val age = it.ageInMonths / 12

                val diarrhea = Diarrhea(age)

                _state.update { it.copy(questions = listOf(QuestionState(diarrhea.tree.root))) }
            } ?: run {
                updateError("Patient not found")
            }
        }
    }

    private fun getPatientData() {

        viewModelScope.launch(dispatcher + errorHandler) {
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

    fun onEvent(event: MainComplaintEvent){
        when(event){
            is MainComplaintEvent.NodeAnswered -> {
                _state.update {
                    answerTreatmentPlanQuestionUseCase(event.answer, event.node, it)
                }
            }

            MainComplaintEvent.ToastShown -> {
                _state.update {
                    it.copy(
                        toastMessage = null
                    )
                }
            }
        }
    }
}

