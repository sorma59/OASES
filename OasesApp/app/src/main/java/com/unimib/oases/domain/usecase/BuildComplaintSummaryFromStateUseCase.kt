package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.ComplaintSummary
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

        check(visit != null){
            "No current visit found for patient"
        }

        return ComplaintSummary(
            visitId = visit.id,
            complaintId = state.complaintId,
            algorithmsQuestionsAndAnswers = convertTreeBranchesToTextUseCase(state.immediateTreatmentQuestions),
            symptoms = state.symptoms,
            tests = state.requestedTests,
            immediateTreatments = state.immediateTreatments.mapNotNull{it}.toSet(),
            supportiveTherapies = state.supportiveTherapies.map { it.therapy }.toSet(),
            additionalTests = state.additionalTestsText
        )
    }
}