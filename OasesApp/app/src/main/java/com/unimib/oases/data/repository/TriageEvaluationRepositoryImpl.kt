package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TriageEvaluationRepositoryImpl @Inject constructor(private val roomDataSource: RoomDataSource): TriageEvaluationRepository {
    override suspend fun insertTriageEvaluation(triageEvaluation: TriageEvaluation): Resource<Unit> {
        return try {
            roomDataSource.insertTriageEvaluation(triageEvaluation.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.message ?: "Unknown error while trying to insert triage evaluation")
        }
    }

    override fun getTriageEvaluation(visitId: String): Flow<Resource<TriageEvaluation>> = flow {

//        if (Random.nextBoolean())
//            emit(Resource.Error("Mock error"))
//        else
            roomDataSource.getTriageEvaluation(visitId)
                .onStart {
                    emit(Resource.Loading())
                }
                .catch { exception ->
                    Log.e(
                        "TriageEvaluationRepository",
                        "Error getting triage evaluation for visitId $visitId: ${exception.message}",
                        exception
                    )
                    emit(Resource.Error(exception.message ?: "An error occurred"))
                }
                .collect { entity ->
                    val resource = entity?.let {
                        Resource.Success(it.toDomain())
                    } ?: Resource.Error("No triage evaluation found for visitId $visitId")
                    emit(resource)
                }
    }
}