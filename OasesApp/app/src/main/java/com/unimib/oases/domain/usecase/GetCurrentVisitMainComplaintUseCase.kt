package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.EvaluationRepository
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class GetCurrentVisitMainComplaintUseCase @Inject constructor(
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val evaluationRepository: EvaluationRepository
) {

    suspend operator fun invoke(patientId: String, visit: Visit? = null): Resource<List<Evaluation>> {
        if (visit != null) { // If visit was passed as parameter, use it
            return try {
                val complaint = evaluationRepository
                    .getVisitEvaluations(visit.id)
                    .firstSuccess()
                Resource.Success(complaint)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        } else { // Otherwise, get it from db
            return try {
                val visitFromDb = getCurrentVisitUseCase(patientId)
                val complaint = evaluationRepository
                    .getVisitEvaluations(visitFromDb.id)
                    .firstSuccess()
                Resource.Success(complaint)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}