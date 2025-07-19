package com.unimib.oases.data.repository

import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.util.Resource
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

    override fun getTriageEvaluation(visitId: String): Resource<TriageEvaluation> {
        return try {
            Resource.Success(roomDataSource.getTriageEvaluation(visitId).toDomain())
        } catch (e: Exception){
            Resource.Error(e.message ?: "Unknown error while trying to retrieve triage evaluation")
        }
    }
}