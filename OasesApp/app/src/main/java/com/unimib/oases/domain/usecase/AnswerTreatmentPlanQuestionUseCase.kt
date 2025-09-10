package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintState
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.QuestionState
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.rebranch
import com.unimib.oases.util.datastructure.binarytree.LeafNode
import com.unimib.oases.util.datastructure.binarytree.ManualNode
import com.unimib.oases.util.datastructure.binarytree.next
import javax.inject.Inject

class AnswerTreatmentPlanQuestionUseCase @Inject constructor() {
    operator fun invoke(answer: Boolean, node: ManualNode, state: MainComplaintState): MainComplaintState{
        val nextNode = node.next(answer)

        return when (nextNode){
            is LeafNode -> {
                state.copy(
                    questions = state.questions.rebranch(node, answer),
                    leaf = nextNode
                )
            }
            is ManualNode -> {
                state.copy(
                    questions = state.questions.rebranch(node, answer) + QuestionState(nextNode),
                    leaf = null
                )
            }
        }
    }
}