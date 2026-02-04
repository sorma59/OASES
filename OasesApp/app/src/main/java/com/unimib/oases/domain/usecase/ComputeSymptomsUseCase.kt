package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RBS_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RBS_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.SBP_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.SBP_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.SPO2_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.TEMP_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.TEMP_LOW
import com.unimib.oases.domain.policy.PediatricVitalSignsPolicy
import javax.inject.Inject

class ComputeSymptomsUseCase @Inject constructor(
    private val pediatricVitalSignsPolicy: PediatricVitalSignsPolicy
){
    fun computeYellowSymptoms(
        selectedYellows: Set<String>,
        patient: Patient,
        vitalSigns: VitalSigns
    ): Set<String>{

        val newYellows = selectedYellows.resetComputedElements()

        when (patient.category){
            PatientCategory.ADULT -> {
                vitalSigns.rr?.let {
                    if (it < RR_LOW) newYellows.add(TriageSymptom.LOW_RR.id)
                    if (it > RR_HIGH) newYellows.add(TriageSymptom.HIGH_RR.id)
                }
                vitalSigns.hr?.let {
                    if (it < HR_LOW) newYellows.add(TriageSymptom.LOW_HR.id)
                    if (it > HR_HIGH) newYellows.add(TriageSymptom.HIGH_HR.id)
                }
                vitalSigns.sbp?.let {
                    if (it < SBP_LOW) newYellows.add(TriageSymptom.LOW_SBP.id)
                    if (it > SBP_HIGH) newYellows.add(TriageSymptom.HIGH_SBP.id)
                }
            }

            PatientCategory.PEDIATRIC -> {
                val bounds = pediatricVitalSignsPolicy.pediatricBounds(patient.ageInMonths) ?: return newYellows
                newYellows.addAll(bounds.extraSymptoms)

                vitalSigns.rr?.let {
                    if (it < bounds.rrLower) newYellows.add(TriageSymptom.LOW_RR.id)
                    if (it > bounds.rrUpper) newYellows.add(TriageSymptom.HIGH_RR.id)
                }
                vitalSigns.hr?.let {
                    if (it < bounds.hrLower) newYellows.add(TriageSymptom.LOW_HR.id)
                    if (it > bounds.hrUpper) newYellows.add(TriageSymptom.HIGH_HR.id)
                }
            }
        }
        // Common
        vitalSigns.spo2?.let {
            if (it < SPO2_LOW) newYellows.add(TriageSymptom.LOW_SPO2.id)
        }
        vitalSigns.temp?.let {
            if (it < TEMP_LOW) newYellows.add(TriageSymptom.LOW_TEMP.id)
            if (it > TEMP_HIGH) newYellows.add(TriageSymptom.HIGH_TEMP.id)
        }
        vitalSigns.rbs?.let {
            if (it < RBS_LOW) newYellows.add(TriageSymptom.LOW_RBS.id)
            if (it > RBS_HIGH) newYellows.add(TriageSymptom.HIGH_RBS.id)
        }

        return newYellows.toSet() // Make it immutable

    }

    fun computeRedSymptoms(
        selectedReds: Set<String>,
        patient: Patient,
        vitalSigns: VitalSigns
    ): Set<String>{
        val newReds = selectedReds.resetComputedElements()
        when (patient.category){
            PatientCategory.ADULT -> {
//                if (vitalSigns.sbp != null && vitalSigns.sbp >= PREGNANCY_HIGH_SBP ||
//                    vitalSigns.dbp != null && vitalSigns.dbp >= PREGNANCY_HIGH_DBP
//                )
//                    newReds.add(TriageSymptom.PREGNANCY_HIGH_BP.id)
            }
            PatientCategory.PEDIATRIC -> {
//                if (ageInMonths < 2 &&
//                    vitalSigns.temp != null &&
//                    (vitalSigns.temp < TEMP_LOW || vitalSigns.temp > TEMP_HIGH)
//                )
//                    newReds.add(TriageSymptom.YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE.id)
            }
        }
        return newReds.toSet()
    }
}

private fun Set<String>.resetComputedElements(): MutableSet<String> {
    return this.minus(
        TriageSymptom.entries
            .filter { it.isComputed }
            .map { it.id }
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
    val rbs: Double? = null
)
