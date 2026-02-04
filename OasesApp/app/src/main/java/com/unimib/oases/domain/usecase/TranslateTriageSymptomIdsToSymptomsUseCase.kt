package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.domain.model.symptom.symptomsById
import javax.inject.Inject

class TranslateTriageSymptomIdsToSymptomsUseCase @Inject constructor() {

    operator fun invoke(symptomIds: List<String>): Set<Symptom>{
        val symptoms = mutableSetOf<Symptom>()

        for (id in symptomIds){
            val symptom = symptomsById[id]
//            if (symptom is Pregnancy)
//                symptoms.add(Symptom.CurrentPregnancy)
            symptom?.let { symptoms.add(it) }
        }

        return symptoms.toSet()
    }

}