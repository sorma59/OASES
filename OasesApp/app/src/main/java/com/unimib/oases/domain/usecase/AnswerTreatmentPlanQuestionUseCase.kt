package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.complaint.binarytree.LeafNode
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.next
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintState
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.TreatmentPlanQuestionState
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.rebranch
import javax.inject.Inject

class AnswerTreatmentPlanQuestionUseCase @Inject constructor() {
    operator fun invoke(answer: Boolean, node: ManualNode, state: MainComplaintState): MainComplaintState{
        val nextNode = node.next(answer)

        return when (nextNode){
            is LeafNode -> {
                state.copy(
                    treatmentPlanQuestions = state.treatmentPlanQuestions.rebranch(node, answer),
                    leaf = nextNode,
                    detailsQuestions = getDetailsQuestion(state)
                )
            }
            is ManualNode -> {
                state.copy(
                    treatmentPlanQuestions =
                        state.treatmentPlanQuestions.rebranch(node, answer) +
                        TreatmentPlanQuestionState(nextNode),
                    leaf = null
                )
            }
        }
    }

    private fun getDetailsQuestion(state: MainComplaintState) = state.complaint!!.details.questions
}