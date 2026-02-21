package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.ImmediateTreatmentQuestionState
import javax.inject.Inject

class ConvertTreeBranchesToTextUseCase @Inject constructor(
    private val convertNodeAnswerToStringUseCase: ConvertNodeAnswerToStringUseCase
) {

    operator fun invoke(listOfAnsweredNodes: List<List<ImmediateTreatmentQuestionState>>): List<List<QuestionAndAnswer>> {
        return listOfAnsweredNodes.map { list ->
            list.map {
                QuestionAndAnswer(
                    it.node.value,
                    convertNodeAnswerToStringUseCase(it.answer!!)
                )
            }
        }
    }

}