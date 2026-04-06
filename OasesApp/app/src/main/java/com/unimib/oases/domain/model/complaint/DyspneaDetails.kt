package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom

object DyspneaDetails: ComplaintDetails {
    override val questions = listOf(
        DyspneaDurationQuestion,
        DyspneaCourseQuestion,
        DyspneaCoughAspectQuestion,
        DyspneaOtherSymptoms,
        DyspneaOtherHighRiskSymptoms
    )
}

data object DyspneaDurationQuestion: SingleChoiceComplaintQuestion{
    override val id = DyspneaDetailQuestionsIds.DURATION
    override val question = "How long did the patient experience cough/difficulty breathing for?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.DyspneaInTheLastOneToSevenDays,
        Symptom.DyspneaInTheLastEightToFourteenDays,
        Symptom.DyspneaInTheLastFifteenToThirtyDays,
        Symptom.DyspneaInTheLastThirtyPlusDays
    )
}

data object DyspneaCourseQuestion: SingleChoiceComplaintQuestion{
    override val id = DyspneaDetailQuestionsIds.COURSE
    override val question = "How was the course of the cough/dyspnea?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.DyspneaAcuteCourse,
        Symptom.DyspneaProgressiveCourse,
        Symptom.DyspneaRecurrentCourse
    )
}

data object DyspneaCoughAspectQuestion: SingleChoiceComplaintQuestion{
    override val id = DyspneaDetailQuestionsIds.ASPECT
    override val question = "How does the cough look like?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.DyspneaDryCough,
        Symptom.DyspneaCoughWithMucous,
        Symptom.DyspneaCoughWithBlood,
        Symptom.DyspneaParoxysmalCoughWithWhoopsOrCentralCyanosisOrVomiting
    )
}

data object DyspneaOtherSymptoms: OtherSymptomsQuestion {
    override val id = DyspneaDetailQuestionsIds.OTHER_SYMPTOMS
    override val isRequired = false
    override val options = listOf(
        Symptom.FeverAbove38Degrees,
        Symptom.NightSweats,
        Symptom.Malnutrition,
        Symptom.ChestPain,
        Symptom.OrthopneaOrParoxysmalNocturnalDyspnea,
        Symptom.RunnyNoseOrSneezingOrSoreThroat,
        Symptom.Lethargy,
        Symptom.Convulsions,
        Symptom.InabilityToBreastfeedOrDrink,
        Symptom.Shock,
        Symptom.SeverePallor,
        Symptom.DecreasedBreathSoundsAtChestAuscultation,
        Symptom.CracklesAtChestAuscultation,
        Symptom.CardiacMurmurAtChestAuscultation,
        Symptom.IrregularPulseOrIrregularHeartSounds,
        Symptom.PeripheralEdemaOrJugularVenousDistension,
        Symptom.SwellingOrPainOrWarmthOrRednessOfOneLeg,
        Symptom.Hypoglycemia
    )
}

data object DyspneaOtherHighRiskSymptoms: OtherHighRiskSymptomsQuestion {
    override val id = DyspneaDetailQuestionsIds.OTHER_HIGH_RISK_SYMPTOMS
    override val isRequired = false
    override val options = listOf(
        Symptom.RecentChokingOrForeignBodyInhalation,
        Symptom.RecentChestTrauma,
        Symptom.HivPositive,
        Symptom.CloseContactWithKnownTuberculosisPatient,
        Symptom.AsthmaCOPDHistory,
        Symptom.HistoryOfHeartDisease,
        Symptom.HistoryOfRenalOrLiverDisease,
        Symptom.SickleCellDisease,
        Symptom.ProlongedImmobilizationOrBedridden,
        Symptom.CurrentPregnancy,
        Symptom.Unvaccinated,
        Symptom.SmokingOrExposedToSmoke
    )
}

object DyspneaDetailQuestionsIds {
    const val DURATION = "dyspnea_duration"
    const val COURSE = "dyspnea_course"
    const val ASPECT = "dyspnea_aspect"
    const val OTHER_SYMPTOMS = "dyspnea_other_symptoms"
    const val OTHER_HIGH_RISK_SYMPTOMS = "dyspnea_other_high_risk_symptoms"
}