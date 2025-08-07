package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.nurse_assessment.triage.Symptom
import com.unimib.oases.ui.screen.nurse_assessment.triage.SymptomTriageCode
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageConfig
import javax.inject.Inject

class ConfigTriageUseCase @Inject constructor(
    private val getPatientCategoryUseCase: GetPatientCategoryUseCase
) {
    operator fun invoke(ageInMonths: Int): TriageConfig {

        val category = getPatientCategoryUseCase(ageInMonths)

        val redOptions = mutableListOf<Symptom>()
        val yellowOptions = mutableListOf<Symptom>()

        Symptom.entries.forEach {
            when (it.colorAssigner(category)) {
                SymptomTriageCode.RED -> redOptions.add(it)
                SymptomTriageCode.YELLOW -> yellowOptions.add(it)
                null -> {}
            }
        }

        return TriageConfig(
            redOptions,
            yellowOptions
        )
    }
}