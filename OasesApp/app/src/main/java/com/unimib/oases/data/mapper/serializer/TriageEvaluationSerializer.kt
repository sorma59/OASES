package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.TriageEvaluation
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TriageEvaluationSerializer {
    fun serialize(triageEvaluation: TriageEvaluation): ByteArray {
        val visitIdBytes = triageEvaluation.visitId.toByteArray(Charsets.UTF_8)
        val unconsciousnessBytes = triageEvaluation.unconsciousness.toString().toByteArray(Charsets.UTF_8)
        val activeConvulsionsBytes = triageEvaluation.activeConvulsions.toString().toByteArray(Charsets.UTF_8)
        val respiratoryDistressBytes = triageEvaluation.respiratoryDistress.toString().toByteArray(Charsets.UTF_8)
        val heavyBleedingBytes = triageEvaluation.heavyBleeding.toString().toByteArray(Charsets.UTF_8)
        val highRiskTraumaBurnsBytes = triageEvaluation.highRiskTraumaBurns.toString().toByteArray(Charsets.UTF_8)
        val threatenedLimbBytes = triageEvaluation.threatenedLimb.toString().toByteArray(Charsets.UTF_8)
        val poisoningIntoxicationBytes = triageEvaluation.poisoningIntoxication.toString().toByteArray(Charsets.UTF_8)
        val snakeBiteBytes = triageEvaluation.snakeBite.toString().toByteArray(Charsets.UTF_8)
        val aggressiveBehaviorBytes = triageEvaluation.aggressiveBehavior.toString().toByteArray(Charsets.UTF_8)
        val pregnancyHeavyBleedingBytes = triageEvaluation.pregnancyWithHeavyBleeding.toString().toByteArray(Charsets.UTF_8)
        val severeAbdominalPainBytes = triageEvaluation.pregnancyWithSevereAbdominalPain.toString().toByteArray(Charsets.UTF_8)
        val seizuresBytes = triageEvaluation.pregnancyWithSeizures.toString().toByteArray(Charsets.UTF_8)
        val alteredMentalStatusBytes = triageEvaluation.pregnancyWithAlteredMentalStatus.toString().toByteArray(Charsets.UTF_8)
        val severeHeadacheBytes = triageEvaluation.pregnancyWithSevereHeadache.toString().toByteArray(Charsets.UTF_8)
        val visualChangesBytes = triageEvaluation.pregnancyWithVisualChanges.toString().toByteArray(Charsets.UTF_8)
        val sbpHighDpbHighBytes = triageEvaluation.pregnancyWithSbpHighDpbHigh.toString().toByteArray(Charsets.UTF_8)
        val traumaBytes = triageEvaluation.pregnancyWithTrauma.toString().toByteArray(Charsets.UTF_8)
        val activeLaborBytes = triageEvaluation.pregnancyWithActiveLabor.toString().toByteArray(Charsets.UTF_8)
        val airwaySwellingMassBytes = triageEvaluation.airwaySwellingMass.toString().toByteArray(Charsets.UTF_8)
        val ongoingBleedingBytes = triageEvaluation.ongoingBleeding.toString().toByteArray(Charsets.UTF_8)
        val severePallorBytes = triageEvaluation.severePallor.toString().toByteArray(Charsets.UTF_8)
        val ongoingSevereVomitingDiarrheaBytes = triageEvaluation.ongoingSevereVomitingDiarrhea.toString().toByteArray(Charsets.UTF_8)
        val unableToFeedOrDrinkBytes = triageEvaluation.unableToFeedOrDrink.toString().toByteArray(Charsets.UTF_8)
        val recentFaintingBytes = triageEvaluation.recentFainting.toString().toByteArray(Charsets.UTF_8)
        val lethargyConfusionAgitationBytes = triageEvaluation.lethargyConfusionAgitation.toString().toByteArray(Charsets.UTF_8)
        val focalNeurologicVisualDeficitBytes = triageEvaluation.focalNeurologicVisualDeficit.toString().toByteArray(Charsets.UTF_8)
        val headacheWithStiffNeckBytes = triageEvaluation.headacheWithStiffNeck.toString().toByteArray(Charsets.UTF_8)
        val severePainBytes = triageEvaluation.severePain.toString().toByteArray(Charsets.UTF_8)
        val acuteTesticularScrotalPainPriapismBytes = triageEvaluation.acuteTesticularScrotalPainPriapism.toString().toByteArray(Charsets.UTF_8)
        val unableToPassUrineBytes = triageEvaluation.unableToPassUrine.toString().toByteArray(Charsets.UTF_8)
        val acuteLimbDeformityOpenFractureBytes = triageEvaluation.acuteLimbDeformityOpenFracture.toString().toByteArray(Charsets.UTF_8)
        val otherTraumaBurnsBytes = triageEvaluation.otherTraumaBurns.toString().toByteArray(Charsets.UTF_8)
        val sexualAssaultBytes = triageEvaluation.sexualAssault.toString().toByteArray(Charsets.UTF_8)
        val animalBiteNeedlestickPunctureBytes = triageEvaluation.animalBiteNeedlestickPuncture.toString().toByteArray(Charsets.UTF_8)
        val otherPregnancyRelatedComplaintsBytes = triageEvaluation.otherPregnancyRelatedComplaints.toString().toByteArray(Charsets.UTF_8)
        val ageOver80YearsBytes = triageEvaluation.ageOver80Years.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsSpo2Bytes = triageEvaluation.alteredVitalSignsSpo2.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsRrLowBytes = triageEvaluation.alteredVitalSignsRrLow.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsRrHighBytes = triageEvaluation.alteredVitalSignsRrHigh.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsHrLowBytes = triageEvaluation.alteredVitalSignsHrLow.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsHrHighBytes = triageEvaluation.alteredVitalSignsHrHigh.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsSbpLowBytes = triageEvaluation.alteredVitalSignsSbpLow.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsSbpHighBytes = triageEvaluation.alteredVitalSignsSbpHigh.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsTempLowBytes = triageEvaluation.alteredVitalSignsTempLow.toString().toByteArray(Charsets.UTF_8)
        val alteredVitalSignsTempHighBytes = triageEvaluation.alteredVitalSignsTempHigh.toString().toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + visitIdBytes.size +
            4 + unconsciousnessBytes.size +
            4 + activeConvulsionsBytes.size +
            4 + respiratoryDistressBytes.size +
            4 + heavyBleedingBytes.size +
            4 + highRiskTraumaBurnsBytes.size +
            4 + threatenedLimbBytes.size +
            4 + poisoningIntoxicationBytes.size +
            4 + snakeBiteBytes.size +
            4 + aggressiveBehaviorBytes.size +
            4 + pregnancyHeavyBleedingBytes.size +
            4 + severeAbdominalPainBytes.size +
            4 + seizuresBytes.size +
            4 + alteredMentalStatusBytes.size +
            4 + severeHeadacheBytes.size +
            4 + visualChangesBytes.size +
            4 + sbpHighDpbHighBytes.size +
            4 + traumaBytes.size +
            4 + activeLaborBytes.size +
            4 + airwaySwellingMassBytes.size +
            4 + ongoingBleedingBytes.size +
            4 + severePallorBytes.size +
            4 + ongoingSevereVomitingDiarrheaBytes.size +
            4 + unableToFeedOrDrinkBytes.size +
            4 + recentFaintingBytes.size +
            4 + lethargyConfusionAgitationBytes.size +
            4 + focalNeurologicVisualDeficitBytes.size +
            4 + headacheWithStiffNeckBytes.size +
            4 + severePainBytes.size +
            4 + acuteTesticularScrotalPainPriapismBytes.size +
            4 + unableToPassUrineBytes.size +
            4 + acuteLimbDeformityOpenFractureBytes.size +
            4 + otherTraumaBurnsBytes.size +
            4 + sexualAssaultBytes.size +
            4 + animalBiteNeedlestickPunctureBytes.size +
            4 + otherPregnancyRelatedComplaintsBytes.size +
            4 + ageOver80YearsBytes.size +
            4 + alteredVitalSignsSpo2Bytes.size +
            4 + alteredVitalSignsRrLowBytes.size +
            4 + alteredVitalSignsRrHighBytes.size +
            4 + alteredVitalSignsHrLowBytes.size +
            4 + alteredVitalSignsHrHighBytes.size +
            4 + alteredVitalSignsSbpLowBytes.size +
            4 + alteredVitalSignsSbpHighBytes.size +
            4 + alteredVitalSignsTempLowBytes.size +
            4 + alteredVitalSignsTempHighBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(visitIdBytes.size)
        buffer.put(visitIdBytes)

        buffer.putInt(unconsciousnessBytes.size)
        buffer.put(unconsciousnessBytes)

        buffer.putInt(activeConvulsionsBytes.size)
        buffer.put(activeConvulsionsBytes)

        buffer.putInt(respiratoryDistressBytes.size)
        buffer.put(respiratoryDistressBytes)

        buffer.putInt(heavyBleedingBytes.size)
        buffer.put(heavyBleedingBytes)

        buffer.putInt(highRiskTraumaBurnsBytes.size)
        buffer.put(highRiskTraumaBurnsBytes)

        buffer.putInt(threatenedLimbBytes.size)
        buffer.put(threatenedLimbBytes)

        buffer.putInt(poisoningIntoxicationBytes.size)
        buffer.put(poisoningIntoxicationBytes)

        buffer.putInt(snakeBiteBytes.size)
        buffer.put(snakeBiteBytes)

        buffer.putInt(aggressiveBehaviorBytes.size)
        buffer.put(aggressiveBehaviorBytes)

        buffer.putInt(pregnancyHeavyBleedingBytes.size)
        buffer.put(pregnancyHeavyBleedingBytes)

        buffer.putInt(severeAbdominalPainBytes.size)
        buffer.put(severeAbdominalPainBytes)

        buffer.putInt(seizuresBytes.size)
        buffer.put(seizuresBytes)

        buffer.putInt(alteredMentalStatusBytes.size)
        buffer.put(alteredMentalStatusBytes)

        buffer.putInt(severeHeadacheBytes.size)
        buffer.put(severeHeadacheBytes)

        buffer.putInt(visualChangesBytes.size)
        buffer.put(visualChangesBytes)

        buffer.putInt(sbpHighDpbHighBytes.size)
        buffer.put(sbpHighDpbHighBytes)

        buffer.putInt(traumaBytes.size)
        buffer.put(traumaBytes)

        buffer.putInt(activeLaborBytes.size)
        buffer.put(activeLaborBytes)

        buffer.putInt(airwaySwellingMassBytes.size)
        buffer.put(airwaySwellingMassBytes)

        buffer.putInt(ongoingBleedingBytes.size)
        buffer.put(ongoingBleedingBytes)

        buffer.putInt(severePallorBytes.size)
        buffer.put(severePallorBytes)

        buffer.putInt(ongoingSevereVomitingDiarrheaBytes.size)
        buffer.put(ongoingSevereVomitingDiarrheaBytes)

        buffer.putInt(unableToFeedOrDrinkBytes.size)
        buffer.put(unableToFeedOrDrinkBytes)

        buffer.putInt(recentFaintingBytes.size)
        buffer.put(recentFaintingBytes)

        buffer.putInt(lethargyConfusionAgitationBytes.size)
        buffer.put(lethargyConfusionAgitationBytes)

        buffer.putInt(focalNeurologicVisualDeficitBytes.size)
        buffer.put(focalNeurologicVisualDeficitBytes)

        buffer.putInt(headacheWithStiffNeckBytes.size)
        buffer.put(headacheWithStiffNeckBytes)

        buffer.putInt(severePainBytes.size)
        buffer.put(severePainBytes)

        buffer.putInt(acuteTesticularScrotalPainPriapismBytes.size)
        buffer.put(acuteTesticularScrotalPainPriapismBytes)

        buffer.putInt(unableToPassUrineBytes.size)
        buffer.put(unableToPassUrineBytes)

        buffer.putInt(acuteLimbDeformityOpenFractureBytes.size)
        buffer.put(acuteLimbDeformityOpenFractureBytes)

        buffer.putInt(otherTraumaBurnsBytes.size)
        buffer.put(otherTraumaBurnsBytes)

        buffer.putInt(sexualAssaultBytes.size)
        buffer.put(sexualAssaultBytes)

        buffer.putInt(animalBiteNeedlestickPunctureBytes.size)
        buffer.put(animalBiteNeedlestickPunctureBytes)

        buffer.putInt(otherPregnancyRelatedComplaintsBytes.size)
        buffer.put(otherPregnancyRelatedComplaintsBytes)

        buffer.putInt(ageOver80YearsBytes.size)
        buffer.put(ageOver80YearsBytes)

        buffer.putInt(alteredVitalSignsSpo2Bytes.size)
        buffer.put(alteredVitalSignsSpo2Bytes)

        buffer.putInt(alteredVitalSignsRrLowBytes.size)
        buffer.put(alteredVitalSignsRrLowBytes)

        buffer.putInt(alteredVitalSignsRrHighBytes.size)
        buffer.put(alteredVitalSignsRrHighBytes)

        buffer.putInt(alteredVitalSignsHrLowBytes.size)
        buffer.put(alteredVitalSignsHrLowBytes)

        buffer.putInt(alteredVitalSignsHrHighBytes.size)
        buffer.put(alteredVitalSignsHrHighBytes)

        buffer.putInt(alteredVitalSignsSbpLowBytes.size)
        buffer.put(alteredVitalSignsSbpLowBytes)

        buffer.putInt(alteredVitalSignsSbpHighBytes.size)
        buffer.put(alteredVitalSignsSbpHighBytes)

        buffer.putInt(alteredVitalSignsTempLowBytes.size)
        buffer.put(alteredVitalSignsTempLowBytes)

        buffer.putInt(alteredVitalSignsTempHighBytes.size)
        buffer.put(alteredVitalSignsTempHighBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): TriageEvaluation {
        val buffer = ByteBuffer.wrap(bytes)

        val visitId = buffer.readString()
        val unconsciousness = buffer.readString()
        val activeConvulsions = buffer.readString()
        val respiratoryDistress = buffer.readString()
        val heavyBleeding = buffer.readString()
        val highRiskTraumaBurns = buffer.readString()
        val threatenedLimb = buffer.readString()
        val poisoningIntoxication = buffer.readString()
        val snakeBite = buffer.readString()
        val aggressiveBehavior = buffer.readString()
        val pregnancyHeavyBleeding = buffer.readString()
        val severeAbdominalPain = buffer.readString()
        val seizures = buffer.readString()
        val alteredMentalStatus = buffer.readString()
        val severeHeadache = buffer.readString()
        val visualChanges = buffer.readString()
        val sbpHighDpbHigh = buffer.readString()
        val trauma = buffer.readString()
        val activeLabor = buffer.readString()
        val airwaySwellingMass = buffer.readString()
        val ongoingBleeding = buffer.readString()
        val severePallor = buffer.readString()
        val ongoingSevereVomitingDiarrhea = buffer.readString()
        val unableToFeedOrDrink = buffer.readString()
        val recentFainting = buffer.readString()
        val lethargyConfusionAgitation = buffer.readString()
        val focalNeurologicVisualDeficit = buffer.readString()
        val headacheWithStiffNeck = buffer.readString()
        val severePain = buffer.readString()
        val acuteTesticularScrotalPainPriapism = buffer.readString()
        val unableToPassUrine = buffer.readString()
        val acuteLimbDeformityOpenFracture = buffer.readString()
        val otherTraumaBurns = buffer.readString()
        val sexualAssault = buffer.readString()
        val animalBiteNeedlestickPuncture = buffer.readString()
        val otherPregnancyRelatedComplaints = buffer.readString()
        val ageOver80Years = buffer.readString()
        val alteredVitalSignsSpo2 = buffer.readString()
        val alteredVitalSignsRrLow = buffer.readString()
        val alteredVitalSignsRrHigh = buffer.readString()
        val alteredVitalSignsHrLow = buffer.readString()
        val alteredVitalSignsHrHigh = buffer.readString()
        val alteredVitalSignsSbpLow = buffer.readString()
        val alteredVitalSignsSbpHigh = buffer.readString()
        val alteredVitalSignsTempLow = buffer.readString()
        val alteredVitalSignsTempHigh = buffer.readString()

        return TriageEvaluation(
            visitId = visitId,
            unconsciousness = unconsciousness.toBooleanStrict(),
            activeConvulsions = activeConvulsions.toBooleanStrict(),
            respiratoryDistress = respiratoryDistress.toBooleanStrict(),
            heavyBleeding = heavyBleeding.toBooleanStrict(),
            highRiskTraumaBurns = highRiskTraumaBurns.toBooleanStrict(),
            threatenedLimb = threatenedLimb.toBooleanStrict(),
            poisoningIntoxication = poisoningIntoxication.toBooleanStrict(),
            snakeBite = snakeBite.toBooleanStrict(),
            aggressiveBehavior = aggressiveBehavior.toBooleanStrict(),
            pregnancyWithHeavyBleeding = pregnancyHeavyBleeding.toBooleanStrict(),
            pregnancyWithSevereAbdominalPain = severeAbdominalPain.toBooleanStrict(),
            pregnancyWithSeizures = seizures.toBooleanStrict(),
            pregnancyWithAlteredMentalStatus = alteredMentalStatus.toBooleanStrict(),
            pregnancyWithSevereHeadache = severeHeadache.toBooleanStrict(),
            pregnancyWithVisualChanges = visualChanges.toBooleanStrict(),
            pregnancyWithSbpHighDpbHigh = sbpHighDpbHigh.toBooleanStrict(),
            pregnancyWithTrauma = trauma.toBooleanStrict(),
            pregnancyWithActiveLabor = activeLabor.toBooleanStrict(),
            airwaySwellingMass = airwaySwellingMass.toBooleanStrict(),
            ongoingBleeding = ongoingBleeding.toBooleanStrict(),
            severePallor = severePallor.toBooleanStrict(),
            ongoingSevereVomitingDiarrhea = ongoingSevereVomitingDiarrhea.toBooleanStrict(),
            unableToFeedOrDrink = unableToFeedOrDrink.toBooleanStrict(),
            recentFainting = recentFainting.toBooleanStrict(),
            lethargyConfusionAgitation = lethargyConfusionAgitation.toBooleanStrict(),
            focalNeurologicVisualDeficit = focalNeurologicVisualDeficit.toBooleanStrict(),
            headacheWithStiffNeck = headacheWithStiffNeck.toBooleanStrict(),
            severePain = severePain.toBooleanStrict(),
            acuteTesticularScrotalPainPriapism = acuteTesticularScrotalPainPriapism.toBooleanStrict(),
            unableToPassUrine = unableToPassUrine.toBooleanStrict(),
            acuteLimbDeformityOpenFracture = acuteLimbDeformityOpenFracture.toBooleanStrict(),
            otherTraumaBurns = otherTraumaBurns.toBooleanStrict(),
            sexualAssault = sexualAssault.toBooleanStrict(),
            animalBiteNeedlestickPuncture = animalBiteNeedlestickPuncture.toBooleanStrict(),
            otherPregnancyRelatedComplaints = otherPregnancyRelatedComplaints.toBooleanStrict(),
            ageOver80Years = ageOver80Years.toBooleanStrict(),
            alteredVitalSignsSpo2 = alteredVitalSignsSpo2.toBooleanStrict(),
            alteredVitalSignsRrLow = alteredVitalSignsRrLow.toBooleanStrict(),
            alteredVitalSignsRrHigh = alteredVitalSignsRrHigh.toBooleanStrict(),
            alteredVitalSignsHrLow = alteredVitalSignsHrLow.toBooleanStrict(),
            alteredVitalSignsHrHigh = alteredVitalSignsHrHigh.toBooleanStrict(),
            alteredVitalSignsSbpLow = alteredVitalSignsSbpLow.toBooleanStrict(),
            alteredVitalSignsSbpHigh = alteredVitalSignsSbpHigh.toBooleanStrict(),
            alteredVitalSignsTempLow = alteredVitalSignsTempLow.toBooleanStrict(),
            alteredVitalSignsTempHigh = alteredVitalSignsTempHigh.toBooleanStrict()

        )
    }
}