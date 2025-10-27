package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.repository.DiseaseRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class DiseaseRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
): DiseaseRepository {

    override suspend fun addDisease(disease: Disease): Outcome {
        return try {
            roomDataSource.insertDisease(disease.toEntity())
            Outcome.Success
        } catch (e: Exception) {
            Log.e("DiseaseRepository", "Error adding disease: ${e.message}")
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteDisease(disease: Disease): Outcome {
        return try {
            roomDataSource.deleteDisease(disease.toEntity())
            Outcome.Success
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "Unknown error")
        }
    }

    override fun getFilteredDiseases(sex: String, age: String): Flow<Resource<List<Disease>>> = flow {
        roomDataSource.getFilteredDiseases(sex, age)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect { entities ->
                val diseases = entities.map { it.toDomain() }
                emit(Resource.Success(diseases))
            }
    }

    override fun getAllDiseases(): Flow<Resource<List<Disease>>> = flow {
        roomDataSource.getAllDiseases()
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect { entities ->
                val diseases = entities.map { entity -> entity.toDomain() }
                emit(Resource.Success(diseases))
            }
    }
}