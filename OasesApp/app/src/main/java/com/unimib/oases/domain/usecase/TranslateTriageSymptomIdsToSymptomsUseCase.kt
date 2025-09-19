package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.symptom.Symptom
import javax.inject.Inject

class TranslateTriageSymptomIdsToSymptomsUseCase @Inject constructor() {

    operator fun invoke(symptomIds: List<String>): Set<Symptom>{
        val symptoms = mutableSetOf<Symptom>()

        for (id in symptomIds){
            val symptom = Symptom.symptoms[id]
            symptom?.let { symptoms.add(it) }
        }

        return symptoms.toSet()
    }

}