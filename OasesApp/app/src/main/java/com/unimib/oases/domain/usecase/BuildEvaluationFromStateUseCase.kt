package com.unimib.oases.domain.usecase

import com.unimib.oases.data.local.model.DetailQuestionAnswer
import com.unimib.oases.data.local.model.TreeAnswers
import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.screen.medical_visit.evaluation.EvaluationState
import com.unimib.oases.ui.screen.medical_visit.evaluation.TreeSummary
import javax.inject.Inject

class BuildEvaluationFromStateUseCase @Inject constructor(
    private val convertTreeBranchesToTextUseCase: ConvertTreeBranchesToTextUseCase
){
    operator fun invoke(state: EvaluationState): Evaluation {
        check(state.supportiveTherapies != null)

        return Evaluation(
            visitId = state.visitId,
            complaintId = ComplaintId.complaints[state.complaintId] ?: error("Complaint id ${state.complaintId} not found"),
            algorithmsQuestionsAndAnswers = convertTreeBranchesToTextUseCase(state.immediateTreatmentQuestions),
            symptoms = state.symptoms + state.immediateTreatmentQuestions.extractSymptomsFromAlgorithmQuestions(),
            requestedTests = state.requestedTests,
            suggestedTests = state.conditions.flatMap { it.suggestedTests }.toSet(), // snapshot
            immediateTreatments = state.immediateTreatments.map { it }.toSet(),
            supportiveTherapies = state.supportiveTherapies.map { it.therapy }.toSet(),
            additionalTestsText = state.additionalTestsText,
            treeAnswers = state.immediateTreatmentQuestions.toTreeAnswers(),
            detailQuestionAnswers = state.detailsQuestions.toDetailQuestionAnswers(state.symptoms),
        )
    }

    private fun List<TreeSummary>.toTreeAnswers(): List<TreeAnswers> {
        return this.map { summary ->
            TreeAnswers(
                treeId = summary.treeId,
                path = summary.answers.map { it.answer!! }
            )
        }
    }

    private fun List<ComplaintQuestion>.toDetailQuestionAnswers(symptoms: Set<Symptom>): List<DetailQuestionAnswer> {
        return this.map { question ->
            DetailQuestionAnswer(
                question = question.question,
                answerSymptomIds = question.options
                    .filter { it in symptoms }
                    .map { it.id }
            )
        }
    }

    private fun List<TreeSummary>.extractSymptomsFromAlgorithmQuestions(): Set<Symptom> {
        return this
            .flatMap { it.answers }
            .filter { it.answer == true }
            .flatMap { it.node.symptoms }
            .toSet()
    }
}