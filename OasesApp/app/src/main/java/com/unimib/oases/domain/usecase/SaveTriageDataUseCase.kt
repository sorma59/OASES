package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.model.symptom.Pregnancy
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.triageSymptoms
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SaveTriageDataUseCase @Inject constructor(
    private val visitRepository: VisitRepository
) {

    suspend operator fun invoke(state: TriageState): Outcome {
        return state.visitId?.let { // The visit exists: hence exists a triageEvaluation
            val visit = Visit(
                id = it,
                patientId = state.patientId,
                triageCode = state.editingState!!.triageData.triageCode
            )
            val triageEvaluation = state.toModel(it)
            visitRepository.addVisitWithTriageEvaluation(
                visit,
                triageEvaluation,
                state.editingState.triageData.triageCode,
                state.editingState.triageData.selectedRoom!!
            )
        } ?: run {
            val visit = Visit(
                patientId = state.patientId,
                triageCode = state.editingState!!.triageData.triageCode
            )
            val triageEvaluation = state.toModel(visit.id)
            visitRepository.addVisitWithTriageEvaluation(
                visit,
                triageEvaluation,
                state.editingState.triageData.triageCode,
                state.editingState.triageData.selectedRoom!!
            )
        }
    }

    private fun TriageState.toModel(visitId: String): TriageEvaluation{
        return TriageEvaluation(
            visitId = visitId,
            redSymptomIds = removePregnancyIfChildless(editingState!!.triageData.selectedReds).toList(),
            yellowSymptomIds = editingState.triageData.selectedYellows.toList(),
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

}