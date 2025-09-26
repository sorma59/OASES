package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.Diarrhea
import com.unimib.oases.domain.model.complaint.Dyspnea
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.usecase.AnswerImmediateTreatmentQuestionUseCase
import com.unimib.oases.domain.usecase.GenerateSuggestedSupportiveTherapiesUseCase
import com.unimib.oases.domain.usecase.GenerateSuggestedTestsUseCase
import com.unimib.oases.domain.usecase.SelectSymptomUseCase
import com.unimib.oases.domain.usecase.TranslateTriageSymptomIdsToSymptomsUseCase
import com.unimib.oases.domain.usecase.VisitUseCase
import com.unimib.oases.util.Resource
import com.unimib.oases.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainComplaintViewModel @Inject constructor(
    private val answerImmediateTreatmentQuestionUseCase: AnswerImmediateTreatmentQuestionUseCase,
    private val generateSuggestedTestsUseCase: GenerateSuggestedTestsUseCase,
    private val generateSuggestedSupportiveTherapiesUseCase: GenerateSuggestedSupportiveTherapiesUseCase,
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
            patientId = savedStateHandle["patientId"]!!,
            complaintId = savedStateHandle["complaintId"]!!
        )
    )
    val state: StateFlow<MainComplaintState> = _state.asStateFlow()

    val additionalTestsText: StateFlow<String> =
        _state
            .map { it.additionalTestsText }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ""
            )

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

                ComplaintId.DYSPNEA.id -> {
                    _state.update{
                        it.copy(
                            complaint = Dyspnea
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
        setAlgorithms()
        showFirstAlgorithm()
        showFirstQuestion(_state.value.complaint!!.immediateTreatmentTrees.first().root)
    }

    private fun setAlgorithms() {
        _state.update {
            val algorithms = it.complaint!!.immediateTreatmentTrees
            it.copy(
                immediateTreatmentAlgorithms = algorithms,
                leaves = List(algorithms.size){ null }
            )
        }
    }

    private fun showFirstAlgorithm() {
        _state.update {
            it.copy(
                immediateTreatmentAlgorithmsToShow = 1
            )
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
                immediateTreatmentQuestions = listOf(
                    listOf(
                        ImmediateTreatmentQuestionState(firstNode)
                    )
                )
            )
        }
    }

    private suspend fun getPatientData() {

        try {

            val resource = patientRepository.getPatientById(_state.value.patientId).first {
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

            val visit = visitUseCase.getCurrentVisit(_state.value.patientId)

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
                    answerImmediateTreatmentQuestionUseCase(event.answer, event.node, event.tree, it)
                }
            }

            is MainComplaintEvent.SymptomSelected -> {
                _state.update {
                    it.copy(
                        symptoms = selectSymptomUseCase(event.symptom, it.symptoms),
                        detailsQuestionsToShow = calculateNumberOfDetailsQuestionsToShow(event.question)
                    )
                }
            }

            is MainComplaintEvent.TestSelected -> {
                _state.update {
                    it.copy(
                        requestedTests = it.requestedTests.toggle(event.test)
                    )
                }
            }

            is MainComplaintEvent.AdditionalTestsTextChanged -> {
                _state.update {
                    it.copy(
                        additionalTestsText = event.text
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
                        conditions = generateSuggestedTestsUseCase(it.complaint!!, it.symptoms),
                        supportiveTherapies = generateSuggestedSupportiveTherapiesUseCase(it.complaint, it.symptoms),
                        shouldShowSubmitButton = true
                    )
                }
            }

            MainComplaintEvent.SubmitPressed -> {
                _state.update {
                    it.copy(
                        toastMessage = "Complaint submitted"
                    )
                }
            }
        }
    }

    /**
     * Calculates the number of details questions to show.
     *
     * The logic is as follows:
     * - If the answered question is not required, the number of questions to show does not change.
     * - If the answered question is required:
     *     - If the answered question is not the last one currently shown, the number of questions to show does not change.
     *     - If the answered question is the last one currently shown:
     *         - If the next question in the list is required, increment the number of questions to show by 1.
     *         - If the next question in the list is not required, show all questions. This assumes that all required questions are listed before non-required ones.
     *
     * @param question The [ComplaintQuestion] that was just answered.
     * @return The updated number of details questions to be shown.
     */
    private fun calculateNumberOfDetailsQuestionsToShow(question: ComplaintQuestion): Int {
        return if (!question.isRequired)
            state.value.detailsQuestionsToShow
        else {
            val questionOrdinal = state.value.detailsQuestions.indexOf(question) + 1
            if (questionOrdinal != state.value.detailsQuestionsToShow)
                state.value.detailsQuestionsToShow
            else {
                // The next question happens to be at the index "questionOrdinal"
                // Reason being the ordinal is index of the question + 1
                val nextQuestion = state.value.detailsQuestions.elementAt(questionOrdinal)
                if (nextQuestion.isRequired)
                    state.value.detailsQuestionsToShow + 1
                else
                    state.value.detailsQuestions.size
            }
        }
    }
}

