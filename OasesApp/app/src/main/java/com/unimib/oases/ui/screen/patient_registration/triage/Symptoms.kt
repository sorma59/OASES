package com.unimib.oases.ui.screen.patient_registration.triage

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

val symptoms = Symptom.entries.associateBy { it.id }

enum class Symptom(
    val id: String,
    val label: String,
    val colorAssigner: (PatientCategory) -> SymptomTriageCode?,
    val parent: Symptom? = null,
    val isParent: Boolean = false,
    val isComputed: Boolean = false
) {

    UNCONSCIOUSNESS(
        "unconsciousness",
        "Unconsciousness",
        ::redForAll
    ),
    ACTIVE_CONVULSIONS(
        "active_convulsions",
        "Active convulsions",
        ::redForAll
    ),
    RESPIRATORY_DISTRESS(
        "respiratory_distress",
        "Respiratory distress",
        ::redForAll
    ),
    HEAVY_BLEEDING(
        "heavy_bleeding",
        "Heavy bleeding",
        ::redForAll
    ),
    SEVERE_DEHYDRATION(
        "severe_dehydration",
        "Severe dehydration",
        ::nullForAdultRedForKid
    ),
    HIGH_RISK_TRAUMA_OR_HIGH_RISK_BURNS(
        "high_risk_trauma_or_high_risk_burns",
        "High risk trauma/burns",
        ::redForAll
    ),
    THREATENED_LIMB(
        "threatened_limb",
        "Threatened limb",
        ::redForAll
    ),
    POISONING_INTOXICATION(
        "poisoning_intoxication",
        "Poisoning/intoxication",
        ::redForAll
    ),
    ACUTE_TESTICULAR_OR_SCROTAL_PAIN_OR_PRIAPISM(
        "acute_testicular_or_scrotal_pain_or_priapism",
        "Acute testicular/scrotal pain or priapism",
        ::yellowForAdultRedForKid
    ),
    SNAKE_BITE(
        "snake_bite",
        "Snake bite",
        ::redForAll
    ),
    AGGRESSIVE_BEHAVIOR(
        "aggressive_behavior",
        "Aggressive behavior",
        ::redForAdultNullForKid
    ),
    YOUNGER_THAN_EIGHT_DAYS(
        "younger_than_eight_days",
        "Younger than 8 days",
        ::nullForAdultRedForKid
    ),
    PRETERM_AND_UNDER_ONE_MONTH(
        "preterm_and_under_one_month",
        "Preterm baby younger than one month old",
        ::nullForAdultRedForKid
    ),
    YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE(
        "younger_than_two_months_and_low_or_high_temperature",
        "Younger than two months old and low or high temperature",
        ::nullForAdultRedForKid,
        isComputed = true
    ),
    PREGNANCY(
        "pregnancy",
        "Pregnancy with any of:",
        ::redForAdultNullForKid,
        isParent = true
    ),
    PREGNANCY_HIGH_BP(
        "high_blood_pressure",
        "High blood pressure",
        ::redForAdultNullForKid,
        parent = PREGNANCY,
        isComputed = true
    ),
    PREGNANCY_WITH_HEAVY_BLEEDING(
        "pregnancy_with_heavy_bleeding",
        "Heavy bleeding",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_ABDOMINAL_PAIN(
        "pregnancy_with_severe_abdominal_pain",
        "Severe abdominal pain",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEIZURES(
        "pregnancy_with_seizures",
        "Seizures",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ALTERED_MENTAL_STATUS(
        "pregnancy_with_altered_mental_status",
        "Altered mental status",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_SEVERE_HEADACHE(
        "pregnancy_with_severe_headache",
        "Severe headache",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_VISUAL_CHANGES(
        "pregnancy_with_visual_changes",
        "Visual changes",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_TRAUMA(
        "pregnancy_with_trauma",
        "Trauma",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    PREGNANCY_WITH_ACTIVE_LABOR(
        "pregnancy_with_active_labor",
        "Active labor",
        ::redForAdultNullForKid,
        parent = PREGNANCY
    ),
    AIRWAY_SWELLING_OR_AIRWAY_MASS(
        "airway_swelling_or_airway_mass",
        "Airway swelling/mass",
        ::yellowForAll
    ),
    ONGOING_BLEEDING(
        "ongoing_bleeding",
        "Ongoing bleeding (no red criteria)",
        ::yellowForAll
    ),
    SEVERE_PALLOR(
        "severe_pallor",
        "Severe pallor",
        ::yellowForAll
    ),
    ONGOING_SEVERE_VOMITING_OR_ONGOING_SEVERE_DIARRHEA(
        "ongoing_severe_vomiting_or_ongoing_severe_diarrhea",
        "Ongoing severe vomiting/diarrhea",
        ::yellowForAll
    ),
    UNABLE_TO_FEED_OR_DRINK(
        "unable_to_feed_or_drink",
        "Unable to feed or drink",
        ::yellowForAll
    ),
    RECENT_FAINTING(
        "recent_fainting",
        "Recent fainting",
        ::yellowForAll
    ),
    LETHARGY_OR_CONFUSION_OR_AGITATION(
        "lethargy_or_confusion_or_agitation",
        "Lethargy/confusion/agitation",
        ::yellowForAdultNullForKid
    ),
    LETHARGY_OR_RESTLESS_OR_IRRITABLE_OR_CONFUSED(
        "lethargy_or_restless_or_irritable_or_confused",
        "Lethargy/restless/irritable/confused",
        ::nullForAdultYellowForKid
    ),
    FOCAL_NEUROLOGIC_DEFICIT_OR_FOCAL_VISUAL_DEFICIT(
        "focal_neurologic_deficit_or_focal_visual_deficit",
        "Focal neurologic/visual deficit",
        ::yellowForAll
    ),
    HEADACHE_WITH_STIFF_NECK(
        "headache_with_stiff_neck",
        "Headache with stiff neck",
        ::yellowForAll
    ),
    SEVERE_PAIN(
        "severe_pain",
        "Severe pain",
        ::yellowForAll
    ),
    UNABLE_TO_PASS_URINE(
        "unable_to_pass_urine",
        "Unable to pass urine",
        ::yellowForAll
    ),
    ACUTE_LIMB_DEFORMITY_OR_OPEN_FRACTURE(
        "acute_limb_deformity_or_open_fracture",
        "Acute limb deformity/open fracture",
        ::yellowForAll
    ),
    OTHER_TRAUMA_OR_OTHER_BURNS(
        "other_trauma_or_other_burns",
        "Other trauma/burns (no red criteria)",
        ::yellowForAll
    ),
    SEXUAL_ASSAULT(
        "sexual_assault",
        "Sexual assault",
        ::yellowForAll
    ),
    ANIMAL_BITE_OR_NEEDLESTICK_PUNCTURE(
        "animal_bite_or_needlestick_puncture",
        "Animal bite/needlestick puncture",
        ::yellowForAll
    ),
    SEVERE_VISIBLE_MALNUTRITION(
        "severe_visible_malnutrition",
        "Severe visible malnutrition",
        ::nullForAdultYellowForKid
    ),
    EDEMA_OF_BOTH_FEET(
        "edema_of_both_feet",
        "Edema of both feet",
        ::nullForAdultYellowForKid
    ),
    YOUNGER_THAN_SIX_MONTHS(
        "younger_than_six_months",
        "Younger than 6 months",
        ::nullForAdultYellowForKid,
        isComputed = true
    ),
    OTHER_PREGNANCY_RELATED_COMPLAINTS(
        "other_pregnancy_related_complaints",
        "Other pregnancy-related complaints",
        ::yellowForAdultNullForKid
    ),
    AGE_OVER_EIGHTY_YEARS(
        "age_over_eighty_years",
        "Age over 80 years",
        ::yellowForAdultNullForKid,
        isComputed = true
    ),
    LOW_SPO2(
        "low_spo2",
        "SPO2 < ${TriageState.SPO2_LOW}",
        ::yellowForAll,
        isComputed = true
    ),
    LOW_RR(
        "low_rr",
        "Low respiratory rate",
        ::yellowForAll,
        isComputed = true
    ),
    HIGH_RR(
        "high_rr",
        "High respiratory rate",
        ::yellowForAll,
        isComputed = true
    ),
    LOW_TEMP(
        "low_temp",
        "Low temperature",
        ::yellowForAll,
        isComputed = true
    ),
    HIGH_TEMP(
        "high_temp",
        "High temperature",
        ::yellowForAll,
        isComputed = true
    ),
    LOW_HR(
        "low_hr",
        "Low heart rate",
        ::yellowForAll,
        isComputed = true
    ),
    HIGH_HR(
        "high_hr",
        "High heart rate",
        ::yellowForAll,
        isComputed = true
    ),
    LOW_SBP(
        "low_sbp",
        "Low systolic blood pressure",
        ::yellowForAdultNullForKid,
        isComputed = true
    ),
    HIGH_SBP(
        "high_sbp",
        "High systolic blood pressure",
        ::yellowForAdultNullForKid,
        isComputed = true
    ),
}