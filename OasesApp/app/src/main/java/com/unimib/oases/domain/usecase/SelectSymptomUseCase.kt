package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.util.select
import com.unimib.oases.util.toggle
import javax.inject.Inject

class SelectSymptomUseCase @Inject constructor() {

    operator fun invoke(symptom: Symptom, symptoms: Set<Symptom>): Set<Symptom> {
        return if (symptom.group == null)
            symptoms.toggle(symptom)
        else
            symptoms.select(symptom)
    }

}