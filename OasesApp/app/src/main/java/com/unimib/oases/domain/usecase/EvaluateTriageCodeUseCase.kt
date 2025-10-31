package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.triageSymptoms
import javax.inject.Inject

class EvaluateTriageCodeUseCase @Inject constructor() {

    operator fun invoke(selectedReds: Set<String>, selectedYellows: Set<String>): TriageCode {
        if (hasValidSymptoms(selectedReds))
            return TriageCode.RED
        if (hasValidSymptoms(selectedYellows))
            return TriageCode.YELLOW
        return TriageCode.GREEN
    }

    fun hasValidSymptoms(selectedSymptoms: Set<String>): Boolean {
        val symptoms = selectedSymptoms.toMutableSet()
        val symptomsToRemove = mutableSetOf<String>()
        symptoms.forEach { id ->
            val symptom = triageSymptoms[id]
            if (symptom == null)
                throw IllegalArgumentException("TriageSymptom $id not found")
            if (symptom.parent != null)
                if (!symptoms.contains(symptom.parent.symptom.id))
                    symptomsToRemove.add(id) // A child without its parent does not count
        }
        symptoms.removeAll(symptomsToRemove)
        // True only if not all symptoms are parents because parents need at least
        // one child to be considered a symptom (empty set contains parents only)
        return !symptoms.all { triageSymptoms[it]?.isParent == true}
    }
}