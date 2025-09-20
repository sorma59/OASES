package com.unimib.oases.domain.model.symptom

import com.unimib.oases.domain.model.symptom.Symptom.AcuteLimbDeformityOrOpenFracture
import com.unimib.oases.domain.model.symptom.Symptom.AcuteTesticularOrScrotalPainOrPriapism
import com.unimib.oases.domain.model.symptom.Symptom.AgeOverEightyYears
import com.unimib.oases.domain.model.symptom.Symptom.AggressiveBehavior
import com.unimib.oases.domain.model.symptom.Symptom.AirwaySwellingOrMassOfMouthOrThroatOrNeck
import com.unimib.oases.domain.model.symptom.Symptom.AnimalBiteOrNeedlestickPuncture
import com.unimib.oases.domain.model.symptom.Symptom.Convulsions
import com.unimib.oases.domain.model.symptom.Symptom.FocalNeurologicDeficitOrFocalVisualDeficit
import com.unimib.oases.domain.model.symptom.Symptom.HeadacheWithStiffNeck
import com.unimib.oases.domain.model.symptom.Symptom.HeavyBleeding
import com.unimib.oases.domain.model.symptom.Symptom.HighHr
import com.unimib.oases.domain.model.symptom.Symptom.HighRiskTrauma
import com.unimib.oases.domain.model.symptom.Symptom.HighRr
import com.unimib.oases.domain.model.symptom.Symptom.HighSbp
import com.unimib.oases.domain.model.symptom.Symptom.HighTemp
import com.unimib.oases.domain.model.symptom.Symptom.LethargyOrConfusionOrAgitation
import com.unimib.oases.domain.model.symptom.Symptom.LethargyOrRestlessOrIrritableOrConfused
import com.unimib.oases.domain.model.symptom.Symptom.LowHr
import com.unimib.oases.domain.model.symptom.Symptom.LowRr
import com.unimib.oases.domain.model.symptom.Symptom.LowSbp
import com.unimib.oases.domain.model.symptom.Symptom.LowSpo2
import com.unimib.oases.domain.model.symptom.Symptom.LowTemp
import com.unimib.oases.domain.model.symptom.Symptom.MajorBurns
import com.unimib.oases.domain.model.symptom.Symptom.ModerateDehydration
import com.unimib.oases.domain.model.symptom.Symptom.NonHeavyBleeding
import com.unimib.oases.domain.model.symptom.Symptom.NonHighRiskPregnancyRelatedComplaints
import com.unimib.oases.domain.model.symptom.Symptom.NonHighRiskTrauma
import com.unimib.oases.domain.model.symptom.Symptom.NonMajorBurns
import com.unimib.oases.domain.model.symptom.Symptom.OngoingSevereVomitingOrOngoingSevereDiarrhea
import com.unimib.oases.domain.model.symptom.Symptom.PoisoningIntoxication
import com.unimib.oases.domain.model.symptom.Symptom.Pregnancy
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithActiveLabor
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithAlteredMentalStatus
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithHeavyBleeding
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithHighBloodPressure
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithSeizures
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithSevereAbdominalPain
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithSevereHeadache
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithTrauma
import com.unimib.oases.domain.model.symptom.Symptom.PregnancyWithVisualChanges
import com.unimib.oases.domain.model.symptom.Symptom.PretermAndUnderOneMonth
import com.unimib.oases.domain.model.symptom.Symptom.RecentFainting
import com.unimib.oases.domain.model.symptom.Symptom.RespiratoryDistress
import com.unimib.oases.domain.model.symptom.Symptom.SevereMalnutrition
import com.unimib.oases.domain.model.symptom.Symptom.SeverePain
import com.unimib.oases.domain.model.symptom.Symptom.SeverePallor
import com.unimib.oases.domain.model.symptom.Symptom.SexualAssault
import com.unimib.oases.domain.model.symptom.Symptom.Shock
import com.unimib.oases.domain.model.symptom.Symptom.SnakeBite
import com.unimib.oases.domain.model.symptom.Symptom.ThreatenedLimb
import com.unimib.oases.domain.model.symptom.Symptom.UnableToFeedOrDrink
import com.unimib.oases.domain.model.symptom.Symptom.UnableToPassUrine
import com.unimib.oases.domain.model.symptom.Symptom.Unconsciousness
import com.unimib.oases.domain.model.symptom.Symptom.Wheezing
import com.unimib.oases.domain.model.symptom.Symptom.YoungerThanEightDays
import com.unimib.oases.domain.model.symptom.Symptom.YoungerThanSixMonths

enum class PatientCategory {
    PEDIATRIC, ADULT
}

enum class SymptomTriageCode {
    RED, YELLOW
}

fun redForAll(category: PatientCategory) = SymptomTriageCode.RED

fun yellowForAll(category: PatientCategory) = SymptomTriageCode.YELLOW

fun yellowForAdultRedForKid(category: PatientCategory) =
    when (category) {
        PatientCategory.PEDIATRIC -> SymptomTriageCode.RED
        PatientCategory.ADULT -> SymptomTriageCode.YELLOW
    }

fun redForAdultNullForKid(category: PatientCategory) =
    when (category) {
        PatientCategory.PEDIATRIC -> null
        PatientCategory.ADULT -> SymptomTriageCode.RED
    }

fun yellowForAdultNullForKid(category: PatientCategory) =
    when (category) {
        PatientCategory.PEDIATRIC -> null
        PatientCategory.ADULT -> SymptomTriageCode.YELLOW
    }

fun nullForAdultYellowForKid(category: PatientCategory) =
    when (category) {
        PatientCategory.PEDIATRIC -> SymptomTriageCode.YELLOW
        PatientCategory.ADULT -> null
    }

fun nullForAdultRedForKid(category: PatientCategory) =
    when (category) {
        PatientCategory.PEDIATRIC -> SymptomTriageCode.RED
        PatientCategory.ADULT -> null
    }

val triageSymptoms = TriageSymptom.entries.associateBy { it.symptom.id }

enum class TriageSymptom(
    val symptom: Symptom,
    val colorAssigner: (PatientCategory) -> SymptomTriageCode?,
    val parent: TriageSymptom? = null,
    val isParent: Boolean = false,
    val isComputed: Boolean = false
) {

    UNCONSCIOUSNESS(
        Unconsciousness,
        ::redForAll
    ),
    CONVULSIONS(
        Convulsions,
        ::redForAll,
    ),
    RESPIRATORY_DISTRESS(
        RespiratoryDistress,
        ::redForAll,
    ),
    SHOCK(
        Shock,
        ::redForAll,
    ),
    HEAVY_ACTIVE_BLEEDING(
        HeavyBleeding,
        ::redForAll
    ),
//    SEVERE_DEHYDRATION(
//        SevereDehydration,
//        ::nullForAdultRedForKid
//    ),
    HIGH_RISK_TRAUMA(
        HighRiskTrauma,
        ::redForAll
    ),
    MAJOR_BURNS(
        MajorBurns,
        ::redForAll
    ),
    THREATENED_LIMB(
        ThreatenedLimb,
        ::redForAll
    ),
    POISONING_INTOXICATION(
        PoisoningIntoxication,
        ::redForAll
    ),
    ACUTE_TESTICULAR_OR_SCROTAL_PAIN_OR_PRIAPISM(
        AcuteTesticularOrScrotalPainOrPriapism,
        ::yellowForAdultRedForKid
    ),
    SNAKE_BITE(
        SnakeBite,
        ::redForAll
    ),
    AGGRESSIVE_BEHAVIOR(
        AggressiveBehavior,
        ::redForAdultNullForKid
    ),
    YOUNGER_THAN_EIGHT_DAYS(
        YoungerThanEightDays,
        ::nullForAdultRedForKid
    ),
    PRETERM_AND_UNDER_ONE_MONTH(
        PretermAndUnderOneMonth,
        ::nullForAdultRedForKid
    ),
//    YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE(
//        YoungerThanTwoMonthsAndLowOrHighTemperature,
//        ::nullForAdultRedForKid,
//        isComputed = true
//    ),
    PREGNANCY(
        Pregnancy,
        ::redForAdultNullForKid,
        isParent = true
    ),
    PREGNANCY_HIGH_BP(
        PregnancyWithHighBloodPressure,
        ::redForAdultNullForKid,
        parent = PREGNANCY,
        isComputed = true
    ),
    PREGNANCY_WITH_HEAVY_BLEEDING(
        PregnancyWithHeavyBleeding,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_ABDOMINAL_PAIN(
        PregnancyWithSevereAbdominalPain,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEIZURES(
        PregnancyWithSeizures,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ALTERED_MENTAL_STATUS(
        PregnancyWithAlteredMentalStatus,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_HEADACHE(
        PregnancyWithSevereHeadache,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_VISUAL_CHANGES(
        PregnancyWithVisualChanges,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_TRAUMA(
        PregnancyWithTrauma,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ACTIVE_LABOR(
        PregnancyWithActiveLabor,
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    AIRWAY_SWELLING_OR_MASS_OF_MOUTH_OR_THROAT_OR_NECK(
        AirwaySwellingOrMassOfMouthOrThroatOrNeck,
        ::yellowForAll
    ),
    WHEEZING(
        Wheezing,
        ::yellowForAll
    ),
    ACTIVE_BLEEDING(
        NonHeavyBleeding,
        ::yellowForAll
    ),
    SEVERE_PALLOR(
        SeverePallor,
        ::yellowForAll
    ),
    ONGOING_SEVERE_VOMITING_OR_ONGOING_SEVERE_DIARRHEA(
        OngoingSevereVomitingOrOngoingSevereDiarrhea,
        ::yellowForAll
    ),
    MODERATE_DEHYDRATION(
        ModerateDehydration,
        ::nullForAdultYellowForKid
    ),
    UNABLE_TO_FEED_OR_DRINK(
        UnableToFeedOrDrink,
        ::yellowForAll
    ),
    RECENT_FAINTING(
        RecentFainting,
        ::yellowForAll
    ),
    LETHARGY_OR_CONFUSION_OR_AGITATION(
        LethargyOrConfusionOrAgitation,
        ::yellowForAdultNullForKid
    ),
    LETHARGY_OR_RESTLESS_OR_IRRITABLE_OR_CONFUSED(
        LethargyOrRestlessOrIrritableOrConfused,
        ::nullForAdultYellowForKid
    ),
    FOCAL_NEUROLOGIC_DEFICIT_OR_FOCAL_VISUAL_DEFICIT(
        FocalNeurologicDeficitOrFocalVisualDeficit,
        ::yellowForAll
    ),
    HEADACHE_WITH_STIFF_NECK(
        HeadacheWithStiffNeck,
        ::yellowForAll
    ),
    SEVERE_PAIN(
        SeverePain,
        ::yellowForAll
    ),
    UNABLE_TO_PASS_URINE(
        UnableToPassUrine,
        ::yellowForAll
    ),
    ACUTE_LIMB_DEFORMITY_OR_OPEN_FRACTURE(
        AcuteLimbDeformityOrOpenFracture,
        ::yellowForAll
    ),
    OTHER_TRAUMA(
        NonHighRiskTrauma,
        ::yellowForAll
    ),
    OTHER_BURNS(
        NonMajorBurns,
        ::yellowForAll
    ),
    SEXUAL_ASSAULT(
        SexualAssault,
        ::yellowForAll
    ),
    ANIMAL_BITE_OR_NEEDLESTICK_PUNCTURE(
        AnimalBiteOrNeedlestickPuncture,
        ::yellowForAll
    ),
    SEVERE_VISIBLE_MALNUTRITION(
        SevereMalnutrition,
        ::nullForAdultYellowForKid
    ),
    YOUNGER_THAN_SIX_MONTHS(
        YoungerThanSixMonths,
        ::nullForAdultYellowForKid,
        isComputed = true
    ),
    OTHER_PREGNANCY_RELATED_COMPLAINTS(
        NonHighRiskPregnancyRelatedComplaints,
        ::yellowForAdultNullForKid
    ),
    AGE_OVER_EIGHTY_YEARS(
        AgeOverEightyYears,
        ::yellowForAdultNullForKid,
        isComputed = true
    ),
    LOW_SPO2(
        LowSpo2,
        ::yellowForAll,
        isComputed = true
    ),
    LOW_RR(
        LowRr,
        ::yellowForAll,
        isComputed = true
    ),
    HIGH_RR(
        HighRr,
        ::yellowForAll,
        isComputed = true
    ),
    LOW_TEMP(
        LowTemp,
        ::yellowForAll,
        isComputed = true
    ),
    HIGH_TEMP(
        HighTemp,
        ::yellowForAll,
        isComputed = true
    ),
    LOW_HR(
        LowHr,
        ::yellowForAll,
        isComputed = true
    ),
    HIGH_HR(
        HighHr,
        ::yellowForAll,
        isComputed = true
    ),
    LOW_SBP(
        LowSbp,
        ::yellowForAdultNullForKid,
        isComputed = true
    ),
    HIGH_SBP(
        HighSbp,
        ::yellowForAdultNullForKid,
        isComputed = true
    );

    companion object {
        // Common
        const val SPO2_LOW = 90
        const val TEMP_LOW = 35.0
        const val TEMP_HIGH = 39.0

        // Adult
        const val RR_LOW = 10
        const val RR_HIGH = 25
        const val HR_LOW = 50
        const val HR_HIGH = 130
        const val SBP_LOW = 90
        const val SBP_HIGH = 200
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
        const val HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 60
        const val HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 140
    }

    fun labelFor(category: PatientCategory): String {
        return when (this) {
            RESPIRATORY_DISTRESS -> {
                when (category) {
                    PatientCategory.ADULT ->
                        "Respiratory distress (very high or low RR, accessory muscle use, inability to talk/walk, stridor, central cyanosis)"
                    PatientCategory.PEDIATRIC ->
                        "Respiratory distress (very high or low RR, accessory muscle use, nasal flaring/grunting, inability to talk/walk/breastfeed, stridor, central cyanosis)"
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
            LOW_SPO2 -> "SpO2 < $SPO2_LOW%"
            LOW_RR -> {
                when (category){
                    PatientCategory.ADULT -> "RR < $RR_LOW"
                    PatientCategory.PEDIATRIC -> "RR < $RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / < $RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / < $RR_LOW_FOR_ONE_YEAR_OLDS (< 1y)"
                }
            }
            HIGH_RR -> {
                when (category){
                    PatientCategory.ADULT -> "RR > $RR_HIGH"
                    PatientCategory.PEDIATRIC -> "RR > $RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / > $RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / > $RR_HIGH_FOR_ONE_YEAR_OLDS (< 1y)"
                }
            }
            LOW_TEMP -> "Temp < $TEMP_LOW°C"
            HIGH_TEMP -> "Temp > $TEMP_HIGH°C"
            LOW_HR -> {
                when (category){
                    PatientCategory.ADULT -> "HR < $HR_LOW"
                    PatientCategory.PEDIATRIC -> "HR < $HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / < $HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / < $HR_LOW_FOR_ONE_YEAR_OLDS (< 1y)"
                }
            }
            HIGH_HR -> {
                when (category) {
                    PatientCategory.ADULT -> "HR > $HR_HIGH"
                    PatientCategory.PEDIATRIC -> "HR > $HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS (> 5y) / > $HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS (1-4y) / > $HR_HIGH_FOR_ONE_YEAR_OLDS (< 1y)"
                }
            }
            LOW_SBP -> "SBP < $SBP_LOW"
            HIGH_SBP -> "SBP > $SBP_HIGH"
            else -> this.symptom.label
        }
    }
}
