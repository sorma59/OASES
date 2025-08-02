package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.patient_registration.triage.PatientCategory
import com.unimib.oases.ui.screen.patient_registration.triage.Symptom
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_HIGH
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_LOW
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.HR_LOW_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.PREGNANCY_HIGH_DBP
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.PREGNANCY_HIGH_SBP
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_HIGH
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_LOW
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.RR_LOW_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.SBP_HIGH
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.SBP_LOW
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.SPO2_LOW
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.TEMP_HIGH
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState.Companion.TEMP_LOW
import javax.inject.Inject

class ComputeSymptomsUseCase @Inject constructor(
    private val getPatientCategory: GetPatientCategoryUseCase
){
    fun computeYellowSymptoms(ageInMonths: Int, vitalSigns: VitalSigns): Set<String>{

        val symptoms = mutableSetOf<String>()

        val patientCategory = getPatientCategory(ageInMonths)

        when (patientCategory){
            PatientCategory.ADULT -> {
                if ((ageInMonths / 12) >= 80)
                    symptoms.add(Symptom.AGE_OVER_EIGHTY_YEARS.id)
                if (vitalSigns.rr != null && vitalSigns.rr < RR_LOW)
                    symptoms.add(Symptom.LOW_RR.id)
                if (vitalSigns.rr != null && vitalSigns.rr > RR_HIGH)
                    symptoms.add(Symptom.HIGH_RR.id)
                if (vitalSigns.hr != null && vitalSigns.hr < HR_LOW)
                    symptoms.add(Symptom.LOW_HR.id)
                if (vitalSigns.hr != null && vitalSigns.hr > HR_HIGH)
                    symptoms.add(Symptom.HIGH_HR.id)
                if (vitalSigns.sbp != null && vitalSigns.sbp < SBP_LOW)
                    symptoms.add(Symptom.LOW_SBP.id)
                if (vitalSigns.sbp != null && vitalSigns.sbp > SBP_HIGH)
                    symptoms.add(Symptom.HIGH_SBP.id)
            }
            PatientCategory.PEDIATRIC -> {
                var rrUpperBound: Int
                var rrLowerBound: Int
                var hrUpperBound: Int
                var hrLowerBound: Int
                when (ageInMonths / 12) {
                    0 -> {
                        if (ageInMonths < 6)
                            symptoms.add(Symptom.YOUNGER_THAN_SIX_MONTHS.id)
                        rrUpperBound = RR_HIGH_FOR_ONE_YEAR_OLDS
                        rrLowerBound = RR_LOW_FOR_ONE_YEAR_OLDS
                        hrUpperBound = HR_HIGH_FOR_ONE_YEAR_OLDS
                        hrLowerBound = HR_LOW_FOR_ONE_YEAR_OLDS
                    }

                    in 1..4 -> {
                        rrUpperBound = RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
                        rrLowerBound = RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
                        hrUpperBound = HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
                        hrLowerBound = HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
                    }

                    in 5..12 -> {
                        rrUpperBound = RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
                        rrLowerBound = RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
                        hrUpperBound = HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
                        hrLowerBound = HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
                    }

                    else -> return symptoms
                }
                if (vitalSigns.rr != null && vitalSigns.rr < rrLowerBound)
                    symptoms.add(Symptom.LOW_RR.id)
                if (vitalSigns.rr != null && vitalSigns.rr > rrUpperBound)
                    symptoms.add(Symptom.HIGH_RR.id)
                if (vitalSigns.hr != null && vitalSigns.hr < hrLowerBound)
                    symptoms.add(Symptom.LOW_HR.id)
                if (vitalSigns.hr != null && vitalSigns.hr > hrUpperBound)
                    symptoms.add(Symptom.HIGH_HR.id)
            }
        }
        // Common
        if (vitalSigns.spo2 != null && vitalSigns.spo2 < SPO2_LOW)
            symptoms.add(Symptom.LOW_SPO2.id)
        if (vitalSigns.temp != null && vitalSigns.temp < TEMP_LOW)
            symptoms.add(Symptom.LOW_TEMP.id)
        if (vitalSigns.temp != null && vitalSigns.temp > TEMP_HIGH)
            symptoms.add(Symptom.HIGH_TEMP.id)

        return symptoms

    }

    fun computeRedSymptoms(ageInMonths: Int, vitalSigns: VitalSigns): Set<String>{
        val symptoms = mutableSetOf<String>()
        val patientCategory = getPatientCategory(ageInMonths)
        when (patientCategory){
            PatientCategory.ADULT -> {
                if (vitalSigns.sbp != null && vitalSigns.sbp >= PREGNANCY_HIGH_SBP ||
                    vitalSigns.dbp != null && vitalSigns.dbp >= PREGNANCY_HIGH_DBP
                )
                    symptoms.add(Symptom.PREGNANCY_HIGH_BP.id)
            }
            PatientCategory.PEDIATRIC -> {
                if (ageInMonths < 2 &&
                    vitalSigns.temp != null &&
                    (vitalSigns.temp < TEMP_LOW || vitalSigns.temp > TEMP_HIGH)
                )
                    symptoms.add(Symptom.YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE.id)
            }
        }
        return symptoms
    }
}

data class VitalSigns(
    val sbp: Int? = null,
    val dbp: Int? = null,
    val hr: Int? = null,
    val rr: Int? = null,
    val spo2: Int? = null,
    val temp: Double? = null,
)
