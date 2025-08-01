package com.unimib.oases.ui.screen.patient_registration.triage

import com.unimib.oases.domain.model.TriageEvaluation

//fun TriageEvaluation.mapToTriageState(): TriageState{
//    return TriageState(
//    )
//}

fun TriageState.mapToTriageEvaluation(visitId: String): TriageEvaluation{
    return TriageEvaluation(
        visitId = visitId,
        redSymptomIds = selectedReds.minus("pregnancy").toList(),
        yellowSymptomIds = selectedYellows.toList(),
    )
}