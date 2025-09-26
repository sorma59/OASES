package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.binarytree.Tree
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.util.StringFormatHelper.SnakeCaseString
import com.unimib.oases.util.StringFormatHelper.snakeCase

sealed interface Complaint {
    val complaintId: ComplaintId
    val immediateTreatmentTrees: List<Tree>
    val details: ComplaintDetails
    val tests: ComplaintTests
    val supportiveTherapies: ComplaintSupportiveTherapies

    val therapies get() = supportiveTherapies.therapies
}

enum class ComplaintId(val value: SnakeCaseString) {

    DIARRHEA(snakeCase("diarrhea")),
    DYSPNEA(snakeCase("dyspnea"));

    val id: String
        get() = value.string
}

interface ComplaintDetails {
    val questions: List<ComplaintQuestion>
}

interface ComplaintTests {
    val conditions: List<Condition>
}

interface ComplaintSupportiveTherapies {
    val therapies: List<SupportiveTherapy>
}

sealed interface Question {
    val question: String
    val type: QuestionType
    val isRequired: Boolean
    val options: List<Any>
}

sealed class QuestionType {
    object SingleChoice : QuestionType()
    object MultipleChoice : QuestionType()
}

sealed interface ComplaintQuestion: Question{
    override val question: String
    override val type: QuestionType
    override val options: List<Symptom>
}

sealed interface SingleChoiceComplaintQuestion: ComplaintQuestion{
    override val type
        get() = QuestionType.SingleChoice
}

sealed interface MultipleChoiceComplaintQuestion: ComplaintQuestion{
    override val type
        get() = QuestionType.MultipleChoice
}

sealed class OtherSymptomsQuestion(): MultipleChoiceComplaintQuestion{
    override val question = "Which other symptoms does the patient have?"
    override val options: List<Symptom> = emptyList()
}

sealed class OtherHighRiskSymptomsQuestion(): MultipleChoiceComplaintQuestion{
    override val question = "Which other high-risk features are present?"
    override val options: List<Symptom> = emptyList()
}