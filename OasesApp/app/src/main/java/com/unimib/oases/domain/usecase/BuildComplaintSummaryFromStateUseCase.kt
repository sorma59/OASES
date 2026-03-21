package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.screen.medical_visit.initial_medical_evaluation.EvaluationState
import com.unimib.oases.ui.screen.medical_visit.initial_medical_evaluation.ImmediateTreatmentQuestionState
import javax.inject.Inject

class BuildComplaintSummaryFromStateUseCase @Inject constructor(
    private val convertTreeBranchesToTextUseCase: ConvertTreeBranchesToTextUseCase
) {

    operator fun invoke(state: EvaluationState): ComplaintSummary {
        check(state.immediateTreatments.all { it != null }){
            "Answers are incomplete"
        }
        check(state.supportiveTherapies != null)

        return ComplaintSummary(
            visitId = state.visitId,
            complaintId = state.complaintId,
            algorithmsQuestionsAndAnswers = convertTreeBranchesToTextUseCase(state.immediateTreatmentQuestions),
            symptoms = state.symptoms + state.immediateTreatmentQuestions.extractSymptomsFromAlgorithmQuestions(),
            tests = state.requestedTests,
            immediateTreatments = state.immediateTreatments.mapNotNull{it}.toSet(),
            supportiveTherapies = state.supportiveTherapies.map { it.therapy }.toSet(),
            additionalTests = state.additionalTestsText
        )
    }

    private fun List<List<ImmediateTreatmentQuestionState>>.extractSymptomsFromAlgorithmQuestions(): Set<Symptom> {
        return this.flatten()
            .filter { it.answer == true }
            .flatMap { it.node.symptoms }
            .toSet()
    }
}