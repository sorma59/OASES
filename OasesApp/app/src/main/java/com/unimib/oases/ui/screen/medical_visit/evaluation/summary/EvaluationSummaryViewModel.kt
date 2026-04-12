package com.unimib.oases.ui.screen.medical_visit.evaluation.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.data.local.model.TreeAnswers
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.binarytree.LeafNode
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.ShowableNode
import com.unimib.oases.domain.model.complaint.binarytree.next
import com.unimib.oases.domain.repository.EvaluationRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.medical_visit.evaluation.ImmediateTreatmentQuestionState
import com.unimib.oases.ui.screen.medical_visit.evaluation.TreeSummary
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EvaluationSummaryViewModel @Inject constructor(
    private val evaluationRepository: EvaluationRepository,
    private val patientRepository: PatientRepository,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
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

    private val mainContext = dispatcher + errorHandler

    private val args = savedStateHandle.toRoute<Route.EvaluationSummary>()

    private val _state = MutableStateFlow(
        EvaluationSummaryState(
            visitId = args.visitId,
            patientId = args.patientId,
            complaintId = args.complaintId,
        )
    )
    val state: StateFlow<EvaluationSummaryState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch(mainContext){

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


            val evaluation = evaluationDeferred.await()
            val patient = patientDeferred.await()

            _state.update {
                it.copy(
                    complaint = Complaint.getComplaint(
                        state.value.complaintId,
                        patient.ageInMonths,
                        patient.sex,
                        patient.category
                    ),
                )
            }


            if (evaluation == null) {
                navigationEventsChannel.send(
                    NavigationEvent.PopAndNavigate(
                        route = Route.Evaluation(
                            patientId = state.value.patientId,
                            visitId = state.value.visitId,
                            complaintId = state.value.complaintId,
                        )
                    )
                )
            } else {
                _state.update {
                    evaluation.toEvaluationSummaryState(it)
                }
            }

            stopLoading()
        }
    }

    private fun stopLoading() {
        _state.update {
            it.copy(
                isLoading = false
            )
        }
    }

    fun onEvent(event: EvaluationSummaryEvent) {

        when (event) {
            EvaluationSummaryEvent.EditButtonClicked -> {
                viewModelScope.launch(mainContext) {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.Evaluation(
                                patientId = state.value.patientId,
                                visitId = state.value.visitId,
                                complaintId = state.value.complaintId,
                            )
                        )
                    )
                }
            }

            EvaluationSummaryEvent.RetryButtonClicked -> {
                initialize()
            }
        }
    }

    private fun Evaluation.toEvaluationSummaryState(
        currentState: EvaluationSummaryState,
    ): EvaluationSummaryState {
        val complaint = currentState.complaint ?: error("Complaint id $complaintId not found")

        // replay tree paths to reconstruct question states
        val immediateTreatmentQuestions = treeAnswers.map { treeAnswer ->
            val tree = complaint.immediateTreatmentTrees.first { it.id.value == treeAnswer.treeId }
            replayTreePath(tree.root, treeAnswer)
        }

        val detailQuestions = complaint.details.questions

        return currentState.copy(
            immediateTreatmentAlgorithms = complaint.immediateTreatmentTrees,
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
            symptoms = symptoms,
            requestedTests = requestedTests,
            additionalTestsText = additionalTestsText,
            supportiveTherapies = supportiveTherapies.toList(),
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