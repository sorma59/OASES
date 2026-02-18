package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.model.symptom.Pregnancy
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.triageSymptoms
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState
import com.unimib.oases.util.Outcome
import kotlinx.coroutines.delay
import javax.inject.Inject

class SaveTriageDataUseCase @Inject constructor(
    private val visitRepository: VisitRepository
) {

    suspend operator fun invoke(state: TriageState, vitalSigns: List<VisitVitalSign>): Outcome<Unit> {
        delay(500)

        val visit = state.visit!!.copy(
            triageCode = state.editingState!!.triageData.triageCode,
            patientStatus = PatientStatus.WAITING_FOR_VISIT,
            roomName = state.editingState.triageData.selectedRoom!!.name
        )
        val triageEvaluation = state.toModel(state.visitId)
        return visitRepository.insertTriageEvaluationAndVitalSignsAndUpdateVisit(
            visit,
            triageEvaluation,
            vitalSigns
        )
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