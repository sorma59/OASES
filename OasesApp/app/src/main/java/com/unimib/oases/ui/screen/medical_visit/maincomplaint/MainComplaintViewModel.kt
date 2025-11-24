package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.Diarrhea
import com.unimib.oases.domain.model.complaint.Dyspnea
import com.unimib.oases.domain.model.complaint.SeizuresOrComa
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.usecase.AnswerImmediateTreatmentQuestionUseCase
import com.unimib.oases.domain.usecase.GenerateSuggestedSupportiveTherapiesUseCase
import com.unimib.oases.domain.usecase.GenerateSuggestedTestsUseCase
import com.unimib.oases.domain.usecase.GetCurrentVisitUseCase
import com.unimib.oases.domain.usecase.GetPatientCategoryUseCase
import com.unimib.oases.domain.usecase.SelectSymptomUseCase
import com.unimib.oases.domain.usecase.SubmitMedicalVisitPartOneUseCase
import com.unimib.oases.domain.usecase.TranslateLatestVitalSignsToSymptomsUseCase
import com.unimib.oases.domain.usecase.TranslateTriageSymptomIdsToSymptomsUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
import com.unimib.oases.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainComplaintViewModel @Inject constructor(
    private val answerImmediateTreatmentQuestionUseCase: AnswerImmediateTreatmentQuestionUseCase,
    private val submitMedicalVisitPartOneUseCase: SubmitMedicalVisitPartOneUseCase,
    private val generateSuggestedTestsUseCase: GenerateSuggestedTestsUseCase,
    private val generateSuggestedSupportiveTherapiesUseCase: GenerateSuggestedSupportiveTherapiesUseCase,
    private val getPatientCategoryUseCase: GetPatientCategoryUseCase,
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val translateTriageSymptomIdsToSymptomsUseCase: TranslateTriageSymptomIdsToSymptomsUseCase,
    private val translateLatestVitalSignsToSymptomsUseCase: TranslateLatestVitalSignsToSymptomsUseCase,
    private val selectSymptomUseCase: SelectSymptomUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val patientRepository: PatientRepository,
    private val triageEvaluationRepository: TriageEvaluationRepository
): ViewModel() {

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
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

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    val additionalTestsText: StateFlow<String> = _state
        .map { it.additionalTestsText }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    init {
        initialize()
    }

    private fun initialize(shouldRebuildTree: Boolean = true){
        viewModelScope.launch(dispatcher + errorHandler){
            updateError(null)
            updateLoading(true)
            getPatientData()
            getTriageData()
            getVitalSignsData()
            if (shouldRebuildTree){
                getComplaint()
                handleComplaint()
            }
            updateLoading(false)
        }
    }

    private fun getComplaint() {

        val patient = state.value.patient
        patient?.let { patient ->
            val age = patient.ageInMonths / 12
            val sex = patient.sex
            val patientCategory = getPatientCategoryUseCase(patient.ageInMonths)

            when (state.value.complaintId) {
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

                ComplaintId.SEIZURES_OR_COMA.id -> {
                    _state.update{
                        it.copy(
                            complaint = SeizuresOrComa(sex, patientCategory)
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
        showFirstQuestion(state.value.complaint!!.immediateTreatmentTrees.first().root)
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
            val patient = patientRepository
                .getPatientById(state.value.patientId)
                .firstSuccess()
            _state.update {
                it.copy(
                    patient = patient
                )
            }
        } catch (e: Exception) {
            updateError(e.message ?: "Unknown error")
        }
    }

    private suspend fun getTriageData(){
        try {

            val visit = getCurrentVisitUseCase(state.value.patientId).firstNullableSuccess()

            visit?.let {
                val triageEvaluation = triageEvaluationRepository
                    .getTriageEvaluation(visit.id)
                    .firstSuccess()

                val ids = triageEvaluation.redSymptomIds + triageEvaluation.yellowSymptomIds
                _state.update {
                    it.copy(
                        symptoms = it.symptoms + translateTriageSymptomIdsToSymptomsUseCase(ids)
                    )
                }
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun getVitalSignsData(){
        try {
            _state.update {
                it.copy(
                    symptoms = _state.value.symptoms + translateLatestVitalSignsToSymptomsUseCase(state.value.patientId)
                )
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
                        symptoms = selectSymptomUseCase(event.symptom, it.symptoms, event.question),
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
                initialize(false)
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
                viewModelScope.launch(dispatcher + errorHandler){
                    when(val result = submitMedicalVisitPartOneUseCase(state.value)){
                        is Outcome.Error -> {
                            _state.update {
                                it.copy(
                                    error = result.message
                                )
                            }
                        }
                        is Outcome.Success -> {
                            _state.update {
                                it.copy(
                                    toastMessage = "Medical visit submitted successfully"
                                )
                            }
                            patientRepository.addPatient(state.value.patient!!)
                            navigationEventsChannel.send(NavigationEvent.NavigateBack)
                        }

                        else -> Unit
                    }
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

