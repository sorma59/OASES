package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.ImmediateTreatmentQuestionState
import javax.inject.Inject

class ConvertTreeBranchesToTextUseCase @Inject constructor(
    private val convertNodeAnswerToStringUseCase: ConvertNodeAnswerToStringUseCase
) {

    operator fun invoke(listOfAnsweredNodes: List<List<ImmediateTreatmentQuestionState>>): List<List<QuestionAndAnswer>> {

        val list = mutableListOf<MutableList<QuestionAndAnswer>>()

        for ((index, algorithm) in listOfAnsweredNodes.withIndex()){
            list.add(mutableListOf())
            for (question in algorithm){
                list[index].add(
                    QuestionAndAnswer(
                        question.node.value,
                        convertNodeAnswerToStringUseCase(question.answer!!)
                    )
                )
            }
        }

        return list.toList()
    }

}