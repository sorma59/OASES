package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom

val statusEpilepticusImmediateTreatment = ImmediateTreatment(
    """
        If the patient is unconscious / responsive only to pain (P, U), consider risk of airway obstruction / inability to protect airways:
          - give oxygen via nasal cannulas (1-5 L/min) or face mask (up to 10 L/min) to target SpO₂ > 90%
          - open airway (head-tilt/chin-lift if no trauma or jaw thrust if trauma suspected) and consider oropharyngeal cannula use in no gag reflex
          - consider intubation/tracheostomy if GCS persistently < 8, inadequate airway protection (aspiration), or refractory hypoxia
    """.trimIndent()
)

object SeizuresOrComaDetails: ComplaintDetails {
    override val questions = listOf(
        SeizuresOrComaResponsivenessQuestion,
        SeizuresOrComaDurationQuestion,
        SeizuresOrComaEpilepsyQuestion,
        SeizureOrComaSeizuresTypeQuestion,
        SeizuresOrComaOtherSymptoms,
        SeizuresOrComaOtherHighRiskSymptoms
    )
}

data object SeizuresOrComaResponsivenessQuestion: SingleChoiceComplaintQuestion, ComplaintQuestionWithImmediateTreatment {
    override val question = "How is the patient current level of consciousness?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.AlertLevelOfConsciousness,
        Symptom.ResponsiveToVoiceLevelOfConsciousness,
        Symptom.ResponsiveToPainLevelOfConsciousness,
        Symptom.UnresponsiveLevelOfConsciousness
    )
    override val treatment = statusEpilepticusImmediateTreatment
    override val shouldShowTreatment = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.ResponsiveToPainLevelOfConsciousness)
        || symptoms.contains(Symptom.UnresponsiveLevelOfConsciousness)
    }
}

data object SeizuresOrComaDurationQuestion: SingleChoiceComplaintQuestion {
    override val question = "How long did the patient experience seizures or altered state of consciousness for?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.SeizuresOrComaInTheLastOneToSevenDays,
        Symptom.SeizuresOrComaInTheLastEightToFourteenDays,
        Symptom.SeizuresOrComaInTheLastFifteenToThirtyDays,
        Symptom.SeizuresOrComaInTheLastThirtyPlusDays
    )
}

data object SeizuresOrComaEpilepsyQuestion: BooleanComplaintQuestion {
    override val question = "Has the patient had ≥ 2 seizures with incomplete recovery of consciousness between them or a seizure lasting > 5 min?"
    override val isRequired = true
    override val trueSymptom = Symptom.StatusEpilepticus
    override val falseSymptom = Symptom.AbsenceOfStatusEpilepticus
    override val options: List<Symptom> = listOf(
        trueSymptom,
        falseSymptom
    )
}

data object SeizureOrComaSeizuresTypeQuestion: SingleChoiceComplaintQuestion {
    override val question = "How are the seizures?"
    override val isRequired = true
    override val options: List<Symptom> = listOf(
        Symptom.SeizuresOrComaFocalSeizures,
        Symptom.SeizuresOrComaGeneralizedMotorSeizures,
        Symptom.SeizuresOrComaAbsenceOfSeizures
    )
}

data object SeizuresOrComaOtherSymptoms: OtherSymptomsQuestion {
    override val isRequired = false
    override val options = listOf(
        Symptom.Hypoglycemia,
        Symptom.IrritabilityOrConfusion,
        Symptom.FeverAbove38Degrees,
        Symptom.HeadacheOrCervicalPain,
        Symptom.StiffNeck,
        Symptom.BulgingFontanelle,
        Symptom.FocalNeurologicalDeficit,
        Symptom.UnequalPupils,
        Symptom.Opisthotonus,
        Symptom.Vomiting,
        Symptom.WeightLoss,
        Symptom.InabilityToBreastfeedOrDrink,
        Symptom.Shock,
        Symptom.PetechialRashOrPurpura,
        Symptom.SeverePallor,
        Symptom.Jaundice,
        Symptom.PeripheralEdemaOrJugularVenousDistension
    )
}

data object SeizuresOrComaOtherHighRiskSymptoms: OtherHighRiskSymptomsQuestion {
    override val isRequired = false
    override val options: List<Symptom> = listOf(
        Symptom.EpilepsyOrHistoryOfRecurrentUnprovokedSeizures,
        Symptom.HeadInjury,
        Symptom.SuspectOfDrugOrToxinIngestion,
        Symptom.SuspectOfAlcoholUseOrWithdrawal,
        Symptom.HivPositive,
        Symptom.CloseContactWithKnownTuberculosisPatient,
        Symptom.HistoryOfRenalOrLiverDisease,
        Symptom.SickleCellDisease,
        Symptom.Diabetes,
        Symptom.CurrentPregnancy
    )
}