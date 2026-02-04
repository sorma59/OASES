package com.unimib.oases.domain.model.symptom

enum class PatientCategory {
    PEDIATRIC, ADULT
}

enum class SymptomTriageCode {
    RED, YELLOW
}

val redForAll = { _: PatientCategory -> SymptomTriageCode.RED }

val yellowForAll = { _: PatientCategory -> SymptomTriageCode.YELLOW }

val yellowForAdultRedForKid = { category: PatientCategory ->
    when (category) {
        PatientCategory.PEDIATRIC -> SymptomTriageCode.RED
        PatientCategory.ADULT -> SymptomTriageCode.YELLOW
    }
}

val redForAdultNullForKid = { category: PatientCategory ->
    when (category) {
        PatientCategory.PEDIATRIC -> null
        PatientCategory.ADULT -> SymptomTriageCode.RED
    }
}

val yellowForAdultNullForKid = { category: PatientCategory ->
    when (category) {
        PatientCategory.PEDIATRIC -> null
        PatientCategory.ADULT -> SymptomTriageCode.YELLOW
    }
}

val nullForAdultYellowForKid = { category: PatientCategory ->
    when (category) {
        PatientCategory.PEDIATRIC -> SymptomTriageCode.YELLOW
        PatientCategory.ADULT -> null
    }
}

val nullForAdultRedForKid = { category: PatientCategory ->
    when (category) {
        PatientCategory.PEDIATRIC -> SymptomTriageCode.RED
        PatientCategory.ADULT -> null
    }
}

enum class TriageSymptom(
    val symptom: Symptom,
    val colorAssigner: (PatientCategory) -> SymptomTriageCode?,
    val parent: TriageSymptom? = null,
    val isParent: Boolean = false,
    val isComputed: Boolean = false
) {
    UNCONSCIOUSNESS(
        Symptom.Unconsciousness,
        redForAll
    ),
    CONVULSIONS(
        Symptom.Convulsions,
        redForAll,
    ),
    STRIDOR(
        Symptom.Stridor,
        redForAll
    ),
    RESPIRATORY_DISTRESS(
        Symptom.RespiratoryDistress,
        redForAll,
    ),
    SHOCK(
        Symptom.Shock,
        redForAll,
    ),
    HEAVY_ACTIVE_BLEEDING(
        Symptom.HeavyBleeding,
        redForAll
    ),
//    SEVERE_DEHYDRATION(
//        SevereDehydration,
//        nullForAdultRedForKid
//    ),
    HIGH_RISK_TRAUMA(
        Symptom.HighRiskTrauma,
        redForAll
    ),
    MAJOR_BURNS(
        Symptom.MajorBurns,
        redForAll
    ),
    THREATENED_LIMB(
        Symptom.ThreatenedLimb,
        redForAll
    ),
    POISONING_INTOXICATION(
        Symptom.PoisoningIntoxication,
        redForAll
    ),
    ACUTE_TESTICULAR_OR_SCROTAL_PAIN_OR_PRIAPISM(
        Symptom.AcuteTesticularOrScrotalPainOrPriapism,
        yellowForAdultRedForKid
    ),
    SNAKE_BITE(
        Symptom.SnakeBite,
        redForAll
    ),
    AGGRESSIVE_BEHAVIOR(
        Symptom.AggressiveBehavior,
        redForAdultNullForKid
    ),
    YOUNGER_THAN_EIGHT_DAYS(
        Symptom.YoungerThanEightDays,
        nullForAdultRedForKid
    ),
    PRETERM_AND_UNDER_ONE_MONTH(
        Symptom.PretermAndUnderOneMonth,
        nullForAdultRedForKid
    ),
//    YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE(
//        YoungerThanTwoMonthsAndLowOrHighTemperature,
//        nullForAdultRedForKid,
//        isComputed = true
//    ),
    PREGNANCY(
        Symptom.CurrentPregnancy,
        redForAdultNullForKid,
        isParent = true
    ),
    PREGNANCY_HIGH_BP(
        Symptom.PregnancyWithHighBloodPressure,
        redForAdultNullForKid,
        parent = PREGNANCY,
        isComputed = true
    ),
    PREGNANCY_WITH_HEAVY_BLEEDING(
        Symptom.PregnancyWithHeavyBleeding,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_ABDOMINAL_PAIN(
        Symptom.PregnancyWithSevereAbdominalPain,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEIZURES(
        Symptom.PregnancyWithSeizures,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ALTERED_MENTAL_STATUS(
        Symptom.PregnancyWithAlteredMentalStatus,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_HEADACHE(
        Symptom.PregnancyWithSevereHeadache,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_VISUAL_CHANGES(
        Symptom.PregnancyWithVisualChanges,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_TRAUMA(
        Symptom.PregnancyWithTrauma,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ACTIVE_LABOR(
        Symptom.PregnancyWithActiveLabor,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    AIRWAY_SWELLING_OR_MASS_OF_MOUTH_OR_THROAT_OR_NECK(
        Symptom.AirwaySwellingOrMassOfMouthOrThroatOrNeck,
        yellowForAll
    ),
    WHEEZING(
        Symptom.Wheezing,
        yellowForAll
    ),
    ACTIVE_BLEEDING(
        Symptom.NonHeavyBleeding,
        yellowForAll
    ),
    SEVERE_PALLOR(
        Symptom.SeverePallor,
        yellowForAll
    ),
    ONGOING_SEVERE_VOMITING_OR_ONGOING_SEVERE_DIARRHEA(
        Symptom.OngoingSevereVomitingOrOngoingSevereDiarrhea,
        yellowForAll
    ),
    MODERATE_DEHYDRATION(
        Symptom.ModerateDehydration,
        nullForAdultYellowForKid
    ),
    UNABLE_TO_FEED_OR_DRINK(
        Symptom.UnableToFeedOrDrink,
        yellowForAll
    ),
    RECENT_FAINTING(
        Symptom.RecentFainting,
        yellowForAll
    ),
    LETHARGY_OR_CONFUSION_OR_AGITATION(
        Symptom.LethargyOrConfusionOrAgitation,
        yellowForAdultNullForKid
    ),
    LETHARGY_OR_RESTLESS_OR_IRRITABLE_OR_CONFUSED(
        Symptom.LethargyOrRestlessOrIrritableOrConfused,
        nullForAdultYellowForKid
    ),
    FOCAL_NEUROLOGIC_DEFICIT_OR_FOCAL_VISUAL_DEFICIT(
        Symptom.FocalNeurologicDeficitOrFocalVisualDeficit,
        yellowForAll
    ),
    HEADACHE_WITH_STIFF_NECK(
        Symptom.HeadacheWithStiffNeck,
        yellowForAll
    ),
    SEVERE_PAIN(
        Symptom.SeverePain,
        yellowForAll
    ),
    UNABLE_TO_PASS_URINE(
        Symptom.UnableToPassUrine,
        yellowForAll
    ),
    ACUTE_LIMB_DEFORMITY_OR_OPEN_FRACTURE(
        Symptom.AcuteLimbDeformityOrOpenFracture,
        yellowForAll
    ),
    OTHER_TRAUMA(
        Symptom.NonHighRiskTrauma,
        yellowForAll
    ),
    OTHER_BURNS(
        Symptom.NonMajorBurns,
        yellowForAll
    ),
    SEXUAL_ASSAULT(
        Symptom.SexualAssault,
        yellowForAll
    ),
    ANIMAL_BITE_OR_NEEDLESTICK_PUNCTURE(
        Symptom.AnimalBiteOrNeedlestickPuncture,
        yellowForAll
    ),
    SEVERE_VISIBLE_MALNUTRITION(
        Symptom.SevereMalnutrition,
        nullForAdultYellowForKid
    ),
    YOUNGER_THAN_SIX_MONTHS(
        Symptom.YoungerThanSixMonths,
        nullForAdultYellowForKid,
        isComputed = true
    ),
    OTHER_PREGNANCY_RELATED_COMPLAINTS(
        Symptom.NonHighRiskPregnancyRelatedComplaints,
        yellowForAdultNullForKid
    ),
//    AGE_OVER_EIGHTY_YEARS(
//        AgeOverEightyYears,
//        yellowForAdultNullForKid,
//        isComputed = true
//    ),
    LOW_SPO2(
        Symptom.LowSpo2,
        yellowForAll,
        isComputed = true
    ),
    LOW_RR(
        Symptom.LowRr,
        yellowForAll,
        isComputed = true
    ),
    HIGH_RR(
        Symptom.HighRr,
        yellowForAll,
        isComputed = true
    ),
    LOW_TEMP(
        Symptom.LowTemp,
        yellowForAll,
        isComputed = true
    ),
    HIGH_TEMP(
        Symptom.HighTemp,
        yellowForAll,
        isComputed = true
    ),
    LOW_RBS(
        Symptom.LowRbs,
        yellowForAll,
        isComputed = true
    ),
    HIGH_RBS(
        Symptom.HighRbs,
        yellowForAll,
        isComputed = true
    ),
    LOW_HR(
        Symptom.LowHr,
        yellowForAll,
        isComputed = true
    ),
    HIGH_HR(
        Symptom.HighHr,
        yellowForAll,
        isComputed = true
    ),
    LOW_SBP(
        Symptom.LowSbp,
        yellowForAdultNullForKid,
        isComputed = true
    ),
    HIGH_SBP(
        Symptom.HighSbp,
        yellowForAdultNullForKid,
        isComputed = true
    );

    companion object {

        val triageSymptoms by lazy { TriageSymptom.entries.associateBy { it.symptom.id } }

        // Common
        const val SPO2_LOW = 92
        const val TEMP_LOW = 35.0
        const val TEMP_HIGH = 39.0
        const val RBS_LOW = 3.0
        const val RBS_HIGH = 14.0

        // Adult
        const val RR_LOW = 10
        const val RR_HIGH = 25
        const val HR_LOW = 40
        const val HR_HIGH = 130
        const val SBP_LOW = 90
        const val SBP_HIGH = 220
        const val PREGNANCY_HIGH_SBP = 160
        const val PREGNANCY_HIGH_DBP = 110

        // Pediatric
        const val RR_LOW_FOR_ONE_YEAR_OLDS = 25
        const val RR_HIGH_FOR_ONE_YEAR_OLDS = 50
        const val HR_LOW_FOR_ONE_YEAR_OLDS = 90
        const val HR_HIGH_FOR_ONE_YEAR_OLDS = 180

        const val RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS = 20
        const val RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS = 40
        const val HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS = 80
        const val HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS = 160

        const val RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 10
        const val RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 30
        const val HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 70
        const val HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 140
    }

    fun labelFor(category: PatientCategory): String {
        return when (this) {
            RESPIRATORY_DISTRESS -> {
                when (category) {
                    PatientCategory.ADULT ->
                        "Respiratory distress (very high or low RR, accessory muscle use, inability to talk/walk, central cyanosis)"
                    PatientCategory.PEDIATRIC ->
                        "Respiratory distress (very high or low RR, accessory muscle use, nasal flaring/grunting, inability to talk/walk/breastfeed, central cyanosis)"
                }
            }
            CONVULSIONS -> "Active convulsions"
            SHOCK -> {
                when (category) {
                    PatientCategory.ADULT ->
                        "Shock (weak rapid pulse, cold extremities, capillary refill > 3 sec, very low BP)"
                    PatientCategory.PEDIATRIC ->
                        "Shock/severe dehydration (weak rapid pulse, cold extremities, capillary refill > 3 sec, unable to drink/breastfeed)"
                }
            }
            HEAVY_ACTIVE_BLEEDING -> "Heavy active bleeding"
            HIGH_RISK_TRAUMA -> "High-risk trauma (fall from twice person’s height, crush injury, penetrating trauma, polytrauma, high-speed motor vehicle crash, pedestrian or cyclist hit by vehicle)"
            MAJOR_BURNS -> when (category) {
                PatientCategory.ADULT ->
                    "Major burns (age > 70 years, >15% BSA, circumferential, involving face/neck, inhalation)"
                PatientCategory.PEDIATRIC ->
                    "Major burns (age < 2 years, >15% BSA, circumferential, involving face/neck, inhalation)"
            }
            THREATENED_LIMB -> "Threatened limb (pulseless, painful and pale, weak, numb, or with massive swelling after trauma)"
            PREGNANCY -> "Pregnancy with any of:"
            PREGNANCY_HIGH_BP -> "SBP > $PREGNANCY_HIGH_SBP or DBP > $PREGNANCY_HIGH_DBP"
            PREGNANCY_WITH_HEAVY_BLEEDING -> "Heavy bleeding"
            PREGNANCY_WITH_SEVERE_ABDOMINAL_PAIN -> "Severe abdominal pain"
            PREGNANCY_WITH_SEIZURES -> "Seizures"
            PREGNANCY_WITH_ALTERED_MENTAL_STATUS -> "Altered mental status"
            PREGNANCY_WITH_SEVERE_HEADACHE -> "Severe headache"
            PREGNANCY_WITH_VISUAL_CHANGES -> "Visual changes"
            PREGNANCY_WITH_TRAUMA -> "Trauma"
            PREGNANCY_WITH_ACTIVE_LABOR -> "Active labor"
            ACTIVE_BLEEDING -> "Active bleeding (no red criteria)"
            MODERATE_DEHYDRATION -> "Moderate dehydration (restless/irritable, sunken eyes and fontanels, skin pinch returns slowly, reduced urine output, drinks eagerly)"
            OTHER_TRAUMA -> "Other trauma (no red criteria)"
            OTHER_BURNS -> "Other burns (no red criteria)"
            SEVERE_VISIBLE_MALNUTRITION -> "Severe malnutrition (visible severe wasting or edema of both feet)"
            OTHER_PREGNANCY_RELATED_COMPLAINTS -> "Other pregnancy-related complaints (no red criteria)"
//            LOW_SPO2 -> "SpO2 < $SPO2_LOW%"
//            LOW_RR -> {
//                when (category){
//                    PatientCategory.ADULT -> "RR < $RR_LOW"
//                    PatientCategory.PEDIATRIC -> "RR < $RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / < $RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / < $RR_LOW_FOR_ONE_YEAR_OLDS (< 1y)"
//                }
//            }
//            HIGH_RR -> {
//                when (category){
//                    PatientCategory.ADULT -> "RR > $RR_HIGH"
//                    PatientCategory.PEDIATRIC -> "RR > $RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / > $RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / > $RR_HIGH_FOR_ONE_YEAR_OLDS (< 1y)"
//                }
//            }
//            LOW_TEMP -> "Temp < $TEMP_LOW°C"
//            HIGH_TEMP -> "Temp > $TEMP_HIGH°C"
//            LOW_HR -> {
//                when (category){
//                    PatientCategory.ADULT -> "HR < $HR_LOW"
//                    PatientCategory.PEDIATRIC -> "HR < $HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / < $HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / < $HR_LOW_FOR_ONE_YEAR_OLDS (< 1y)"
//                }
//            }
//            HIGH_HR -> {
//                when (category) {
//                    PatientCategory.ADULT -> "HR > $HR_HIGH"
//                    PatientCategory.PEDIATRIC -> "HR > $HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / > $HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / > $HR_HIGH_FOR_ONE_YEAR_OLDS (< 1y)"
//                }
//            }
//            LOW_SBP -> "SBP < $SBP_LOW"
//            HIGH_SBP -> "SBP > $SBP_HIGH"
            else -> this.symptom.label
        }
    }

    val id: String
        get() = symptom.id
}