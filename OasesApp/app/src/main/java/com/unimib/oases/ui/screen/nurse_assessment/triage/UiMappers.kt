package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.model.symptom.Pregnancy
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.triageSymptoms

//fun TriageEvaluation.mapToTriageState(): TriageState{
//    return TriageState(
//    )
//}

fun TriageState.mapToTriageEvaluation(visitId: String): TriageEvaluation{
    return TriageEvaluation(
        visitId = visitId,
        redSymptomIds = removePregnancyIfChildless(selectedReds).toList(),
        yellowSymptomIds = selectedYellows.toList(),
    )
}

private fun removePregnancyIfChildless(selectedReds: Set<String>): Set<String> {
    return if (selectedReds.minus(TriageSymptom.PREGNANCY.id).any {
        triageSymptoms[it]!!.symptom is Pregnancy
    })
        selectedReds
    else
        selectedReds.minus(TriageSymptom.PREGNANCY.id)
}