package com.unimib.oases.ui.screen.patient_registration.triage

import com.unimib.oases.util.AppConstants

data class TriageState(

    // Birthdate & Vital Signs
    val ageInMonths: Int? = null,
    val sbp: Int? = null,
    val dbp: Int? = null,
    val hr: Int? = null,
    val rr: Int? = null,
    val spo2: Int? = null,
    val temp: Double? = null,

    val triageConfig: TriageConfig? = null,

    val selectedReds: Set<String> = emptySet(),
    val selectedYellows: Set<String> = emptySet(),

//    val computedSymptoms: Set<Symptom> = emptySet(),

    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null,
) {
    companion object {
        // Common
        const val SPO2_LOW = 92
        const val TEMP_LOW = 35.0
        const val TEMP_HIGH = 39.0

        // Adult
        const val RR_LOW = 10
        const val RR_HIGH = 30
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
        const val HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 70
        const val HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS = 140
    }

    val isRedCode: Boolean
        get() = selectedReds.minus("pregnancy").isNotEmpty()
    val isYellowCode: Boolean
        get() = !isRedCode && selectedYellows.isNotEmpty()

    val patientCategory: PatientCategory?
        get() = if (ageInMonths == null) null
            else{
                if (ageInMonths / 12 >= AppConstants.MATURITY_AGE)
                    PatientCategory.ADULT
                else
                    PatientCategory.PEDIATRIC
            }
}