package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.ImmediateTreatmentQuestionState
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintState
import javax.inject.Inject

class BuildComplaintSummaryFromStateUseCase @Inject constructor(
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val convertTreeBranchesToTextUseCase: ConvertTreeBranchesToTextUseCase
) {

    suspend operator fun invoke(state: MainComplaintState): ComplaintSummary {
        check(state.immediateTreatments.all { it != null }){
            "Answers are incomplete"
        }
        check(state.supportiveTherapies != null)

        val visit = getCurrentVisitUseCase(state.patientId)

        return ComplaintSummary(
            visitId = visit.id,
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