package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.Diarrhea
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.usecase.AnswerTreatmentPlanQuestionUseCase
import com.unimib.oases.domain.usecase.GenerateSuggestedTestsUseCase
import com.unimib.oases.domain.usecase.SelectSymptomUseCase
import com.unimib.oases.domain.usecase.TranslateTriageSymptomIdsToSymptomsUseCase
import com.unimib.oases.domain.usecase.VisitUseCase
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
class MainComplaintViewModel @Inject constructor(
    private val answerTreatmentPlanQuestionUseCase: AnswerTreatmentPlanQuestionUseCase,
    private val generateSuggestedTestsUseCase: GenerateSuggestedTestsUseCase,
    private val translateTriageSymptomIdsToSymptomsUseCase: TranslateTriageSymptomIdsToSymptomsUseCase,
    private val selectSymptomUseCase: SelectSymptomUseCase,
    private val visitUseCase: VisitUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val patientRepository: PatientRepository,
    private val triageEvaluationRepository: TriageEvaluationRepository
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
            updateLoading(true)
            getPatientData()
            getTriageData()
            getComplaint()
            handleComplaint()
            updateLoading(false)
        }
    }

    private fun getComplaint() {

        val patient = _state.value.patient
        patient?.let {
            val age = it.ageInMonths / 12

            when (_state.value.complaintId) {
                ComplaintId.DIARRHEA.id -> {
                    _state.update{
                        it.copy(
                            complaint = Diarrhea(age)
                        )
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

    private fun updateLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                isLoading = isLoading
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

            val resource = patientRepository.getPatientById(_state.value.receivedId).first {
                it is Resource.Success || it is Resource.Error
            }

            when (resource) {
                is Resource.Success -> {
                    _state.update { it.copy(patient = resource.data) }
                }
                is Resource.Error -> {
                    updateError(resource.message)
                }
                else -> Unit
            }
        } catch (e: Exception) {
            updateError(e.message ?: "Unknown error")
        }
    }

    private fun getTriageData(){
        try {

            val visit = visitUseCase.getCurrentVisit(_state.value.receivedId)

            visit?.let {
                val resource = triageEvaluationRepository.getTriageEvaluation(visit.id)

                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let {
                            val ids = it.redSymptomIds + it.yellowSymptomIds
                            _state.update {
                                it.copy(
                                    symptoms = translateTriageSymptomIdsToSymptomsUseCase(ids)
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _state.update { it.copy(error = resource.message) }
                    }

                    else -> Unit
                }
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
                        symptoms = selectSymptomUseCase(event.symptom, it.symptoms)
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

            MainComplaintEvent.GenerateTestsPressed -> {
                _state.update {
                    it.copy(
                        conditions = generateSuggestedTestsUseCase(complaint = it.complaint!!, symptoms = it.symptoms)
                    )
                }
            }
        }
    }
}

