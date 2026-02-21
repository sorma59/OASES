package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.SupportiveTherapy
import com.unimib.oases.domain.model.symptom.Symptom
import javax.inject.Inject

class GenerateSuggestedSupportiveTherapiesUseCase @Inject constructor() {

    operator fun invoke(complaint: Complaint, symptoms: Set<Symptom>): List<SupportiveTherapy>{
        return complaint.therapies.filter {
            it.predicate(symptoms)
        }
    }

}