package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.binarytree.Tree
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.util.StringFormatHelper.SnakeCaseString
import com.unimib.oases.util.StringFormatHelper.snakeCase

fun ComplaintQuestion.getLabels(): List<String> {
    return if (this is BooleanComplaintQuestion)
        this.options.map {
            if (it == this.trueSymptom)
                this.trueLabel
            else
                this.falseLabel
        }
    else
        this.options.map { it.label }
}

sealed interface Complaint {
    val complaintId: ComplaintId
    val immediateTreatmentTrees: List<Tree>
    val details: ComplaintDetails
    val tests: ComplaintTests
    val supportiveTherapies: ComplaintSupportiveTherapies
    val therapies get() = supportiveTherapies.therapies
}

enum class ComplaintId(val value: SnakeCaseString, val label: String) {

    DIARRHEA(snakeCase("diarrhea"), "Diarrhea"),
    DYSPNEA(snakeCase("dyspnea"), "Dyspnea"),
    SEIZURES_OR_COMA(snakeCase("seizures_or_coma"), "Seizures or coma");

    companion object {
        val complaints = mapOf(
            DIARRHEA.id to DIARRHEA,
            DYSPNEA.id to DYSPNEA,
            SEIZURES_OR_COMA.id to SEIZURES_OR_COMA
        )
    }

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

sealed interface ComplaintQuestionWithImmediateTreatment {
    val treatment: ImmediateTreatment
    val shouldShowTreatment: (Set<Symptom>) -> Boolean
}

sealed class QuestionType {
    object SingleChoice: QuestionType()
    object MultipleChoice: QuestionType()
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

sealed interface BooleanComplaintQuestion: SingleChoiceComplaintQuestion{
    val trueSymptom: Symptom
    val falseSymptom: Symptom
    val trueLabel: String
        get() = "Yes"
    val falseLabel: String
        get() = "No"
}

sealed interface OtherSymptomsQuestion: MultipleChoiceComplaintQuestion{
    override val question: String
        get() = "Which other symptoms does the patient have?"
    override val options: List<Symptom>
}

sealed interface OtherHighRiskSymptomsQuestion: MultipleChoiceComplaintQuestion{
    override val question: String
        get() = "Which other high-risk features are present?"
    override val options: List<Symptom>
}