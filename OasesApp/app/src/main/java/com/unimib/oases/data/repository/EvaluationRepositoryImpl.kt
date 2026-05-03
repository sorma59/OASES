package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.EvaluationEntity
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntities
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.data.remote.FirestoreManager
import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.repository.EvaluationRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class EvaluationRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
    private val firestoreManager: FirestoreManager
): EvaluationRepository {

    override suspend fun addEvaluation(evaluation: Evaluation): Outcome<Unit> {
        return try {
            roomDataSource.insertEvaluation(evaluation.toEntity())
            firestoreManager.insertEvaluation(evaluation.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addEvaluations(evaluations: List<Evaluation>): Outcome<Unit> {
        return try {
            roomDataSource.insertEvaluations(evaluations.toEntities())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteEvaluation(evaluation: Evaluation): Outcome<Unit> {
        return try {
            roomDataSource.deleteEvaluation(evaluation.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun getVisitEvaluations(visitId: String): Flow<Resource<List<Evaluation>>> =
        roomDataSource.getVisitEvaluations(visitId)
            .map<List<EvaluationEntity>, Resource<List<Evaluation>>> { entities ->
                val domainModels = entities.map { it.toDomain() }
                Resource.Success(domainModels)
            }
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                val message = "Error getting complaint summaries for visitId $visitId: ${exception.message}"
                Log.e("EvaluationRepository", message, exception)
                emit(Resource.Error(message))
            }

    override fun getEvaluation(visitId: String, complaintId: String): Flow<Resource<Evaluation>> = flow {
        roomDataSource.getEvaluation(visitId, complaintId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "EvaluationRepository",
                    "Error getting complaint summary for visitId $visitId and complaintId $complaintId: ${exception.message}",
                    exception
                )
                emit(Resource.Error(exception.message ?: "An error occurred"))
            }
            .collect { entity ->
                val resource = when (entity) {
                    null -> Resource.NotFound()
                    else -> Resource.Success(entity.toDomain())
                }
                emit(resource)
            }
    }

}