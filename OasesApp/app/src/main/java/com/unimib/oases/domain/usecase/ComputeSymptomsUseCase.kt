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

    data class VitalRange<T : Number>(
        val low: T? = null,
        val high: T? = null
    )

    enum class VitalKey {
        SPO2,
        RR,
        HR,
        SBP,
        DBP,
        TEMP,
        RBS
    }



    fun getVitalLimits(
        category: PatientCategory,
        ageInMonths: Int
    ): Map<VitalKey, VitalRange<*>>? {




        return when (category) {

            PatientCategory.ADULT -> mapOf(
                VitalKey.SPO2 to VitalRange(low = 92),

                VitalKey.RR to VitalRange(low = 10, high = 25),
                VitalKey.HR to VitalRange(low = 40, high = 130),

                VitalKey.SBP to VitalRange(low = 90, high = 220),
                VitalKey.DBP to VitalRange(high = 130),

                VitalKey.TEMP to VitalRange(low = 35.0, high = 39.0),
                VitalKey.RBS to VitalRange(low = 3.0, high = 14.0)
            )

            PatientCategory.PEDIATRIC -> {
                when (ageInMonths / 12) {
                    // < 1 year
                    0 -> mapOf(
                        VitalKey.RR to VitalRange(low = 25, high = 50),
                        VitalKey.HR to VitalRange(low = 90, high = 180),

                        VitalKey.SPO2 to VitalRange(low = 92),
                        VitalKey.TEMP to VitalRange(low = 35.0, high = 39.0),
                        VitalKey.RBS to VitalRange(low = 3.0, high = 14.0)
                    )

                    // 1–4 years
                    in 1..4 -> mapOf(
                        VitalKey.RR to VitalRange(low = 20, high = 40),
                        VitalKey.HR to VitalRange(low = 80, high = 160),

                        VitalKey.SPO2 to VitalRange(low = 92),
                        VitalKey.TEMP to VitalRange(low = 35.0, high = 39.0),
                        VitalKey.RBS to VitalRange(low = 3.0, high = 14.0)
                    )

                    // 5–12 years
                    in 5..12 -> mapOf(
                        VitalKey.RR to VitalRange(low = 10, high = 30),
                        VitalKey.HR to VitalRange(low = 70, high = 140),

                        VitalKey.SPO2 to VitalRange(low = 92),
                        VitalKey.TEMP to VitalRange(low = 35.0, high = 39.0),
                        VitalKey.RBS to VitalRange(low = 3.0, high = 14.0)
                    )

                    else -> null
                }
            }
        }
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
