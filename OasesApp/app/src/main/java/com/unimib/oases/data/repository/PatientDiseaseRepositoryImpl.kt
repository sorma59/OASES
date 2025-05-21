package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDisease
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PatientDiseaseRepositoryImpl(
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

    override fun getPatientDiseases(patientId: String): Flow<Resource<List<Disease>>> = flow {
        try {
            emit(Resource.Loading())
            roomDataSource.getPatientDiseases(patientId).collect {
                emit(Resource.Success(it.map { entity -> entity.toDisease() }))
            }
        } catch (e: Exception) {
            Log.e("PatientDiseaseRepository", "Error getting patient diseases: ${e.message}")
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
}