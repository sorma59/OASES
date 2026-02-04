package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.domain.model.symptom.SymptomTriageCode
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.domain.model.symptom.TriageSymptomGroup
import com.unimib.oases.ui.screen.nurse_assessment.triage.SymptomWithLabel
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageConfig
import javax.inject.Inject

class ConfigTriageUseCase @Inject constructor() {
    operator fun invoke(category: PatientCategory): TriageConfig {

        val redOptions = mutableListOf<SymptomWithLabel>()
        val yellowOptions = mutableListOf<SymptomWithLabel>()

        TriageSymptom.entries
            .filter { it.group != TriageSymptomGroup.ALTERED_VITAL_SIGNS }
            .sortedBy{ it.group }
            .forEach {
                val symptom = SymptomWithLabel(it, it.labelFor(category))
                when (it.colorAssigner(category)) {
                    SymptomTriageCode.RED -> redOptions.add(symptom)
                    SymptomTriageCode.YELLOW -> yellowOptions.add(symptom)
                    null -> Unit
                }
            }

        return TriageConfig(
            redOptions.toList(),
            yellowOptions.toList()
        )
    }
}