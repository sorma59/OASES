package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.DBP_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW_FOR_ONE_YEAR_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RBS_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RBS_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW_FOR_ONE_YEAR_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.SBP_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.SBP_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.SPO2_LOW
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.TEMP_HIGH
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.TEMP_LOW
import com.unimib.oases.util.let2
import javax.inject.Inject

class ComputeSymptomsUseCase @Inject constructor(){
    operator fun invoke(
        selectedYellows: Set<String>,
        ageInMonths: Int,
        category: PatientCategory,
        vitalSigns: List<Pair<VitalKey, Double>>
    ): Set<String>{

        val vitalLimits = getVitalLimits(category = category, ageInMonths = ageInMonths)

        val newYellows = selectedYellows.resetComputedElements()

        vitalSigns.forEach { (key, value) ->
            vitalLimits[key]?.let {

                let2(it.low, key.getLowerBoundSymptom()) { low, symptom ->
                    if (value < low) {
                        newYellows.add(symptom.id)
                    }
                }

                let2(it.high, key.getHigherBoundSymptom()) { high, symptom ->
                    if (value > high) {
                        newYellows.add(symptom.id)
                    }
                }
            }
        }

//        when (category){
//            PatientCategory.ADULT -> {
//                vitalSigns.rr?.let {
//                    if (it < RR_LOW) newYellows.add(TriageSymptom.LOW_RR.id)
//                    if (it > RR_HIGH) newYellows.add(TriageSymptom.HIGH_RR.id)
//                }
//                vitalSigns.hr?.let {
//                    if (it < HR_LOW) newYellows.add(TriageSymptom.LOW_HR.id)
//                    if (it > HR_HIGH) newYellows.add(TriageSymptom.HIGH_HR.id)
//                }
//                vitalSigns.sbp?.let {
//                    if (it < SBP_LOW) newYellows.add(TriageSymptom.LOW_SBP.id)
//                    if (it > SBP_HIGH) newYellows.add(TriageSymptom.HIGH_SBP.id)
//                }
//            }
//
//            PatientCategory.PEDIATRIC -> {
//                val bounds = pediatricVitalSignsPolicy.pediatricBounds(ageInMonths) ?: return newYellows
//                newYellows.addAll(bounds.extraSymptoms)
//
//                vitalSigns.rr?.let {
//                    if (it < bounds.rrLower) newYellows.add(TriageSymptom.LOW_RR.id)
//                    if (it > bounds.rrUpper) newYellows.add(TriageSymptom.HIGH_RR.id)
//                }
//                vitalSigns.hr?.let {
//                    if (it < bounds.hrLower) newYellows.add(TriageSymptom.LOW_HR.id)
//                    if (it > bounds.hrUpper) newYellows.add(TriageSymptom.HIGH_HR.id)
//                }
//            }
//        }
//        // Common
//        vitalSigns.spo2?.let {
//            if (it < SPO2_LOW) newYellows.add(TriageSymptom.LOW_SPO2.id)
//        }
//        vitalSigns.temp?.let {
//            if (it < TEMP_LOW) newYellows.add(TriageSymptom.LOW_TEMP.id)
//            if (it > TEMP_HIGH) newYellows.add(TriageSymptom.HIGH_TEMP.id)
//        }
//        vitalSigns.rbs?.let {
//            if (it < RBS_LOW) newYellows.add(TriageSymptom.LOW_RBS.id)
//            if (it > RBS_HIGH) newYellows.add(TriageSymptom.HIGH_RBS.id)
//        }

        return newYellows.toSet() // Make it immutable

    }

//    fun computeRedSymptoms(
//        selectedReds: Set<String>,
//        patient: Patient,
//        vitalSigns: VitalSigns
//    ): Set<String>{
//        val newReds = selectedReds.resetComputedElements()
//        when (patient.category){
//            PatientCategory.ADULT -> {
////                if (vitalSigns.sbp != null && vitalSigns.sbp >= PREGNANCY_HIGH_SBP ||
////                    vitalSigns.dbp != null && vitalSigns.dbp >= PREGNANCY_HIGH_DBP
////                )
////                    newReds.add(TriageSymptom.PREGNANCY_HIGH_BP.id)
//            }
//            PatientCategory.PEDIATRIC -> {
////                if (ageInMonths < 2 &&
////                    vitalSigns.temp != null &&
////                    (vitalSigns.temp < TEMP_LOW || vitalSigns.temp > TEMP_HIGH)
////                )
////                    newReds.add(TriageSymptom.YOUNGER_THAN_TWO_MONTHS_AND_LOW_OR_HIGH_TEMPERATURE.id)
//            }
//        }
//        return newReds.toSet()
//    }

    data class VitalRange(
        val low: Double? = null,
        val high: Double? = null
    )

    enum class VitalKey {
        SPO2,
        RR,
        HR,
        SBP,
        DBP,
        TEMP,
        RBS;

        fun getLowerBoundSymptom(): TriageSymptom? {
            return when (this) {
                SPO2 -> TriageSymptom.LOW_SPO2
                RR -> TriageSymptom.LOW_RR
                HR -> TriageSymptom.LOW_HR
                SBP -> TriageSymptom.LOW_SBP
                DBP -> null
                TEMP -> TriageSymptom.LOW_TEMP
                RBS -> TriageSymptom.LOW_RBS
            }
        }

        fun getHigherBoundSymptom(): TriageSymptom? {
            return when (this) {
                SPO2 -> null
                RR -> TriageSymptom.HIGH_RR
                HR -> TriageSymptom.HIGH_HR
                SBP -> TriageSymptom.HIGH_SBP
                DBP -> TriageSymptom.HIGH_DBP
                TEMP -> TriageSymptom.HIGH_TEMP
                RBS -> TriageSymptom.HIGH_RBS
            }
        }
    }



    fun getVitalLimits(
        category: PatientCategory,
        ageInMonths: Int
    ): Map<VitalKey, VitalRange> {

        val common = mapOf(
            VitalKey.SPO2 to VitalRange(low = SPO2_LOW.toDouble()),
            VitalKey.TEMP to VitalRange(low = TEMP_LOW, high = TEMP_HIGH),
            VitalKey.RBS to VitalRange(low = RBS_LOW, high = RBS_HIGH)
        )

        val unique = when (category) {

            PatientCategory.ADULT -> mapOf(
                VitalKey.RR to VitalRange(low = RR_LOW.toDouble(), high = RR_HIGH.toDouble()),
                VitalKey.HR to VitalRange(low = HR_LOW.toDouble(), high = HR_HIGH.toDouble()),

                VitalKey.SBP to VitalRange(low = SBP_LOW.toDouble(), high = SBP_HIGH.toDouble()),
                VitalKey.DBP to VitalRange(high = DBP_HIGH.toDouble()),

                VitalKey.TEMP to VitalRange(low = TEMP_LOW, high = TEMP_HIGH),
                VitalKey.RBS to VitalRange(low = RBS_LOW, high = RBS_HIGH)
            )

            PatientCategory.PEDIATRIC -> {
                when (ageInMonths / 12) {
                    // < 1 year
                    0 -> mapOf(
                        VitalKey.RR to VitalRange(
                            low = RR_LOW_FOR_ONE_YEAR_OLDS.toDouble(),
                            high = RR_HIGH_FOR_ONE_YEAR_OLDS.toDouble()
                        ),
                        VitalKey.HR to VitalRange(
                            low = HR_LOW_FOR_ONE_YEAR_OLDS.toDouble(),
                            high = HR_HIGH_FOR_ONE_YEAR_OLDS.toDouble()
                        )
                    )

                    // 1–4 years
                    in 1..4 -> mapOf(
                        VitalKey.RR to VitalRange(
                            low = RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS.toDouble(),
                            high = RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS.toDouble()
                        ),
                        VitalKey.HR to VitalRange(
                            low = HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS.toDouble(),
                            high = HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS.toDouble()
                        )
                    )

                    // 5–12 years
                    in 5..12 -> mapOf(
                        VitalKey.RR to VitalRange(
                            low = RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS.toDouble(),
                            high = RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS.toDouble()
                        ),
                        VitalKey.HR to VitalRange(
                            low = HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS.toDouble(),
                            high = HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS.toDouble()
                        )
                    )

                    else -> throw IllegalStateException("A pediatric patient cannot be older than 12")
                }
            }
        }

        return common + unique
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
