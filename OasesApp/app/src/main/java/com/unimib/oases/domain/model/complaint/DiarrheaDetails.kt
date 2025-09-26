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
    override val question = "How long did the patient experience diarrhea for?"
    override val isRequired = true
    override val options = listOf(
        Symptom.DiarrheaInTheLastOneToSevenDays,
        Symptom.DiarrheaInTheLastEightToFourteenDays,
        Symptom.DiarrheaInTheLastFifteenToThirtyDays,
        Symptom.DiarrheaInTheLastThirtyPlusDays
    )
}

data object DiarrheaFrequencyQuestion: SingleChoiceComplaintQuestion{
    override val question = "How many episodes of diarrhea does the patient have?"
    override val isRequired = true
    override val options = listOf(
        Symptom.DiarrheaEpisodesOnceOrTwiceADay,
        Symptom.DiarrheaEpisodesThreeToFiveTimesADay,
        Symptom.DiarrheaEpisodesSixOrMoreTimesADay
    )
}

data object DiarrheaAspectQuestion: SingleChoiceComplaintQuestion{
    override val question = "How do the stools look like?"
    override val isRequired = true
    override val options = listOf(
        Symptom.DiarrheaWateryStools,
        Symptom.DiarrheaBloodyStools,
        Symptom.DiarrheaOilyOrGreasyOrFoulSmellingStools
    )
}

data object DiarrheaOtherSymptoms: OtherSymptomsQuestion() {
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

data object DiarrheaOtherHighRiskSymptoms: OtherHighRiskSymptomsQuestion() {
    override val isRequired = false
    override val options = listOf(
        Symptom.HivPositive,
        Symptom.CholeraOutbreak
    )
}