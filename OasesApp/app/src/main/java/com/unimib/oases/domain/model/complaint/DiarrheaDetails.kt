package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.OtherHighRiskSymptomsQuestion.DiarrheaOtherHighRiskSymptoms
import com.unimib.oases.domain.model.complaint.OtherSymptomsQuestion.DiarrheaOtherSymptoms

class DiarrheaDetails: ComplaintDetails {

    val durationQuestion = DurationQuestion(
        "For how long did the patient experience diarrhea?",
        DiarrheaDuration.entries
    )

    val frequencyQuestion = FrequencyQuestion(
        "How many episodes of diarrhea does the patient have?",
        DiarrheaFrequency.entries
    )

    val aspectQuestion = AspectQuestion(
        "What do the stools look like?",
        DiarrheaAspect.entries
    )

    val otherSymptomsQuestion = DiarrheaOtherSymptoms

    val otherHighRiskFeaturesQuestion = DiarrheaOtherHighRiskSymptoms

    override val questions = listOf(
        durationQuestion,
        frequencyQuestion,
        aspectQuestion,
        otherSymptomsQuestion,
        otherHighRiskFeaturesQuestion
    )
}

sealed interface Option {
    val displayText: String
}

enum class DiarrheaDuration(override val displayText: String): Option{
    ONE_TO_SEVEN_DAYS("1-7 days"),
    EIGHT_TO_FOURTEEN_DAYS("8-14 days"),
    FIFTEEN_TO_THIRTY_DAYS("15-30 days"),
    MORE_THAN_THIRTY_DAYS("more than 30 days")
}

enum class DiarrheaFrequency(override val displayText: String): Option{
    ONE_TO_TWO_TIMES_A_DAY("1-2 episodes/day"),
    THREE_TO_FIVE_TIMES_A_DAY("3-5 episodes/day"),
    SIX_OR_MORE_TIMES_A_DAY("6 or more episodes/day")
}

enum class DiarrheaAspect(override val displayText: String): Option{
    WATERY("Watery"),
    WITH_BLOOD("With blood"),
    OILY_OR_GREASY_OR_FOUL_SMELLING("Oily/greasy/foul-smelling")
}

class DurationQuestion(
    override val question: String,
    override val options: List<Option>
): SingleChoiceComplaintQuestion

class FrequencyQuestion(
    override val question: String,
    override val options: List<Option>
): SingleChoiceComplaintQuestion

class AspectQuestion(
    override val question: String,
    override val options: List<Option>
): SingleChoiceComplaintQuestion