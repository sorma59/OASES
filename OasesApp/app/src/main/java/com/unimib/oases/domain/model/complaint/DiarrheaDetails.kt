package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom

object DiarrheaDetails: ComplaintDetails {

    override val questions = listOf(
        DiarrheaDurationQuestion,
        DiarrheaFrequencyQuestion,
        DiarrheaAspectQuestion,
        DiarrheaOtherSymptoms,
        DiarrheaOtherHighRiskSymptoms
    )
}

data object DiarrheaDurationQuestion: SingleChoiceComplaintQuestion{
    override val id = DiarrheaDetailQuestionsIds.DURATION
    override val question = "How long did the patient experience diarrhea for?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.DiarrheaInTheLastOneToSevenDays,
        Symptom.DiarrheaInTheLastEightToFourteenDays,
        Symptom.DiarrheaInTheLastFifteenToThirtyDays,
        Symptom.DiarrheaInTheLastThirtyPlusDays
    )
}

data object DiarrheaFrequencyQuestion: SingleChoiceComplaintQuestion{
    override val id = DiarrheaDetailQuestionsIds.FREQUENCY
    override val question = "How many episodes of diarrhea does the patient have?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.DiarrheaEpisodesOnceOrTwiceADay,
        Symptom.DiarrheaEpisodesThreeToFiveTimesADay,
        Symptom.DiarrheaEpisodesSixOrMoreTimesADay
    )
}

data object DiarrheaAspectQuestion: SingleChoiceComplaintQuestion{
    override val id = DiarrheaDetailQuestionsIds.ASPECT
    override val question = "How do the stools look like?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.DiarrheaWateryStools,
        Symptom.DiarrheaBloodyStools,
        Symptom.DiarrheaOilyOrGreasyOrFoulSmellingStools
    )
}

data object DiarrheaOtherSymptoms: OtherSymptomsQuestion {
    override val id = DiarrheaDetailQuestionsIds.OTHER_SYMPTOMS
    override val isRequired = false
    override val options = listOf(
        Symptom.Vomiting,
        Symptom.AbdominalPain,
        Symptom.AbdominalDistensionAndTendernessWithAlteredBowelSounds,
        Symptom.WeightLoss,
        Symptom.Malnutrition,
        Symptom.FeverAbove38Degrees,
        Symptom.Convulsions,
        Symptom.EasyBruising,
        Symptom.SeverePallor,
        Symptom.Hypoglycemia
    )
}

data object DiarrheaOtherHighRiskSymptoms: OtherHighRiskSymptomsQuestion {
    override val id = DiarrheaDetailQuestionsIds.OTHER_HIGH_RISK_SYMPTOMS
    override val isRequired = false
    override val options = listOf(
        Symptom.HivPositive,
        Symptom.CholeraOutbreak
    )
}

object DiarrheaDetailQuestionsIds {
    const val DURATION = "diarrhea_duration"
    const val FREQUENCY = "diarrhea_frequency"
    const val ASPECT = "diarrhea_aspect"
    const val OTHER_SYMPTOMS = "diarrhea_other_symptoms"
    const val OTHER_HIGH_RISK_SYMPTOMS = "diarrhea_other_high_risk_symptoms"
}