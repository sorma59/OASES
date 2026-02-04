package com.unimib.oases.domain.model.symptom

import com.unimib.oases.util.StringFormatHelper.SnakeCaseString
import com.unimib.oases.util.StringFormatHelper.snakeCase

sealed interface Symptom {
    val symptomId: SymptomId
    val label: String

    val id: String
        get() = symptomId.value.string

    data object Unconsciousness: Symptom {
        override val symptomId = SymptomId.UnconsciousnessId
        override val label = "Unconsciousness"
    }
    data object RespiratoryDistress: Symptom {
        override val symptomId = SymptomId.RespiratoryDistressId
        override val label = "Respiratory distress"
    }
    data object Shock: Symptom {
        override val symptomId = SymptomId.ShockId
        override val label = "Shock"
    }
    data object HeavyBleeding: Symptom {
        override val symptomId = SymptomId.HeavyBleedingId
        override val label = "Heavy bleeding"
    }
    data object SevereDehydration: Symptom {
        override val symptomId = SymptomId.SevereDehydrationId
        override val label = "Severe dehydration"
    }
    data object HighRiskTrauma: Symptom {
        override val symptomId = SymptomId.HighRiskTraumaId
        override val label = "High risk trauma"
    }
    data object MajorBurns: Symptom {
        override val symptomId = SymptomId.MajorBurnsId
        override val label = "Major burns"
    }
    data object ThreatenedLimb: Symptom {
        override val symptomId = SymptomId.ThreatenedLimbId
        override val label = "Threatened limb"
    }
    data object PoisoningIntoxication: Symptom {
        override val symptomId = SymptomId.PoisoningIntoxicationId
        override val label = "Poisoning/intoxication"
    }
    data object AcuteTesticularOrScrotalPainOrPriapism: Symptom {
        override val symptomId = SymptomId.AcuteTesticularOrScrotalPainOrPriapismId
        override val label = "Acute testicular/scrotal pain or priapism"
    }
    data object SnakeBite: Symptom {
        override val symptomId = SymptomId.SnakeBiteId
        override val label = "Snake bite"
    }
    data object AggressiveBehavior: Symptom {
        override val symptomId = SymptomId.AggressiveBehaviorId
        override val label = "Aggressive behavior"
    }
    data object YoungerThanEightDays: Symptom {
        override val symptomId = SymptomId.YoungerThanEightDaysId
        override val label = "Infant < 8 days old"
    }
    data object PretermAndUnderOneMonth: Symptom {
        override val symptomId = SymptomId.PretermAndUnderOneMonthId
        override val label = "Preterm baby < 1 month old"
    }
    data object YoungerThanTwoMonthsAndLowOrHighTemperature: Symptom {
        override val symptomId = SymptomId.YoungerThanTwoMonthsAndLowOrHighTemperatureId
        override val label = "Younger than two months old and low or high temperature"
    }
    data object CurrentPregnancy: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyId
        override val label = "Currently pregnant"
    }
    data object PregnancyWithHighBloodPressure: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithHighBloodPressureId
        override val label = "High blood pressure"
    }
    data object PregnancyWithHeavyBleeding: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithHeavyBleedingId
        override val label = "Heavy bleeding"
    }
    data object PregnancyWithSevereAbdominalPain: Symptom, Pain, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithSevereAbdominalPainId
        override val label = "Severe abdominal pain"
    }
    data object PregnancyWithSeizures: Symptom , Pregnancy, Seizures{
        override val symptomId = SymptomId.PregnancyWithSeizuresId
        override val label = "Seizures"
    }
    data object PregnancyWithAlteredMentalStatus: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithAlteredMentalStatusId
        override val label = "Altered mental status"
    }
    data object PregnancyWithSevereHeadache: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithSevereHeadacheId
        override val label = "Severe headache"
    }
    data object PregnancyWithVisualChanges: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithVisualChangesId
        override val label = "Visual changes"
    }
    data object PregnancyWithTrauma: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithTraumaId
        override val label = "Trauma"
    }
    data object PregnancyWithActiveLabor: Symptom, Pregnancy {
        override val symptomId = SymptomId.PregnancyWithActiveLaborId
        override val label = "Active labor"
    }

    data object AirwaySwellingOrMassOfMouthOrThroatOrNeck: Symptom {
        override val symptomId = SymptomId.AirwaySwellingOrMassOfMouthOrThroatOrNeckId
        override val label = "Airway swelling or mass of mouth, throat or neck"
    }
    data object Wheezing: Symptom {
        override val symptomId = SymptomId.WheezingId
        override val label = "Wheezing"
    }
    data object NonHeavyBleeding: Symptom {
        override val symptomId = SymptomId.NonHeavyBleedingId
        override val label = "Active bleeding (not heavy)"
    }
    data object SeverePallor: Symptom {
        override val symptomId = SymptomId.SeverePallorId
        override val label = "Severe pallor"
    }
    data object OngoingSevereVomitingOrOngoingSevereDiarrhea: Symptom {
        override val symptomId = SymptomId.OngoingSevereVomitingOrOngoingSevereDiarrheaId
        override val label = "Ongoing severe vomiting/diarrhea"
    }
    data object ModerateDehydration: Symptom {
        override val symptomId = SymptomId.ModerateDehydrationId
        override val label = "Moderate dehydration"
    }
    data object UnableToFeedOrDrink: Symptom {
        override val symptomId = SymptomId.UnableToFeedOrDrinkId
        override val label = "Unable to feed or drink"
    }
    data object RecentFainting: Symptom {
        override val symptomId = SymptomId.RecentFaintingId
        override val label = "Recent fainting"
    }
    data object LethargyOrConfusionOrAgitation: Symptom {
        override val symptomId = SymptomId.LethargyOrConfusionOrAgitationId
        override val label = "Lethargy / confusion / agitation"
    }
    data object LethargyOrRestlessOrIrritableOrConfused: Symptom {
        override val symptomId = SymptomId.LethargyOrRestlessOrIrritableOrConfusedId
        override val label = "Lethargic / restless / irritable / confused"
    }
    data object FocalNeurologicDeficitOrFocalVisualDeficit: Symptom {
        override val symptomId = SymptomId.FocalNeurologicDeficitOrFocalVisualDeficitId
        override val label = "Focal neurologic deficit or focal visual deficit"
    }
    data object HeadacheWithStiffNeck: Symptom {
        override val symptomId = SymptomId.HeadacheWithStiffNeckId
        override val label = "Headache with stiff neck"
    }
    data object SeverePain: Symptom, Pain {
        override val symptomId = SymptomId.SeverePainId
        override val label = "Severe pain"
    }
    data object UnableToPassUrine: Symptom {
        override val symptomId = SymptomId.UnableToPassUrineId
        override val label = "Unable to pass urine"
    }
    data object AcuteLimbDeformityOrOpenFracture: Symptom {
        override val symptomId = SymptomId.AcuteLimbDeformityOrOpenFractureId
        override val label = "Acute limb/deformity or open fracture"
    }
    data object NonHighRiskTrauma: Symptom {
        override val symptomId = SymptomId.NonHighRiskTraumaId
        override val label = "Other trauma (non high-risk)"
    }
    data object NonMajorBurns: Symptom {
        override val symptomId = SymptomId.NonMajorBurnsId
        override val label = "Other burns (non major)"
    }
    data object SexualAssault: Symptom {
        override val symptomId = SymptomId.SexualAssaultId
        override val label = "Sexual assault"
    }
    data object AnimalBiteOrNeedlestickPuncture: Symptom {
        override val symptomId = SymptomId.AnimalBiteOrNeedlestickPunctureId
        override val label = "Animal bite or needlestick puncture"
    }
    data object SevereMalnutrition: Symptom {
        override val symptomId = SymptomId.SevereMalnutritionId
        override val label = "Severe malnutrition"
    }
    data object EdemaOfBothFeet: Symptom {
        override val symptomId = SymptomId.EdemaOfBothFeetId
        override val label = "Edema of both feet"
    }
    data object YoungerThanSixMonths: Symptom {
        override val symptomId = SymptomId.YoungerThanSixMonthsId
        override val label = "Age < 6 months"
    }
    data object NonHighRiskPregnancyRelatedComplaints: Symptom {
        override val symptomId = SymptomId.NonHighRiskPregnancyRelatedComplaintsId
        override val label = "Other pregnancy-related complaints"
    }
    data object AgeOverEightyYears: Symptom {
        override val symptomId = SymptomId.AgeOverEightyYearsId
        override val label = "Age > 80 years"
    }
    data object LowSpo2: Symptom {
        override val symptomId = SymptomId.LowSpo2Id
        override val label = "Low SPO2"
    }
    data object LowRr: Symptom {
        override val symptomId = SymptomId.LowRrId
        override val label = "Low RR"
    }
    data object HighRr: Symptom {
        override val symptomId = SymptomId.HighRrId
        override val label = "High RR"
    }
    data object LowTemp: Symptom {
        override val symptomId = SymptomId.LowTempId
        override val label = "Low temperature"
    }
    data object HighTemp: Symptom, Fever {
        override val symptomId = SymptomId.HighTempId
        override val label = "High temperature"
    }
    data object LowRbs: Symptom {
        override val symptomId = SymptomId.LowRbsId
        override val label = "Low blood sugar"
    }
    data object HighRbs: Symptom {
        override val symptomId = SymptomId.HighRbsId
        override val label = "High blood sugar"
    }
    data object LowHr: Symptom {
        override val symptomId = SymptomId.LowHrId
        override val label = "Low HR"
    }
    data object HighHr: Symptom {
        override val symptomId = SymptomId.HighHrId
        override val label = "High HR"
    }
    data object LowSbp: Symptom {
        override val symptomId = SymptomId.LowSbpId
        override val label = "Low SBP"
    }
    data object HighSbp: Symptom {
        override val symptomId = SymptomId.HighSbpId
        override val label = "High SBP"
    }

    data object Hypotension: Symptom {
        override val symptomId = SymptomId.HypotensionId
        override val label = "Hypotension"
    }

    data object HypertensiveEmergency: Symptom {
        override val symptomId = SymptomId.HypertensiveEmergencyId
        override val label = "Hypertensive emergency"
    }

    //---------------------------------------------------

    data object DiarrheaInTheLastOneToSevenDays: Symptom, DiarrheaDuration {
        override val symptomId = SymptomId.DiarrheaInTheLastOneToSevenDaysId
        override val label = "1-7 days"
    }

    data object DiarrheaInTheLastEightToFourteenDays: Symptom, DiarrheaDuration {
        override val symptomId = SymptomId.DiarrheaInTheLastEightToFourteenDaysId
        override val label = "8-14 days"
    }

    data object DiarrheaInTheLastFifteenToThirtyDays: Symptom, DiarrheaDuration{
        override val symptomId = SymptomId.DiarrheaInTheLastFifteenToThirtyDaysId
        override val label = "15-30 days"
    }

    data object DiarrheaInTheLastThirtyPlusDays: Symptom, DiarrheaDuration {
        override val symptomId = SymptomId.DiarrheaInTheLastThirtyPlusDaysId
        override val label = "30+ days"
    }


    data object DiarrheaEpisodesOnceOrTwiceADay: Symptom, DiarrheaFrequency {
        override val symptomId = SymptomId.DiarrheaEpisodesOnceOrTwiceADayId
        override val label = "1-2 episodes/day"
    }

    data object DiarrheaEpisodesThreeToFiveTimesADay: Symptom, DiarrheaFrequency {
        override val symptomId = SymptomId.DiarrheaEpisodesThreeToFiveTimesADayId
        override val label = "3-5 episodes/day"
    }

    data object DiarrheaEpisodesSixOrMoreTimesADay: Symptom, DiarrheaFrequency {
        override val symptomId = SymptomId.DiarrheaEpisodesSixOrMoreTimesADayId
        override val label = "6+ episodes/day"
    }

    data object DiarrheaWateryStools: Symptom, DiarrheaAspect {
        override val symptomId = SymptomId.DiarrheaWateryStoolsId
        override val label = "watery"
    }

    data object DiarrheaBloodyStools: Symptom, DiarrheaAspect {
        override val symptomId = SymptomId.DiarrheaBloodyStoolsId
        override val label = "with blood"
    }

    data object DiarrheaOilyOrGreasyOrFoulSmellingStools: Symptom, DiarrheaAspect {
        override val symptomId = SymptomId.DiarrheaOilyOrGreasyOrFoulSmellingStoolsId
        override val label = "oily/greasy/foul-smelling"
    }

    // Dyspnea

    data object DyspneaInTheLastOneToSevenDays: Symptom, DyspneaDuration {
        override val symptomId = SymptomId.DyspneaInTheLastOneToSevenDaysId
        override val label = "1-7 days"
    }

    data object DyspneaInTheLastEightToFourteenDays: Symptom, DyspneaDuration {
        override val symptomId = SymptomId.DyspneaInTheLastEightToFourteenDaysId
        override val label = "8-14 days"
    }

    data object DyspneaInTheLastFifteenToThirtyDays: Symptom, DyspneaDuration {
        override val symptomId = SymptomId.DyspneaInTheLastFifteenToThirtyDaysId
        override val label = "15-30 days"
    }

    data object DyspneaInTheLastThirtyPlusDays: Symptom, DyspneaDuration {
        override val symptomId = SymptomId.DyspneaInTheLastThirtyPlusDaysId
        override val label = "30+ days"
    }

    data object DyspneaAcuteCourse: Symptom, DyspneaCourse {
        override val symptomId = SymptomId.DyspneaAcuteCourseId
        override val label = "Acute (new onset)"
    }

    data object DyspneaProgressiveCourse: Symptom, DyspneaCourse {
        override val symptomId = SymptomId.DyspneaProgressiveCourseId
        override val label = "Progressive (worsening over days/weeks)"
    }

    data object DyspneaRecurrentCourse: Symptom, DyspneaCourse {
        override val symptomId = SymptomId.DyspneaRecurrentCourseId
        override val label = "Recurrent (previous similar episodes with symptom-free gaps)"
    }

    data object DyspneaDryCough: Symptom, DyspneaCoughAspect {
        override val symptomId = SymptomId.DyspneaDryCoughId
        override val label = "dry"
    }

    data object DyspneaCoughWithMucous: Symptom, DyspneaCoughAspect {
        override val symptomId = SymptomId.DyspneaCoughWithMucousId
        override val label = "with mucous"
    }

    data object DyspneaCoughWithBlood: Symptom, DyspneaCoughAspect {
        override val symptomId = SymptomId.DyspneaCoughWithBloodId
        override val label = "with blood"
    }

    data object DyspneaParoxysmalCoughWithWhoopsOrCentralCyanosisOrVomiting: Symptom, DyspneaCoughAspect {
        override val symptomId = SymptomId.DyspneaParoxysmalCoughWithWhoopsOrCentralCyanosisOrVomitingId
        override val label = "paroxysms with whoops/central cyanosis/vomiting"
    }

    // Seizures-Coma

    data object AlertLevelOfConsciousness: Symptom, SeizuresOrComaConsciousness {
        override val symptomId = SymptomId.AlertLevelOfConsciousnessId
        override val label = "A (Alert)"
    }

    data object ResponsiveToVoiceLevelOfConsciousness: Symptom, SeizuresOrComaConsciousness {
        override val symptomId = SymptomId.ResponsiveToVoiceLevelOfConsciousnessId
        override val label = "V (Responsive to Voice)"
    }

    data object ResponsiveToPainLevelOfConsciousness: Symptom, SeizuresOrComaConsciousness {
        override val symptomId = SymptomId.ResponsiveToPainLevelOfConsciousnessId
        override val label = "P (Responsive to Pain)"
    }

    data object UnresponsiveLevelOfConsciousness: Symptom, SeizuresOrComaConsciousness {
        override val symptomId = SymptomId.UnresponsiveLevelOfConsciousnessId
        override val label = "U (Unresponsive)"
    }

    data object SeizuresOrComaInTheLastOneToSevenDays: Symptom, SeizuresOrComaDuration {
        override val symptomId = SymptomId.SeizuresOrComaInTheLastOneToSevenDaysId
        override val label = "1-7 days"
    }

    data object SeizuresOrComaInTheLastEightToFourteenDays: Symptom, SeizuresOrComaDuration {
        override val symptomId = SymptomId.SeizuresOrComaInTheLastEightToFourteenDaysId
        override val label = "8-14 days"
    }

    data object SeizuresOrComaInTheLastFifteenToThirtyDays: Symptom, SeizuresOrComaDuration {
        override val symptomId = SymptomId.SeizuresOrComaInTheLastFifteenToThirtyDaysId
        override val label = "15-30 days"
    }

    data object SeizuresOrComaInTheLastThirtyPlusDays: Symptom, SeizuresOrComaDuration {
        override val symptomId = SymptomId.SeizuresOrComaInTheLastThirtyPlusDaysId
        override val label = "30+ days"
    }

    data object StatusEpilepticus: Symptom, AbsenceOrPresenceOfStatusEpilepticus {
        override val symptomId = SymptomId.StatusEpilepticusId
        override val label = "Status epilepticus"
    }

    data object AbsenceOfStatusEpilepticus: Symptom, AbsenceOrPresenceOfStatusEpilepticus {
        override val symptomId = SymptomId.AbsenceOfStatusEpilepticusId
        override val label = "Absence of status epilepticus"
    }

    data object SeizuresOrComaFocalSeizures: Symptom, Seizures, SeizuresType {
        override val symptomId = SymptomId.SeizuresOrComaFocalSeizuresId
        override val label = "focal"
    }

    data object SeizuresOrComaGeneralizedMotorSeizures: Symptom, Seizures, SeizuresType {
        override val symptomId = SymptomId.SeizuresOrComaGeneralizedMotorSeizuresId
        override val label = "generalized motor (e.g. tonic-clonic)"
    }

    data object SeizuresOrComaAbsenceOfSeizures: Symptom, SeizuresType {
        override val symptomId = SymptomId.SeizuresOrComaAbsenceOfSeizuresId
        override val label = "absence"
    }
    //--------------------------------------------

    data object Vomiting: Symptom {
        override val symptomId = SymptomId.VomitingId
        override val label = "Vomiting"
    }

    data object AbdominalPain: Symptom, Pain {
        override val symptomId = SymptomId.AbdominalPainId
        override val label = "Abdominal pain"
    }

    data object AbdominalDistensionAndTendernessWithAlteredBowelSounds: Symptom {
        override val symptomId = SymptomId.AbdominalDistensionAndTendernessWithAlteredBowelSoundsId
        override val label = "Abdominal distension and tenderness with altered bowel sounds"
    }

    data object WeightLoss: Symptom {
        override val symptomId = SymptomId.WeightLossId
        override val label = "Weight loss"
    }

    data object Malnutrition: Symptom {
        override val symptomId = SymptomId.MalnutritionId
        override val label = "Malnutrition"
    }

    data object FeverAbove38Degrees: Symptom, Fever {
        override val symptomId = SymptomId.FeverAboveThirtyEightDegrees
        override val label = "Fever > 38°C"
    }

    data object Convulsions: Symptom {
        override val symptomId = SymptomId.ConvulsionsId
        override val label = "Convulsions"
    }
    
    data object Stridor: Symptom {
        override val symptomId = SymptomId.StridorId
        override val label = "Stridor"
    }

    data object EasyBruising: Symptom {
        override val symptomId = SymptomId.EasyBruisingId
        override val label = "Easy bruising"
    }

    data object Hypoglycemia: Symptom {
        override val symptomId = SymptomId.HypoglycemiaId
        override val label = "Hypoglycemia"
    }

    data object NightSweats: Symptom {
        override val symptomId = SymptomId.NightSweatsId
        override val label = "Night sweats"
    }

    data object ChestPain: Symptom, Pain {
        override val symptomId = SymptomId.ChestPainId
        override val label = "Chest pain"
    }

    data object OrthopneaOrParoxysmalNocturnalDyspnea: Symptom {
        override val symptomId = SymptomId.OrthopneaOrParoxysmalNocturnalDyspneaId
        override val label = "Orthopnea/paroxysmal nocturnal dyspnea"
    }

    data object RunnyNoseOrSneezingOrSoreThroat: Symptom {
        override val symptomId = SymptomId.RunnyNoseOrSneezingOrSoreThroatId
        override val label = "Runny nose/sneezing/sore throat"
    }

    data object Lethargy: Symptom {
        override val symptomId = SymptomId.LethargyId
        override val label = "Lethargy"
    }

    data object InabilityToBreastfeedOrDrink: Symptom {
        override val symptomId = SymptomId.InabilityToBreastfeedOrDrinkId
        override val label = "Inability to breastfeed/drink"
    }

    data object DecreasedBreathSoundsAtChestAuscultation: Symptom {
        override val symptomId = SymptomId.DecreasedBreathSoundsAtChestAuscultationId
        override val label = "Decreased breath sounds at chest aspiration"
    }

    data object CracklesAtChestAuscultation: Symptom {
        override val symptomId = SymptomId.CracklesAtChestAuscultationId
        override val label = "Crackles at chest aspiration"
    }

    data object CardiacMurmurAtChestAuscultation: Symptom {
        override val symptomId = SymptomId.CardiacMurmurAtChestAuscultationId
        override val label = "Cardiac murmur at chest aspiration"
    }

    data object IrregularPulseOrIrregularHeartSounds: Symptom {
        override val symptomId = SymptomId.IrregularPulseOrIrregularHeartSoundsId
        override val label = "Irregular pulse/heart sounds"
    }

    data object PeripheralEdemaOrJugularVenousDistension: Symptom {
        override val symptomId = SymptomId.PeripheralEdemaOrJugularVenousDistensionId
        override val label = "Peripheral edema/jugular venous distension"
    }

    data object SwellingOrPainOrWarmthOrRednessOfOneLeg: Symptom {
        override val symptomId = SymptomId.SwellingOrPainOrWarmthOrRednessOfOneLegId
        override val label = "Swelling, pain, warmth, redness of one leg"
    }

    // Seizures-Coma

    data object IrritabilityOrConfusion: Symptom {
        override val symptomId = SymptomId.IrritabilityOrConfusionId
        override val label = "Irritability or confusion"
    }

    data object HeadacheOrCervicalPain: Symptom {
        override val symptomId = SymptomId.HeadacheOrCervicalPainId
        override val label = "Headache or cervical pain"
    }

    data object StiffNeck: Symptom {
        override val symptomId = SymptomId.StiffNeckId
        override val label = "Stiff neck"
    }

    data object BulgingFontanelle: Symptom {
        override val symptomId = SymptomId.BulgingFontanelleId
        override val label = "Bulging fontanelle"
    }

    data object FocalNeurologicalDeficit: Symptom {
        override val symptomId = SymptomId.FocalNeurologicalDeficitId
        override val label = "Focal neurological deficit"
    }

    data object UnequalPupils: Symptom {
        override val symptomId = SymptomId.UnequalPupilsId
        override val label = "Unequal pupils"
    }

    data object Opisthotonus: Symptom {
        override val symptomId = SymptomId.OpisthotonusId
        override val label = "Opisthotonus (rigid posture)"
    }

    data object PetechialRashOrPurpura: Symptom {
        override val symptomId = SymptomId.PetechialRashOrPurpuraId
        override val label = "Petechial rash or purpura"
    }

    data object Jaundice: Symptom {
        override val symptomId = SymptomId.JaundiceId
        override val label = "Jaundice"
    }
    // --------High-risk------------

    data object HivPositive: Symptom {
        override val symptomId = SymptomId.HivPositiveId
        override val label = "HIV positive"
    }

    data object CholeraOutbreak: Symptom {
        override val symptomId = SymptomId.CholeraOutbreakId
        override val label = "Cholera outbreak in the region the patient comes from"
    }

    // Dyspnea

    data object RecentChokingOrForeignBodyInhalation: Symptom {
        override val symptomId = SymptomId.RecentChokingOrForeignBodyInhalationId
        override val label = "Recent choking / foreign body inhalation"
    }

    data object RecentChestTrauma: Symptom {
        override val symptomId = SymptomId.RecentChestTraumaId
        override val label = "Recent trauma to the chest"
    }

    data object CloseContactWithKnownTuberculosisPatient: Symptom {
        override val symptomId = SymptomId.CloseContactWithKnownTuberculosisPatientId
        override val label = "Close contact with a known TB patient"
    }

    data object AsthmaCOPDHistory: Symptom {
        override val symptomId = SymptomId.AsthmaCOPDHistoryId
        override val label = "Asthma/COPD history"
    }

    data object HistoryOfHeartDisease: Symptom {
        override val symptomId = SymptomId.HistoryOfHeartDiseaseId
        override val label = "History of heart disease"
    }

    data object HistoryOfRenalOrLiverDisease: Symptom {
        override val symptomId = SymptomId.HistoryOfRenalOrLiverDiseaseId
        override val label = "History of renal/liver disease"
    }

    data object SickleCellDisease: Symptom {
        override val symptomId = SymptomId.SickleCellDiseaseId
        override val label = "Sickle cell disease"
    }

    data object ProlongedImmobilizationOrBedridden: Symptom {
        override val symptomId = SymptomId.ProlongedImmobilizationOrBedriddenId
        override val label = "Prolonged immobilization/bedridden"
    }

    data object Unvaccinated: Symptom {
        override val symptomId = SymptomId.UnvaccinatedId
        override val label = "Unvaccinated or incomplete vaccination"
    }

    data object SmokingOrExposedToSmoke: Symptom {
        override val symptomId = SymptomId.SmokingOrExposedToSmokeId
        override val label = "Smoking / exposure to smoke"
    }

    // Seizures-Coma
    data object EpilepsyOrHistoryOfRecurrentUnprovokedSeizures: Symptom {
        override val symptomId = SymptomId.EpilepsyOrHistoryOfRecurrentUnprovokedSeizuresId
        override val label = "Known epilepsy or history of recurrent unprovoked seizures"
    }

    data object HeadInjury: Symptom {
        override val symptomId = SymptomId.HeadInjuryId
        override val label = "Head injury"
    }

    data object SuspectOfDrugOrToxinIngestion: Symptom {
        override val symptomId = SymptomId.SuspectOfDrugOrToxinIngestionId
        override val label = "Suspect of drug/toxin ingestion"
    }

    data object SuspectOfAlcoholUseOrWithdrawal: Symptom {
        override val symptomId = SymptomId.SuspectOfAlcoholUseOrWithdrawalId
        override val label = "Suspect of alcohol use/withdrawal"
    }

    data object Diabetes: Symptom {
        override val symptomId = SymptomId.DiabetesId
        override val label = "Diabetes"
    }
}

sealed interface Pain
sealed interface Fever
sealed interface Pregnancy
sealed interface Seizures

sealed interface DiarrheaDuration
sealed interface DiarrheaFrequency
sealed interface DiarrheaAspect
sealed interface DyspneaDuration
sealed interface DyspneaCourse
sealed interface DyspneaCoughAspect
sealed interface AbsenceOrPresenceOfStatusEpilepticus
sealed interface SeizuresOrComaConsciousness
sealed interface SeizuresOrComaDuration
sealed interface SeizuresType

/**
 * Sealed interface representing the unique identifier for a symptom.
 * Each implementing object represents a specific symptom type.
 *
 * The `id` property is a [SnakeCaseString] which provides a standardized,
 * machine-readable representation of the symptom.
 */
sealed class SymptomId (
    val value: SnakeCaseString
){
    object UnconsciousnessId: SymptomId(snakeCase("unconsciousness"))
    object RespiratoryDistressId: SymptomId(snakeCase("respiratory_distress"))
    object ShockId: SymptomId(snakeCase("shock"))
    object HeavyBleedingId: SymptomId(snakeCase("heavy_bleeding"))
    object SevereDehydrationId: SymptomId(snakeCase("severe_dehydration"))
    object HighRiskTraumaId: SymptomId(snakeCase("high_risk_trauma"))
    object MajorBurnsId: SymptomId(snakeCase("major_burns"))
    object ThreatenedLimbId: SymptomId(snakeCase("threatened_limb"))
    object PoisoningIntoxicationId: SymptomId(snakeCase("poisoning_intoxication"))
    object AcuteTesticularOrScrotalPainOrPriapismId: SymptomId(snakeCase("acute_testicular_or_scrotal_pain_or_priapism"))
    object SnakeBiteId: SymptomId(snakeCase("snake_bite"))
    object AggressiveBehaviorId: SymptomId(snakeCase("aggressive_behavior"))
    object YoungerThanEightDaysId: SymptomId(snakeCase("younger_than_eight_days"))
    object PretermAndUnderOneMonthId: SymptomId(snakeCase("preterm_and_under_one_month"))
    object YoungerThanTwoMonthsAndLowOrHighTemperatureId: SymptomId(snakeCase("younger_than_two_months_and_low_or_high_temperature"))
    object PregnancyId: SymptomId(snakeCase("pregnancy"))
    object PregnancyWithHighBloodPressureId: SymptomId(snakeCase("pregnancy_with_high_blood_pressure"))
    object PregnancyWithHeavyBleedingId: SymptomId(snakeCase("pregnancy_with_heavy_bleeding"))
    object PregnancyWithSevereAbdominalPainId: SymptomId(snakeCase("pregnancy_with_severe_abdominal_pain"))
    object PregnancyWithSeizuresId: SymptomId(snakeCase("pregnancy_with_seizures"))
    object PregnancyWithAlteredMentalStatusId: SymptomId(snakeCase("pregnancy_with_altered_mental_status"))
    object PregnancyWithSevereHeadacheId: SymptomId(snakeCase("pregnancy_with_severe_headache"))
    object PregnancyWithVisualChangesId: SymptomId(snakeCase("pregnancy_with_visual_changes"))
    object PregnancyWithTraumaId: SymptomId(snakeCase("pregnancy_with_trauma"))
    object PregnancyWithActiveLaborId: SymptomId(snakeCase("pregnancy_with_active_labor"))

    object AirwaySwellingOrMassOfMouthOrThroatOrNeckId: SymptomId(snakeCase("airway_swelling_or_mass_of_mouth_or_throat_or_neck"))
    object WheezingId: SymptomId(snakeCase("wheezing"))
    object NonHeavyBleedingId: SymptomId(snakeCase("non_heavy_bleeding"))
    object SeverePallorId: SymptomId(snakeCase("severe_pallor"))
    object OngoingSevereVomitingOrOngoingSevereDiarrheaId: SymptomId(snakeCase("ongoing_severe_vomiting_or_ongoing_severe_diarrhea"))
    object ModerateDehydrationId: SymptomId(snakeCase("moderate_dehydration"))
    object UnableToFeedOrDrinkId: SymptomId(snakeCase("unable_to_feed_or_drink"))
    object RecentFaintingId: SymptomId(snakeCase("recent_fainting"))
    object LethargyOrConfusionOrAgitationId: SymptomId(snakeCase("lethargy_or_confusion_or_agitation"))
    object LethargyOrRestlessOrIrritableOrConfusedId: SymptomId(snakeCase("lethargy_or_restless_or_irritable_or_confused"))
    object FocalNeurologicDeficitOrFocalVisualDeficitId: SymptomId(snakeCase("focal_neurologic_deficit_or_focal_visual_deficit"))
    object HeadacheWithStiffNeckId: SymptomId(snakeCase("headache_with_stiff_neck"))
    object SeverePainId: SymptomId(snakeCase("severe_pain"))
    object UnableToPassUrineId: SymptomId(snakeCase("unable_to_pass_urine"))
    object AcuteLimbDeformityOrOpenFractureId: SymptomId(snakeCase("acute_limb_deformity_or_open_fracture"))
    object NonHighRiskTraumaId: SymptomId(snakeCase("non_high_risk_trauma"))
    object NonMajorBurnsId: SymptomId(snakeCase("non_major_burns"))
    object SexualAssaultId: SymptomId(snakeCase("sexual_assault"))
    object AnimalBiteOrNeedlestickPunctureId: SymptomId(snakeCase("animal_bite_or_needlestick_puncture"))
    object SevereMalnutritionId: SymptomId(snakeCase("severe_malnutrition"))
    object EdemaOfBothFeetId: SymptomId(snakeCase("edema_of_both_feet"))
    object YoungerThanSixMonthsId: SymptomId(snakeCase("younger_than_six_months"))
    object NonHighRiskPregnancyRelatedComplaintsId: SymptomId(snakeCase("non_high_risk_pregnancy_related_complaints"))
    object AgeOverEightyYearsId: SymptomId(snakeCase("age_over_eighty_years"))
    object LowSpo2Id: SymptomId(snakeCase("low_spo2"))
    object LowRrId: SymptomId(snakeCase("low_rr"))
    object HighRrId: SymptomId(snakeCase("high_rr"))
    object LowTempId: SymptomId(snakeCase("low_temp"))
    object HighTempId: SymptomId(snakeCase("high_temp"))
    object LowRbsId: SymptomId(snakeCase("low_rbs"))
    object HighRbsId: SymptomId(snakeCase("high_rbs"))
    object LowHrId: SymptomId(snakeCase("low_hr"))
    object HighHrId: SymptomId(snakeCase("high_hr"))
    object LowSbpId: SymptomId(snakeCase("low_sbp"))
    object HighSbpId: SymptomId(snakeCase("high_sbp"))

    object HypertensiveEmergencyId: SymptomId(snakeCase("hypertensive_emergency"))
    object HypotensionId: SymptomId(snakeCase("hypotension"))

    //---------------------------------------------------------------

    // Diarrhea
    object DiarrheaInTheLastOneToSevenDaysId: SymptomId(snakeCase("diarrhea_in_the_last_one_to_seven_days"))
    object DiarrheaInTheLastEightToFourteenDaysId: SymptomId(snakeCase("diarrhea_in_the_last_eight_to_fourteen_days"))
    object DiarrheaInTheLastFifteenToThirtyDaysId: SymptomId(snakeCase("diarrhea_in_the_last_fifteen_to_thirty_days"))
    object DiarrheaInTheLastThirtyPlusDaysId: SymptomId(snakeCase("diarrhea_in_the_last_thirty_plus_days"))

    object DiarrheaEpisodesOnceOrTwiceADayId: SymptomId(snakeCase("diarrhea_episodes_once_or_twice_a_day"))
    object DiarrheaEpisodesThreeToFiveTimesADayId: SymptomId(snakeCase("diarrhea_episodes_three_to_five_times_a_day"))
    object DiarrheaEpisodesSixOrMoreTimesADayId: SymptomId(snakeCase("diarrhea_episodes_six_or_more_times_a_day"))

    object DiarrheaWateryStoolsId: SymptomId(snakeCase("diarrhea_watery_stools"))
    object DiarrheaBloodyStoolsId: SymptomId(snakeCase("diarrhea_bloody_stools"))
    object DiarrheaOilyOrGreasyOrFoulSmellingStoolsId: SymptomId(snakeCase("diarrhea_oily_or_greasy_or_foul_smelling_stools"))

    // Dyspnea
    object DyspneaInTheLastOneToSevenDaysId: SymptomId(snakeCase("dyspnea_in_the_last_one_to_seven_days"))
    object DyspneaInTheLastEightToFourteenDaysId: SymptomId(snakeCase("dyspnea_in_the_last_eight_to_fourteen_days"))
    object DyspneaInTheLastFifteenToThirtyDaysId: SymptomId(snakeCase("dyspnea_in_the_last_fifteen_to_thirty_days"))
    object DyspneaInTheLastThirtyPlusDaysId: SymptomId(snakeCase("dyspnea_in_the_last_thirty_plus_days"))

    object DyspneaAcuteCourseId: SymptomId(snakeCase("dyspnea_acute_course"))
    object DyspneaProgressiveCourseId: SymptomId(snakeCase("dyspnea_progressive_course"))
    object DyspneaRecurrentCourseId: SymptomId(snakeCase("dyspnea_recurrent_course"))

    object DyspneaDryCoughId: SymptomId(snakeCase("dyspnea_dry_cough"))
    object DyspneaCoughWithMucousId: SymptomId(snakeCase("dyspnea_cough_with_mucous"))
    object DyspneaCoughWithBloodId: SymptomId(snakeCase("dyspnea_cough_with_blood"))
    object DyspneaParoxysmalCoughWithWhoopsOrCentralCyanosisOrVomitingId: SymptomId(snakeCase("dyspnea_paroxysmal_cough_with_whoops_or_central_cyanosis_or_vomiting"))

    // Seizures-Coma
    object AlertLevelOfConsciousnessId: SymptomId(snakeCase("alert_level_of_consciousness"))
    object ResponsiveToVoiceLevelOfConsciousnessId: SymptomId(snakeCase("responsive_to_voice_level_of_consciousness"))
    object ResponsiveToPainLevelOfConsciousnessId: SymptomId(snakeCase("responsive_to_pain_level_of_consciousness"))
    object UnresponsiveLevelOfConsciousnessId: SymptomId(snakeCase("unresponsive_level_of_consciousness"))

    object SeizuresOrComaInTheLastOneToSevenDaysId: SymptomId(snakeCase("seizures_or_coma_in_the_last_one_to_seven_days"))
    object SeizuresOrComaInTheLastEightToFourteenDaysId: SymptomId(snakeCase("seizures_or_coma_in_the_last_eight_to_fourteen_days"))
    object SeizuresOrComaInTheLastFifteenToThirtyDaysId: SymptomId(snakeCase("seizures_or_coma_in_the_last_fifteen_to_thirty_days"))
    object SeizuresOrComaInTheLastThirtyPlusDaysId: SymptomId(snakeCase("seizures_or_coma_in_the_last_thirty_plus_days"))

    object StatusEpilepticusId: SymptomId(snakeCase("status_epilepticus"))
    object AbsenceOfStatusEpilepticusId: SymptomId(snakeCase("absence_of_status_epilepticus"))

    object SeizuresOrComaFocalSeizuresId: SymptomId(snakeCase("seizures_or_coma_focal_seizures"))
    object SeizuresOrComaGeneralizedMotorSeizuresId: SymptomId(snakeCase("seizures_or_coma_generalized_motor_seizures"))
    object SeizuresOrComaAbsenceOfSeizuresId: SymptomId(snakeCase("seizures_or_coma_absence_of_seizures"))
    //--------------------------------------------------------------

    object VomitingId: SymptomId(snakeCase("vomiting"))
    object AbdominalPainId: SymptomId(snakeCase("abdominal_pain"))
    object AbdominalDistensionAndTendernessWithAlteredBowelSoundsId: SymptomId(snakeCase("abdominal_distension_and_tenderness_with_altered_bowel_sounds"))
    object WeightLossId: SymptomId(snakeCase("weight_loss"))
    object MalnutritionId: SymptomId(snakeCase("malnutrition"))
    object FeverAboveThirtyEightDegrees: SymptomId(snakeCase("fever_above_thirty_eight_degrees"))
    object ConvulsionsId: SymptomId(snakeCase("convulsions"))
    object StridorId: SymptomId(snakeCase("stridor"))
    object EasyBruisingId: SymptomId(snakeCase("easy_bruising"))
    object HypoglycemiaId: SymptomId(snakeCase("hypoglycemia"))

    object NightSweatsId: SymptomId(snakeCase("night_sweats"))
    object ChestPainId: SymptomId(snakeCase("chest_pain"))
    object OrthopneaOrParoxysmalNocturnalDyspneaId: SymptomId(snakeCase("orthopnea_or_paroxysmal_nocturnal_dyspnea"))
    object RunnyNoseOrSneezingOrSoreThroatId: SymptomId(snakeCase("runny_nose_or_sneezing_or_sore_throat"))
    object LethargyId: SymptomId(snakeCase("lethargy"))
    object InabilityToBreastfeedOrDrinkId: SymptomId(snakeCase("inability_to_breastfeed_or_drink"))
    object DecreasedBreathSoundsAtChestAuscultationId: SymptomId(snakeCase("decreased_breath_sounds_at_chest_auscultation"))
    object CracklesAtChestAuscultationId: SymptomId(snakeCase("crackles_at_chest_auscultation"))
    object CardiacMurmurAtChestAuscultationId: SymptomId(snakeCase("cardiac_murmur_at_chest_auscultation"))
    object IrregularPulseOrIrregularHeartSoundsId: SymptomId(snakeCase("irregular_pulse_or_irregular_heart_sounds"))
    object PeripheralEdemaOrJugularVenousDistensionId: SymptomId(snakeCase("peripheral_edema_or_jugular_venous_distension"))
    object SwellingOrPainOrWarmthOrRednessOfOneLegId: SymptomId(snakeCase("swelling_or_pain_or_warmth_or_redness_of_one_leg"))

    object IrritabilityOrConfusionId: SymptomId(snakeCase("irritability_or_confusion"))
    object HeadacheOrCervicalPainId: SymptomId(snakeCase("headache_or_cervical_pain"))
    object StiffNeckId: SymptomId(snakeCase("stiff_neck"))
    object BulgingFontanelleId: SymptomId(snakeCase("bulging_fontanelle"))
    object FocalNeurologicalDeficitId: SymptomId(snakeCase("focal_neurological_deficit"))
    object UnequalPupilsId: SymptomId(snakeCase("unequal_pupils"))
    object OpisthotonusId: SymptomId(snakeCase("opisthotonus"))
    object PetechialRashOrPurpuraId: SymptomId(snakeCase("petechial_rash_or_purpura"))
    object JaundiceId: SymptomId(snakeCase("jaundice"))

    object HivPositiveId: SymptomId(snakeCase("hiv_positive"))
    object CholeraOutbreakId: SymptomId(snakeCase("cholera_outbreak"))

    object RecentChokingOrForeignBodyInhalationId: SymptomId(snakeCase("recent_choking_or_foreign_body_inhalation"))
    object RecentChestTraumaId: SymptomId(snakeCase("recent_chest_trauma"))
    object CloseContactWithKnownTuberculosisPatientId: SymptomId(snakeCase("close_contact_with_known_tuberculosis_patient"))
    object AsthmaCOPDHistoryId: SymptomId(snakeCase("asthma_copd_history"))
    object HistoryOfHeartDiseaseId: SymptomId(snakeCase("history_of_heart_disease"))
    object HistoryOfRenalOrLiverDiseaseId: SymptomId(snakeCase("history_of_renal_or_liver_disease"))
    object SickleCellDiseaseId: SymptomId(snakeCase("sickle_cell_disease"))
    object ProlongedImmobilizationOrBedriddenId: SymptomId(snakeCase("prolonged_immobilization_or_bedridden"))
    object UnvaccinatedId: SymptomId(snakeCase("unvaccinated"))
    object SmokingOrExposedToSmokeId: SymptomId(snakeCase("smoking_or_exposed_to_smoke"))

    object EpilepsyOrHistoryOfRecurrentUnprovokedSeizuresId: SymptomId(snakeCase("epilepsy_or_history_of_recurrent_unprovoked_seizures"))
    object HeadInjuryId: SymptomId(snakeCase("head_injury"))
    object SuspectOfDrugOrToxinIngestionId: SymptomId(snakeCase("suspect_of_drug_or_toxin_ingestion"))
    object SuspectOfAlcoholUseOrWithdrawalId: SymptomId(snakeCase("suspect_of_alcohol_use_or_withdrawal"))
    object DiabetesId: SymptomId(snakeCase("diabetes"))
}


