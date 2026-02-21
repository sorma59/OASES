package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.Condition
import com.unimib.oases.domain.model.symptom.Symptom
import javax.inject.Inject

class GenerateSuggestedTestsUseCase @Inject constructor() {

    operator fun invoke(complaint: Complaint, symptoms: Set<Symptom>): List<Condition> {
        return complaint.tests.conditions.filter {
            it.predicate(symptoms)
        }
    }

}