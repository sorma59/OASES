package com.unimib.oases.domain.model.symptom

enum class PatientCategory {
    PEDIATRIC, ADULT
}

enum class SymptomTriageCode {
    RED, YELLOW
}

enum class TriageSymptomGroup(val label: String) {
    AIRWAY_AND_BREATHING("AIRWAY & BREATHING"),
    CIRCULATION("CIRCULATION"),
    DISABILITY("DISABILITY"),
    EXPOSURE("EXPOSURE"),
    ALTERED_VITAL_SIGNS("ALTERED VITAL SIGNS")
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
    val group: TriageSymptomGroup,
    val colorAssigner: (PatientCategory) -> SymptomTriageCode?,
    val parent: TriageSymptom? = null,
    val isParent: Boolean = false,
    val isComputed: Boolean = false
) {
    UNCONSCIOUSNESS(
        Symptom.Unconsciousness,
        TriageSymptomGroup.DISABILITY,
        redForAll
    ),
    CONVULSIONS(
        Symptom.Convulsions,
        TriageSymptomGroup.DISABILITY,
        redForAll,
    ),
    STRIDOR(
        Symptom.Stridor,
        TriageSymptomGroup.AIRWAY_AND_BREATHING,
        redForAll
    ),
    RESPIRATORY_DISTRESS(
        Symptom.RespiratoryDistress,
        TriageSymptomGroup.AIRWAY_AND_BREATHING,
        redForAll,
    ),
    SHOCK(
        Symptom.Shock,
        TriageSymptomGroup.CIRCULATION,
        redForAll,
    ),
    HEAVY_ACTIVE_BLEEDING(
        Symptom.HeavyBleeding,
        TriageSymptomGroup.CIRCULATION,
        redForAll
    ),
//    SEVERE_DEHYDRATION(
//        SevereDehydration,
//        nullForAdultRedForKid
//    ),
    HIGH_RISK_TRAUMA(
        Symptom.HighRiskTrauma,
    TriageSymptomGroup.EXPOSURE,
        redForAll
    ),
    MAJOR_BURNS(
        Symptom.MajorBurns,
        TriageSymptomGroup.EXPOSURE,
        redForAll
    ),
    THREATENED_LIMB(
        Symptom.ThreatenedLimb,
        TriageSymptomGroup.EXPOSURE,
        redForAll
    ),
    POISONING_INTOXICATION(
        Symptom.PoisoningIntoxication,
        TriageSymptomGroup.EXPOSURE,
        redForAll
    ),
    ACUTE_TESTICULAR_OR_SCROTAL_PAIN_OR_PRIAPISM(
        Symptom.AcuteTesticularOrScrotalPainOrPriapism,
        TriageSymptomGroup.EXPOSURE,
        yellowForAdultRedForKid
    ),
    SNAKE_BITE(
        Symptom.SnakeBite,
        TriageSymptomGroup.EXPOSURE,
        redForAll
    ),
    AGGRESSIVE_BEHAVIOR(
        Symptom.AggressiveBehavior,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid
    ),
    YOUNGER_THAN_EIGHT_DAYS(
        Symptom.YoungerThanEightDays,
        TriageSymptomGroup.EXPOSURE,
        nullForAdultRedForKid
    ),
//    PRETERM_AND_UNDER_ONE_MONTH(
//        Symptom.PretermAndUnderOneMonth,
//        nullForAdultRedForKid
//    ),
//    YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE(
//        YoungerThanTwoMonthsAndLowOrHighTemperature,
//        nullForAdultRedForKid,
//        isComputed = true
//    ),
    PREGNANCY(
        Symptom.CurrentPregnancy,
    TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        isParent = true
    ),
//    PREGNANCY_HIGH_BP(
//        Symptom.PregnancyWithHighBloodPressure,
//        TriageSymptomGroup.EXPOSURE,
//        redForAdultNullForKid,
//        parent = PREGNANCY,
//        isComputed = true
//    ),
    PREGNANCY_WITH_HEAVY_BLEEDING(
        Symptom.PregnancyWithHeavyBleeding,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_ABDOMINAL_PAIN(
        Symptom.PregnancyWithSevereAbdominalPain,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEIZURES(
        Symptom.PregnancyWithSeizures,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ALTERED_MENTAL_STATUS(
        Symptom.PregnancyWithAlteredMentalStatus,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_HEADACHE(
        Symptom.PregnancyWithSevereHeadache,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_VISUAL_CHANGES(
        Symptom.PregnancyWithVisualChanges,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_TRAUMA(
        Symptom.PregnancyWithTrauma,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ACTIVE_LABOR(
        Symptom.PregnancyWithActiveLabor,
        TriageSymptomGroup.EXPOSURE,
        redForAdultNullForKid,
        parent = PREGNANCY
    ),
    AIRWAY_SWELLING_OR_MASS_OF_MOUTH_OR_THROAT_OR_NECK(
        Symptom.AirwaySwellingOrMassOfMouthOrThroatOrNeck,
        TriageSymptomGroup.AIRWAY_AND_BREATHING,
        yellowForAll
    ),
    WHEEZING(
        Symptom.Wheezing,
        TriageSymptomGroup.AIRWAY_AND_BREATHING,
        yellowForAll
    ),
    ACTIVE_BLEEDING(
        Symptom.NonHeavyBleeding,
        TriageSymptomGroup.CIRCULATION,
        yellowForAll
    ),
    SEVERE_PALLOR(
        Symptom.SeverePallor,
        TriageSymptomGroup.CIRCULATION,
        yellowForAll
    ),
    ONGOING_SEVERE_VOMITING_OR_ONGOING_SEVERE_DIARRHEA(
        Symptom.OngoingSevereVomitingOrOngoingSevereDiarrhea,
        TriageSymptomGroup.CIRCULATION,
        yellowForAll
    ),
    MODERATE_DEHYDRATION(
        Symptom.ModerateDehydration,
        TriageSymptomGroup.CIRCULATION,
        nullForAdultYellowForKid
    ),
    UNABLE_TO_FEED_OR_DRINK(
        Symptom.UnableToFeedOrDrink,
        TriageSymptomGroup.CIRCULATION,
        yellowForAll
    ),
    RECENT_FAINTING(
        Symptom.RecentFainting,
        TriageSymptomGroup.CIRCULATION,
        yellowForAll
    ),
    LETHARGY_OR_CONFUSION_OR_AGITATION(
        Symptom.LethargyOrConfusionOrAgitation,
        TriageSymptomGroup.DISABILITY,
        yellowForAdultNullForKid
    ),
    LETHARGY_OR_RESTLESS_OR_IRRITABLE_OR_CONFUSED(
        Symptom.LethargyOrRestlessOrIrritableOrConfused,
        TriageSymptomGroup.DISABILITY,
        nullForAdultYellowForKid
    ),
    FOCAL_NEUROLOGIC_DEFICIT_OR_FOCAL_VISUAL_DEFICIT(
        Symptom.FocalNeurologicDeficitOrFocalVisualDeficit,
        TriageSymptomGroup.DISABILITY,
        yellowForAll
    ),
    HEADACHE_WITH_STIFF_NECK(
        Symptom.HeadacheWithStiffNeck,
        TriageSymptomGroup.DISABILITY,
        yellowForAll
    ),
    SEVERE_PAIN(
        Symptom.SeverePain,
        TriageSymptomGroup.EXPOSURE,
        yellowForAll
    ),
    UNABLE_TO_PASS_URINE(
        Symptom.UnableToPassUrine,
        TriageSymptomGroup.EXPOSURE,
        yellowForAll
    ),
    ACUTE_LIMB_DEFORMITY_OR_OPEN_FRACTURE(
        Symptom.AcuteLimbDeformityOrOpenFracture,
        TriageSymptomGroup.EXPOSURE,
        yellowForAll
    ),
    OTHER_TRAUMA(
        Symptom.NonHighRiskTrauma,
        TriageSymptomGroup.EXPOSURE,
        yellowForAll
    ),
    OTHER_BURNS(
        Symptom.NonMajorBurns,
        TriageSymptomGroup.EXPOSURE,
        yellowForAll
    ),
    SEXUAL_ASSAULT(
        Symptom.SexualAssault,
        TriageSymptomGroup.EXPOSURE,
        yellowForAll
    ),
    ANIMAL_BITE_OR_NEEDLESTICK_PUNCTURE(
        Symptom.AnimalBiteOrNeedlestickPuncture,
        TriageSymptomGroup.EXPOSURE,
        yellowForAll
    ),
    SEVERE_VISIBLE_MALNUTRITION(
        Symptom.SevereMalnutrition,
        TriageSymptomGroup.EXPOSURE,
        nullForAdultYellowForKid
    ),
    YOUNGER_THAN_SIX_MONTHS(
        Symptom.YoungerThanSixMonths,
        TriageSymptomGroup.EXPOSURE,
        nullForAdultYellowForKid,
        isComputed = true
    ),
    OTHER_PREGNANCY_RELATED_COMPLAINTS(
        Symptom.NonHighRiskPregnancyRelatedComplaints,
        TriageSymptomGroup.EXPOSURE,
        yellowForAdultNullForKid
    ),
//    AGE_OVER_EIGHTY_YEARS(
//        AgeOverEightyYears,
//        yellowForAdultNullForKid,
//        isComputed = true
//    ),
    LOW_SPO2(
        Symptom.LowSpo2,
    TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    LOW_RR(
        Symptom.LowRr,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    HIGH_RR(
        Symptom.HighRr,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    LOW_TEMP(
        Symptom.LowTemp,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    HIGH_TEMP(
        Symptom.HighTemp,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    LOW_RBS(
        Symptom.LowRbs,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    HIGH_RBS(
        Symptom.HighRbs,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    LOW_HR(
        Symptom.LowHr,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    HIGH_HR(
        Symptom.HighHr,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
        isComputed = true
    ),
    LOW_SBP(
        Symptom.LowSbp,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAdultNullForKid,
        isComputed = true
    ),
    HIGH_SBP(
        Symptom.HighSbp,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAdultNullForKid,
        isComputed = true
    ),

    HIGH_DBP(
        Symptom.HighDbp,
        TriageSymptomGroup.ALTERED_VITAL_SIGNS,
        yellowForAll,
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
        const val DBP_HIGH = 130
//        const val PREGNANCY_HIGH_SBP = 160
//        const val PREGNANCY_HIGH_DBP = 110

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
//            PREGNANCY_HIGH_BP -> "SBP > $PREGNANCY_HIGH_SBP or DBP > $PREGNANCY_HIGH_DBP"
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