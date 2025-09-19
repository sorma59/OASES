package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom

sealed interface Complaint {
    val id: ComplaintId
    val details: ComplaintDetails
    val tests: ComplaintTests
}

enum class ComplaintId(val id: String) {
    DIARRHEA("diarrhea");
}

interface ComplaintDetails {
    val questions: List<ComplaintQuestion>
}

interface ComplaintTests {
    val conditions: List<Condition>
}

sealed interface Question {
    val question: String
    val type: QuestionType
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
    override val question = "Which other high-risk symptoms does the patient have?"
    override val options: List<Symptom> = emptyList()
}