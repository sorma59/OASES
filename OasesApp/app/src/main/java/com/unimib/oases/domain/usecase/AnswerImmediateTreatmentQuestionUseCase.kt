package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.complaint.binarytree.LeafNode
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.next
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.ImmediateTreatmentQuestionState
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintState
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.rebranch
import javax.inject.Inject

class AnswerImmediateTreatmentQuestionUseCase @Inject constructor() {
    operator fun invoke(answer: Boolean, node: ManualNode, state: MainComplaintState): MainComplaintState{
        val nextNode = node.next(answer)

        return when (nextNode){
            is LeafNode -> {
                state.copy(
                    immediateTreatmentQuestions = state.immediateTreatmentQuestions.rebranch(node, answer),
                    leaf = nextNode,
                    detailsQuestions = getDetailsQuestion(state),
                    detailsQuestionsToShow = if (state.detailsQuestionsToShow == 0) 1 else state.detailsQuestionsToShow
                )
            }
            is ManualNode -> {
                state.copy(
                    immediateTreatmentQuestions =
                        state.immediateTreatmentQuestions.rebranch(node, answer) +
                        ImmediateTreatmentQuestionState(nextNode),
                    leaf = null
                )
            }
        }
    }

    private fun getDetailsQuestion(state: MainComplaintState) = state.complaint!!.details.questions
}