package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintState
import com.unimib.oases.util.Resource
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

        val visitResource = getCurrentVisitUseCase(state.patientId)

        check(visitResource is Resource.Success){
            "Error during during retrieval of the patient's current visit"
        }

        val visit = visitResource.data

        check(visit != null){
            "This patient does not have an open visit"
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