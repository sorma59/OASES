package com.unimib.oases.ui.screen.medical_visit.evaluation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.data.local.model.TreeAnswers
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.binarytree.LeafNode
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.ShowableNode
import com.unimib.oases.domain.model.complaint.binarytree.next
import com.unimib.oases.domain.repository.EvaluationRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.usecase.AnswerImmediateTreatmentQuestionUseCase
import com.unimib.oases.domain.usecase.GenerateSuggestedSupportiveTherapiesUseCase
import com.unimib.oases.domain.usecase.GenerateSuggestedTestsUseCase
import com.unimib.oases.domain.usecase.SelectSymptomUseCase
import com.unimib.oases.domain.usecase.SubmitMedicalVisitPartOneUseCase
import com.unimib.oases.domain.usecase.TranslateLatestVitalSignsToSymptomsUseCase
import com.unimib.oases.domain.usecase.TranslateTriageSymptomIdsToSymptomsUseCase
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.util.snackbar.SnackbarData
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
import com.unimib.oases.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
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
class EvaluationViewModel @Inject constructor(
    private val answerImmediateTreatmentQuestionUseCase: AnswerImmediateTreatmentQuestionUseCase,
    private val submitMedicalVisitPartOneUseCase: SubmitMedicalVisitPartOneUseCase,
    private val generateSuggestedTestsUseCase: GenerateSuggestedTestsUseCase,
    private val generateSuggestedSupportiveTherapiesUseCase: GenerateSuggestedSupportiveTherapiesUseCase,
    private val translateTriageSymptomIdsToSymptomsUseCase: TranslateTriageSymptomIdsToSymptomsUseCase,
    private val translateLatestVitalSignsToSymptomsUseCase: TranslateLatestVitalSignsToSymptomsUseCase,
    private val selectSymptomUseCase: SelectSymptomUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val patientRepository: PatientRepository,
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val evaluationRepository: EvaluationRepository,
): ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message,
                isLoading = false,
            )
        }
    }

    private val args = savedStateHandle.toRoute<Route.Evaluation>()

    private val _state = MutableStateFlow(
        EvaluationState(
            patientId = args.patientId,
            visitId = args.visitId,
            complaintId = args.complaintId
        )
    )
    val state: StateFlow<EvaluationState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

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

    private fun initialize(){
        viewModelScope.launch(dispatcher + errorHandler){

            _state.update {
                it.copy(
                    error = null,
                    isLoading = true,
                )
            }

            val evaluationDeferred = async {
                evaluationRepository
                    .getEvaluation(
                        visitId = state.value.visitId,
                        complaintId = state.value.complaintId,
                    )
                    .firstNullableSuccess()
            }

            val patientDeferred = async {
                patientRepository
                    .getPatientById(patientId = state.value.patientId)
                    .firstSuccess()
            }

            val triageEvaluationDeferred = async {
                triageEvaluationRepository
                    .getTriageEvaluation(visitId = state.value.visitId)
                    .firstSuccess()
            }

            val vitalSignsDeferred = async {
                translateLatestVitalSignsToSymptomsUseCase(patientId = state.value.patientId)
            }

            val evaluation = evaluationDeferred.await()
            val patient = patientDeferred.await()
            val triageEvaluation = triageEvaluationDeferred.await()
            val vitalSignsSymptomsIds = vitalSignsDeferred.await()

            _state.update {
                it.copy(
                    patient = patient,
                    complaint = Complaint.getComplaint(
                        state.value.complaintId,
                        patient.ageInMonths,
                        patient.sex,
                        patient.category
                    ),
                )
            }

            val triageSymptomsIds = translateTriageSymptomIdsToSymptomsUseCase(
                triageEvaluation.symptomsIds
            )

            if (evaluation == null) {
                _state.update {
                    it.copy(
                        symptoms = triageSymptomsIds + vitalSignsSymptomsIds
                    )
                }
                handleComplaint()
            } else {
                _state.update {
                    evaluation.toEvaluationState()
                }
            }

            stopLoading()
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

    private fun stopLoading() {
        _state.update {
            it.copy(
                isLoading = false
            )
        }
    }

    private fun showFirstQuestion(firstNode: ManualNode) {
        _state.update {
            checkNotNull(it.complaint) {
                "Complaint was not found"
            }
            it.copy(
                immediateTreatmentQuestions = listOf(
                    TreeSummary(
                        treeId = it.complaint.immediateTreatmentTrees[0].id.value,
                        answers = listOf(
                            ImmediateTreatmentQuestionState(firstNode)
                        )
                    )
                )
            )
        }
    }

    fun onEvent(event: EvaluationEvent){
        when(event){
            is EvaluationEvent.NodeAnswered -> {
                _state.update {
                    answerImmediateTreatmentQuestionUseCase(event.answer, event.node, event.tree, it)
                }
            }

            is EvaluationEvent.SymptomSelected -> {
                _state.update {
                    it.copy(
                        symptoms = selectSymptomUseCase(event.symptom, it.symptoms, event.question),
                        detailsQuestionsToShow = calculateNumberOfDetailsQuestionsToShow(event.question)
                    )
                }
            }

            is EvaluationEvent.TestSelected -> {
                _state.update {
                    it.copy(
                        requestedTests = it.requestedTests.toggle(event.test)
                    )
                }
            }

            is EvaluationEvent.AdditionalTestsTextChanged -> {
                _state.update {
                    it.copy(
                        additionalTestsText = event.text
                    )
                }
            }

            EvaluationEvent.RetryButtonClicked -> {
                initialize()
            }

            EvaluationEvent.GenerateTestsPressed -> {
                _state.update {
                    it.copy(
                        conditions = generateSuggestedTestsUseCase(it.complaint!!, it.symptoms),
                        supportiveTherapies = generateSuggestedSupportiveTherapiesUseCase(it.complaint, it.symptoms),
                        wereTestsGenerated = true,
                    )
                }
            }

            EvaluationEvent.SubmitPressed -> {
                viewModelScope.launch(dispatcher + errorHandler){
                    when(val result = submitMedicalVisitPartOneUseCase(state.value)) {
                        is Outcome.Error -> {
                            _state.update {
                                it.copy(
                                    error = result.message
                                )
                            }
                        }
                        is Outcome.Success -> {
                            uiEventsChannel.send(
                                UiEvent.ShowSnackbar(
                                    SnackbarData.SaveSuccess
                                )
                            )
                            navigationEventsChannel.send(
                                NavigationEvent.PopUpTo(
                                    Route.MedicalVisit(
                                        patientId = state.value.patientId,
                                        visitId = state.value.visitId,
                                    )
                                )
                            )
                        }
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

    fun Evaluation.toEvaluationState(): EvaluationState {

        val complaint = state.value.complaint ?: error("Complaint id $complaintId not found")
        
        // replay tree paths to reconstruct question states
        val immediateTreatmentQuestions = treeAnswers.map { treeAnswer ->
            val tree = complaint.immediateTreatmentTrees.first { it.id.value == treeAnswer.treeId }
            replayTreePath(tree.root, treeAnswer)
        }

        val detailQuestions = complaint.details.questions

        return state.value.copy(
            immediateTreatmentAlgorithms = complaint.immediateTreatmentTrees,
            immediateTreatmentAlgorithmsToShow = complaint.immediateTreatmentTrees.size,
            immediateTreatmentQuestions = immediateTreatmentQuestions,
            leaves = immediateTreatmentQuestions.map { treeSummary ->
                treeSummary.answers.lastOrNull()?.let { last ->
                    last.node.children.let { children ->
                        val answer = last.answer ?: return@let null
                        if (answer) children.left else children.right
                    } as? LeafNode
                }
            },
            detailsQuestions = detailQuestions,
            detailsQuestionsToShow = detailQuestions.size,
            symptoms = symptoms,
            requestedTests = emptySet(), // must fill in again
            additionalTestsText = additionalTestsText,
            conditions = complaint.tests.conditions.filter { it.predicate(symptoms) },
        )
    }

    private fun replayTreePath(
        root: ManualNode,
        treeAnswers: TreeAnswers,
    ): TreeSummary {
        val result = mutableListOf<ImmediateTreatmentQuestionState>()
        var current: ShowableNode = root

        for (answer in treeAnswers.path) {
            if (current !is ManualNode) break
            result.add(ImmediateTreatmentQuestionState(node = current, answer = answer))
            current = current.next(answer)
        }

        // add the last node without an answer (the one currently shown)
        if (current is ManualNode) {
            result.add(ImmediateTreatmentQuestionState(node = current, answer = null))
        }

        return TreeSummary(
            treeId = treeAnswers.treeId,
            answers = result,
        )
    }
}

