package com.unimib.oases.ui.screen.patient_registration.triage

data class TriageState(

    // Age:
    val age: Int? = null,
    // Vital Signs:
    val sbp: Int? = null,
    val dbp: Int? = null,
    val hr: Int? = null,
    val rr: Int? = null,
    val spo2: Int? = null,
    val temp: Double? = null,

    val unconsciousness: Boolean = false,
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

    val airwaySwellingMass: Boolean = false,
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

    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null,
){
    companion object {
        const val LOW_SPO2 = 92
        const val LOW_RR = 10
        const val HIGH_RR = 30
        const val LOW_HR = 50
        const val HIGH_HR = 130
        const val LOW_SBP = 90
        const val HIGH_SBP = 200
        const val LOW_TEMP = 35.0
        const val HIGH_TEMP = 39.0
    }

    val ageOver80Years: Boolean
        get() = age != null && age >= 80

    val alteredVitalSignsSpo2: Boolean
        get() = spo2 != null && spo2 < LOW_SPO2

    val alteredVitalSignsRrLow: Boolean
        get() = rr != null && rr < LOW_RR

    val alteredVitalSignsRrHigh: Boolean
        get() = rr != null && rr > HIGH_RR

    val alteredVitalSignsHrLow: Boolean
        get() = hr != null && hr < LOW_HR

    val alteredVitalSignsHrHigh: Boolean
        get() = hr != null && hr > HIGH_HR

    val alteredVitalSignsSbpLow: Boolean
        get() = sbp != null && sbp < LOW_SBP

    val alteredVitalSignsSbpHigh: Boolean
        get() = sbp != null && sbp > HIGH_SBP

    val alteredVitalSignsTempLow: Boolean
        get() = temp != null && temp < LOW_TEMP

    val alteredVitalSignsTempHigh: Boolean
        get() = temp != null && temp > HIGH_TEMP

    val isRedCode: Boolean
        get() =
            unconsciousness ||
            activeConvulsions ||
            respiratoryDistress ||
            heavyBleeding ||
            highRiskTraumaBurns ||
            threatenedLimb ||
            poisoningIntoxication ||
            snakeBite ||
            aggressiveBehavior ||
            pregnancyWithHeavyBleeding ||
            pregnancyWithSevereAbdominalPain ||
            pregnancyWithSeizures ||
            pregnancyWithAlteredMentalStatus ||
            pregnancyWithSevereHeadache ||
            pregnancyWithVisualChanges ||
            pregnancyWithSbpHighDpbHigh ||
            pregnancyWithTrauma ||
            pregnancyWithActiveLabor

    val isYellowCode: Boolean
        get() =
            !isRedCode &&
            (airwaySwellingMass || ongoingBleeding || severePallor || ongoingSevereVomitingDiarrhea ||
            unableToFeedOrDrink || recentFainting || lethargyConfusionAgitation || focalNeurologicVisualDeficit
            || headacheWithStiffNeck || severePain || acuteTesticularScrotalPainPriapism || unableToPassUrine ||
            acuteLimbDeformityOpenFracture || otherTraumaBurns || sexualAssault || animalBiteNeedlestickPuncture ||
            otherPregnancyRelatedComplaints || ageOver80Years || alteredVitalSignsSpo2 || alteredVitalSignsRrLow ||
            alteredVitalSignsRrHigh || alteredVitalSignsHrLow || alteredVitalSignsHrHigh || alteredVitalSignsSbpLow ||
            alteredVitalSignsSbpHigh || alteredVitalSignsTempLow || alteredVitalSignsTempHigh)
}