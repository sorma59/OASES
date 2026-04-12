package com.unimib.oases.ui.screen.medical_visit.evaluation

import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.Tree
import com.unimib.oases.domain.model.symptom.Symptom

sealed class EvaluationEvent {

    data class NodeAnswered(val answer: Boolean, val node: ManualNode, val tree: Tree): EvaluationEvent()

    data class SymptomSelected(val symptom: Symptom, val question: ComplaintQuestion): EvaluationEvent()

    data class TestSelected(val test: LabelledTest): EvaluationEvent()

    data object RetryButtonClicked: EvaluationEvent()

    data object GoToTriageClicked: EvaluationEvent()

    data object GenerateTestsPressed: EvaluationEvent()

    data class AdditionalTestsTextChanged(val text: String): EvaluationEvent()

    data object SubmitPressed: EvaluationEvent()
}
