package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.MultipleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.SingleChoiceComplaintQuestion
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.util.selectAndRemove
import com.unimib.oases.util.toggle
import javax.inject.Inject

class SelectSymptomUseCase @Inject constructor() {

    operator fun invoke(
        symptom: Symptom,
        symptoms: Set<Symptom>,
        question: ComplaintQuestion
    ): Set<Symptom> {
        return when (question){
            is SingleChoiceComplaintQuestion -> {
                symptoms.selectAndRemove(symptom, question.options.minus(symptom).toSet())
            }
            is MultipleChoiceComplaintQuestion -> {
                symptoms.toggle(symptom)
            }
        }
    }

}