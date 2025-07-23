package com.unimib.oases.ui.screen.patient_registration.triage

import com.unimib.oases.domain.model.TriageEvaluation

fun TriageEvaluation.mapToTriageState(): TriageState{
    return TriageState(
        unconsciousness = unconsciousness,
        activeConvulsions = activeConvulsions,
        respiratoryDistress = respiratoryDistress,
        heavyBleeding = heavyBleeding,
        highRiskTraumaBurns = highRiskTraumaBurns,
        threatenedLimb = threatenedLimb,
        poisoningIntoxication = poisoningIntoxication,
        snakeBite = snakeBite,
        aggressiveBehavior = aggressiveBehavior,
        pregnancyWithHeavyBleeding = pregnancyWithHeavyBleeding,
        pregnancyWithSevereAbdominalPain = pregnancyWithSevereAbdominalPain,
        pregnancyWithSeizures = pregnancyWithSeizures,
        pregnancyWithAlteredMentalStatus = pregnancyWithAlteredMentalStatus,
        pregnancyWithSevereHeadache = pregnancyWithSevereHeadache,
        pregnancyWithVisualChanges = pregnancyWithVisualChanges,
        pregnancyWithTrauma = pregnancyWithTrauma,
        pregnancyWithActiveLabor = pregnancyWithActiveLabor,
        pregnancy = ( // The UI uses pregnancy field to show pregnancy related symptoms,
                      // if any is true then pregnancy should be true as well
            pregnancyWithHeavyBleeding ||
            pregnancyWithSevereAbdominalPain ||
            pregnancyWithSeizures ||
            pregnancyWithAlteredMentalStatus ||
            pregnancyWithSevereHeadache ||
            pregnancyWithVisualChanges ||
            pregnancyWithSbpHighDpbHigh ||
            pregnancyWithTrauma ||
            pregnancyWithActiveLabor
        ),

        airwaySwellingMass = airwaySwellingMass,
        ongoingBleeding = ongoingBleeding,
        severePallor = severePallor,
        ongoingSevereVomitingDiarrhea = ongoingSevereVomitingDiarrhea,
        unableToFeedOrDrink = unableToFeedOrDrink,
        recentFainting = recentFainting,
        lethargyConfusionAgitation = lethargyConfusionAgitation,
        focalNeurologicVisualDeficit = focalNeurologicVisualDeficit,
        headacheWithStiffNeck = headacheWithStiffNeck,
        severePain = severePain,
        acuteTesticularScrotalPainPriapism = acuteTesticularScrotalPainPriapism,
        unableToPassUrine = unableToPassUrine,
        acuteLimbDeformityOpenFracture = acuteLimbDeformityOpenFracture,
        otherTraumaBurns = otherTraumaBurns,
        sexualAssault = sexualAssault,
        animalBiteNeedlestickPuncture = animalBiteNeedlestickPuncture,
        otherPregnancyRelatedComplaints = otherPregnancyRelatedComplaints,

    )
}

fun TriageState.mapToTriageEvaluation(visitId: String): TriageEvaluation{
    return TriageEvaluation(
        visitId = visitId,
        unconsciousness = unconsciousness,
        activeConvulsions = activeConvulsions,
        respiratoryDistress = respiratoryDistress,
        heavyBleeding = heavyBleeding,
        highRiskTraumaBurns = highRiskTraumaBurns,
        threatenedLimb = threatenedLimb,
        poisoningIntoxication = poisoningIntoxication,
        snakeBite = snakeBite,
        aggressiveBehavior = aggressiveBehavior,
        pregnancyWithHeavyBleeding = pregnancyWithHeavyBleeding,
        pregnancyWithSevereAbdominalPain = pregnancyWithSevereAbdominalPain,
        pregnancyWithSeizures = pregnancyWithSeizures,
        pregnancyWithAlteredMentalStatus = pregnancyWithAlteredMentalStatus,
        pregnancyWithSevereHeadache = pregnancyWithSevereHeadache,
        pregnancyWithVisualChanges = pregnancyWithVisualChanges,
        pregnancyWithSbpHighDpbHigh = pregnancy && sbpHighDbpHighForPregnancy,
        pregnancyWithTrauma = pregnancyWithTrauma,
        pregnancyWithActiveLabor = pregnancyWithActiveLabor,

        airwaySwellingMass = airwaySwellingMass,
        ongoingBleeding = ongoingBleeding,
        severePallor = severePallor,
        ongoingSevereVomitingDiarrhea = ongoingSevereVomitingDiarrhea,
        unableToFeedOrDrink = unableToFeedOrDrink,
        recentFainting = recentFainting,
        lethargyConfusionAgitation = lethargyConfusionAgitation,
        focalNeurologicVisualDeficit = focalNeurologicVisualDeficit,
        headacheWithStiffNeck = headacheWithStiffNeck,
        severePain = severePain,
        acuteTesticularScrotalPainPriapism = acuteTesticularScrotalPainPriapism,
        unableToPassUrine = unableToPassUrine,
        acuteLimbDeformityOpenFracture = acuteLimbDeformityOpenFracture,
        otherTraumaBurns = otherTraumaBurns,
        sexualAssault = sexualAssault,
        animalBiteNeedlestickPuncture = animalBiteNeedlestickPuncture,
        otherPregnancyRelatedComplaints = otherPregnancyRelatedComplaints,
    )
}