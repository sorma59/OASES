package com.unimib.oases.domain.model.symptom

import com.unimib.oases.domain.model.symptom.Symptom.AcuteLimbDeformityOrOpenFracture
import com.unimib.oases.domain.model.symptom.Symptom.AcuteTesticularOrScrotalPainOrPriapism
import com.unimib.oases.domain.model.symptom.Symptom.AgeOverEightyYears
import com.unimib.oases.domain.model.symptom.Symptom.AggressiveBehavior
import com.unimib.oases.domain.model.symptom.Symptom.AirwaySwellingOrAirwayMass
import com.unimib.oases.domain.model.symptom.Symptom.AnimalBiteOrNeedlestickPuncture
import com.unimib.oases.domain.model.symptom.Symptom.Convulsions
import com.unimib.oases.domain.model.symptom.Symptom.EdemaOfBothFeet
import com.unimib.oases.domain.model.symptom.Symptom.FocalNeurologicDeficitOrFocalVisualDeficit
import com.unimib.oases.domain.model.symptom.Symptom.HeadacheWithStiffNeck
import com.unimib.oases.domain.model.symptom.Symptom.HeavyBleeding
import com.unimib.oases.domain.model.symptom.Symptom.HighHr
import com.unimib.oases.domain.model.symptom.Symptom.HighRiskTraumaOrHighRiskBurns
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
import com.unimib.oases.domain.model.symptom.Symptom.OngoingBleeding
import com.unimib.oases.domain.model.symptom.Symptom.OngoingSevereVomitingOrOngoingSevereDiarrhea
import com.unimib.oases.domain.model.symptom.Symptom.OtherPregnancyRelatedComplaints
import com.unimib.oases.domain.model.symptom.Symptom.OtherTraumaOrOtherBurns
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
import com.unimib.oases.domain.model.symptom.Symptom.SevereDehydration
import com.unimib.oases.domain.model.symptom.Symptom.SeverePain
import com.unimib.oases.domain.model.symptom.Symptom.SeverePallor
import com.unimib.oases.domain.model.symptom.Symptom.SevereVisibleMalnutrition
import com.unimib.oases.domain.model.symptom.Symptom.SexualAssault
import com.unimib.oases.domain.model.symptom.Symptom.SnakeBite
import com.unimib.oases.domain.model.symptom.Symptom.ThreatenedLimb
import com.unimib.oases.domain.model.symptom.Symptom.UnableToFeedOrDrink
import com.unimib.oases.domain.model.symptom.Symptom.UnableToPassUrine
import com.unimib.oases.domain.model.symptom.Symptom.Unconsciousness
import com.unimib.oases.domain.model.symptom.Symptom.YoungerThanEightDays
import com.unimib.oases.domain.model.symptom.Symptom.YoungerThanSixMonths
import com.unimib.oases.domain.model.symptom.Symptom.YoungerThanTwoMonthsAndLowOrHighTemperature

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

val symptoms = TriageSymptom.entries.associateBy { it.symptom.id }

enum class TriageSymptom(
    val symptom: Symptom,
    val colorAssigner: (PatientCategory) -> SymptomTriageCode?,
    val label: String = symptom.label,
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
        "Active convulsions"
    ),
    RESPIRATORY_DISTRESS(
        RespiratoryDistress,
        ::redForAll
    ),
    HEAVY_BLEEDING(
        HeavyBleeding,
        ::redForAll
    ),
    SEVERE_DEHYDRATION(
        SevereDehydration,
        ::nullForAdultRedForKid
    ),
    HIGH_RISK_TRAUMA_OR_HIGH_RISK_BURNS(
        HighRiskTraumaOrHighRiskBurns,
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
    YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE(
        YoungerThanTwoMonthsAndLowOrHighTemperature,
        ::nullForAdultRedForKid,
        isComputed = true
    ),
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
    AIRWAY_SWELLING_OR_AIRWAY_MASS(
        AirwaySwellingOrAirwayMass,
        ::yellowForAll
    ),
    ONGOING_BLEEDING(
        OngoingBleeding,
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
    OTHER_TRAUMA_OR_OTHER_BURNS(
        OtherTraumaOrOtherBurns,
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
        SevereVisibleMalnutrition,
        ::nullForAdultYellowForKid
    ),
    EDEMA_OF_BOTH_FEET(
        EdemaOfBothFeet,
        ::nullForAdultYellowForKid
    ),
    YOUNGER_THAN_SIX_MONTHS(
        YoungerThanSixMonths,
        ::nullForAdultYellowForKid,
        isComputed = true
    ),
    OTHER_PREGNANCY_RELATED_COMPLAINTS(
        OtherPregnancyRelatedComplaints,
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
    ),
}