package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.ui.screen.medical_visit.evaluation.TreeSummary
import javax.inject.Inject

class ConvertTreeBranchesToTextUseCase @Inject constructor(
    private val convertNodeAnswerToStringUseCase: ConvertNodeAnswerToStringUseCase
) {

    operator fun invoke(listOfAnsweredNodes: List<TreeSummary>): List<List<QuestionAndAnswer>> {
        return listOfAnsweredNodes.map { summary ->
            summary.answers.map {
                QuestionAndAnswer(
                    it .node.value,
                    convertNodeAnswerToStringUseCase(it.answer!!)
                )
            }
        }
    }

}