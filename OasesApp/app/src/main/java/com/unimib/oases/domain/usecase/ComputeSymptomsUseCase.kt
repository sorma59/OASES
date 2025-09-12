package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_HIGH
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_LOW
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.HR_LOW_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.PREGNANCY_HIGH_DBP
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.PREGNANCY_HIGH_SBP
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_HIGH
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_LOW
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.RR_LOW_FOR_ONE_YEAR_OLDS
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.SBP_HIGH
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.SBP_LOW
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.SPO2_LOW
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.TEMP_HIGH
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState.Companion.TEMP_LOW
import javax.inject.Inject

class ComputeSymptomsUseCase @Inject constructor(
    private val getPatientCategory: GetPatientCategoryUseCase
){
    fun computeYellowSymptoms(
        selectedYellows: Set<String>,
        ageInMonths: Int,
        vitalSigns: VitalSigns
    ): Set<String>{

        val newYellows = selectedYellows.resetComputedElements()

        val patientCategory = getPatientCategory(ageInMonths)

        when (patientCategory){
            PatientCategory.ADULT -> {
                if ((ageInMonths / 12) >= 80)
                    newYellows.add(TriageSymptom.AGE_OVER_EIGHTY_YEARS.symptom.symptomId.value.string)
                if (vitalSigns.rr != null && vitalSigns.rr < RR_LOW)
                    newYellows.add(TriageSymptom.LOW_RR.symptom.id)
                if (vitalSigns.rr != null && vitalSigns.rr > RR_HIGH)
                    newYellows.add(TriageSymptom.HIGH_RR.symptom.id)
                if (vitalSigns.hr != null && vitalSigns.hr < HR_LOW)
                    newYellows.add(TriageSymptom.LOW_HR.symptom.id)
                if (vitalSigns.hr != null && vitalSigns.hr > HR_HIGH)
                    newYellows.add(TriageSymptom.HIGH_HR.symptom.id)
                if (vitalSigns.sbp != null && vitalSigns.sbp < SBP_LOW)
                    newYellows.add(TriageSymptom.LOW_SBP.symptom.id)
                if (vitalSigns.sbp != null && vitalSigns.sbp > SBP_HIGH)
                    newYellows.add(TriageSymptom.HIGH_SBP.symptom.id)
            }
            PatientCategory.PEDIATRIC -> {
                var rrUpperBound: Int
                var rrLowerBound: Int
                var hrUpperBound: Int
                var hrLowerBound: Int
                when (ageInMonths / 12) {
                    0 -> {
                        if (ageInMonths < 6)
                            newYellows.add(TriageSymptom.YOUNGER_THAN_SIX_MONTHS.symptom.id)
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

                    else -> return newYellows
                }
                if (vitalSigns.rr != null && vitalSigns.rr < rrLowerBound)
                    newYellows.add(TriageSymptom.LOW_RR.symptom.id)
                if (vitalSigns.rr != null && vitalSigns.rr > rrUpperBound)
                    newYellows.add(TriageSymptom.HIGH_RR.symptom.id)
                if (vitalSigns.hr != null && vitalSigns.hr < hrLowerBound)
                    newYellows.add(TriageSymptom.LOW_HR.symptom.id)
                if (vitalSigns.hr != null && vitalSigns.hr > hrUpperBound)
                    newYellows.add(TriageSymptom.HIGH_HR.symptom.id)
            }
        }
        // Common
        if (vitalSigns.spo2 != null && vitalSigns.spo2 < SPO2_LOW)
            newYellows.add(TriageSymptom.LOW_SPO2.symptom.id)
        if (vitalSigns.temp != null && vitalSigns.temp < TEMP_LOW)
            newYellows.add(TriageSymptom.LOW_TEMP.symptom.id)
        if (vitalSigns.temp != null && vitalSigns.temp > TEMP_HIGH)
            newYellows.add(TriageSymptom.HIGH_TEMP.symptom.id)

        return newYellows.toSet() // Make it immutable

    }

    fun computeRedSymptoms(
        selectedReds: Set<String>,
        ageInMonths: Int,
        vitalSigns: VitalSigns
    ): Set<String>{
        val newReds = selectedReds.resetComputedElements()
        val patientCategory = getPatientCategory(ageInMonths)
        when (patientCategory){
            PatientCategory.ADULT -> {
                if (vitalSigns.sbp != null && vitalSigns.sbp >= PREGNANCY_HIGH_SBP ||
                    vitalSigns.dbp != null && vitalSigns.dbp >= PREGNANCY_HIGH_DBP
                )
                    newReds.add(TriageSymptom.PREGNANCY_HIGH_BP.symptom.id)
            }
            PatientCategory.PEDIATRIC -> {
                if (ageInMonths < 2 &&
                    vitalSigns.temp != null &&
                    (vitalSigns.temp < TEMP_LOW || vitalSigns.temp > TEMP_HIGH)
                )
                    newReds.add(TriageSymptom.YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE.symptom.id)
            }
        }
        return newReds.toSet() // Make it immutable
    }
}

private fun Set<String>.resetComputedElements(): MutableSet<String> {
    return this.minus(
        TriageSymptom.entries
            .filter { it.isComputed }
            .map { it.symptom.id }
            .toSet()
    ).toMutableSet()
}

data class VitalSigns(
    val sbp: Int? = null,
    val dbp: Int? = null,
    val hr: Int? = null,
    val rr: Int? = null,
    val spo2: Int? = null,
    val temp: Double? = null,
)
