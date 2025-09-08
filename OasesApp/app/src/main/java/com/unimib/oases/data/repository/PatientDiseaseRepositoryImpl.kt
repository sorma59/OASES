package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntities
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PatientDiseaseRepositoryImpl @Inject constructor(
    val roomDataSource: RoomDataSource,
): PatientDiseaseRepository {

    override suspend fun addPatientDisease(patientDisease: PatientDisease):Resource<Unit> {
        return try {
            roomDataSource.insertPatientDisease(patientDisease.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("PatientDiseaseRepository", "Error adding patient disease: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addPatientDiseases(patientDiseases: List<PatientDisease>): Resource<Unit> {
        return try {
            roomDataSource.insertPatientDiseases(patientDiseases.toEntities())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("PatientDiseaseRepository", "Error adding patient disease: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deletePatientDisease(
        diseaseName: String,
        patientId: String
    ): Resource<Unit> {
        return try {
            roomDataSource.deletePatientDisease(patientId, diseaseName)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("PatientDiseaseRepository", "Error adding patient disease: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun getPatientDiseases(patientId: String): Flow<Resource<List<PatientDisease>>> = flow {
        roomDataSource.getPatientDiseases(patientId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "PatientDiseaseRepository",
                    "Error getting patient diseases for patientId $patientId: ${exception.message}",
                    exception
                )
                emit(Resource.Error(exception.message ?: "An error occurred"))
            }
            .collect { entities ->
                val domainModels = entities.map { it.toDomain() }
                emit(Resource.Success(domainModels))
            }
    }
}