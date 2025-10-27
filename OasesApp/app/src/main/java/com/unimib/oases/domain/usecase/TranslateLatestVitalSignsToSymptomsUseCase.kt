package com.unimib.oases.domain.usecase

import android.util.Log
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.util.firstNullableSuccess
import javax.inject.Inject

class TranslateLatestVitalSignsToSymptomsUseCase @Inject constructor(
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val getLatestVitalSignsUseCase: GetLatestVitalSignsUseCase
) {

    suspend operator fun invoke(patientId: String): Set<Symptom> {
        val symptoms = mutableSetOf<Symptom>()
        val visit = getCurrentVisitUseCase(patientId).firstNullableSuccess()
        visit?.let {
            //TODO("Replace magic numbers")
            val vitalSigns = getLatestVitalSignsUseCase(visit.id)
            for (vitalSign in vitalSigns) {
                when (vitalSign.vitalSignName) {
                    "Temperature" -> {
                        if (vitalSign.value > 38)
                            symptoms.add(Symptom.FeverAbove38Degrees)
                    }
                    "Systolic Blood Pressure" -> {
                        if (vitalSign.value > 180)
                            symptoms.add(Symptom.HypertensiveEmergency)
                        else if (vitalSign.value < 90)
                            symptoms.add(Symptom.Hypotension)
                    }
                    "Diastolic Blood Pressure" -> {
                        if (vitalSign.value > 110)
                            symptoms.add(Symptom.HypertensiveEmergency)
                    }
                }
            }
        } ?: run {
            Log.e("GetCurrentVisitUseCase", "Visit not found")
        }

        return symptoms.toSet()
    }

}