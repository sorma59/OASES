package com.unimib.oases.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TriageEvaluation(

    val visitId: String,

    // RED Code Symptoms
    val unconsciousness: Boolean,
    val activeConvulsions: Boolean = false,
    val respiratoryDistress: Boolean = false,
    val heavyBleeding: Boolean = false,
    val highRiskTraumaBurns: Boolean = false,
    val threatenedLimb: Boolean = false,
    val poisoningIntoxication: Boolean = false,
    val snakeBite: Boolean = false,
    val aggressiveBehavior: Boolean = false,
    val pregnancyWithHeavyBleeding: Boolean = false,
    val pregnancyWithSevereAbdominalPain: Boolean = false,
    val pregnancyWithSeizures: Boolean = false,
    val pregnancyWithAlteredMentalStatus: Boolean = false,
    val pregnancyWithSevereHeadache: Boolean = false,
    val pregnancyWithVisualChanges: Boolean = false,
    val pregnancyWithSbpHighDpbHigh: Boolean = false,
    val pregnancyWithTrauma: Boolean = false,
    val pregnancyWithActiveLabor: Boolean = false,

    // YELLOW Code Symptoms
    val airwaySwellingMass: Boolean,
    val ongoingBleeding: Boolean = false,
    val severePallor: Boolean = false,
    val ongoingSevereVomitingDiarrhea: Boolean = false,
    val unableToFeedOrDrink: Boolean = false,
    val recentFainting: Boolean = false,
    val lethargyConfusionAgitation: Boolean = false,
    val focalNeurologicVisualDeficit: Boolean = false,
    val headacheWithStiffNeck: Boolean = false,
    val severePain: Boolean = false,
    val acuteTesticularScrotalPainPriapism: Boolean = false,
    val unableToPassUrine: Boolean = false,
    val acuteLimbDeformityOpenFracture: Boolean = false,
    val otherTraumaBurns: Boolean = false,
    val sexualAssault: Boolean = false,
    val animalBiteNeedlestickPuncture: Boolean = false,
    val otherPregnancyRelatedComplaints: Boolean = false,
    val ageOver80Years: Boolean = false,
    val alteredVitalSignsSpo2: Boolean = false,
    val alteredVitalSignsRrLow: Boolean = false,
    val alteredVitalSignsRrHigh: Boolean = false,
    val alteredVitalSignsHrLow: Boolean = false,
    val alteredVitalSignsHrHigh: Boolean = false,
    val alteredVitalSignsSbpLow: Boolean = false,
    val alteredVitalSignsSbpHigh: Boolean = false,
    val alteredVitalSignsTempLow: Boolean = false,
    val alteredVitalSignsTempHigh: Boolean = false
)