package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.EvaluationRepository
import com.unimib.oases.ui.screen.medical_visit.initial_medical_evaluation.EvaluationState
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SubmitMedicalVisitPartOneUseCase @Inject constructor(
    private val buildEvaluationFromStateUseCase: BuildEvaluationFromStateUseCase,
    private val evaluationRepository: EvaluationRepository
){

    suspend operator fun invoke(state: EvaluationState): Outcome<Unit> {
        return try {
            val complaintSummary = buildEvaluationFromStateUseCase(state)
            evaluationRepository.addEvaluation(complaintSummary)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

}