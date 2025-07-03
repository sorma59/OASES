package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDisease
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.repository.DiseaseRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class DiseaseRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
): DiseaseRepository {

    override suspend fun addDisease(disease: Disease): Resource<Unit> {

        return try {
            roomDataSource.insertDisease(disease.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("DiseaseRepository", "Error adding disease: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun deleteDisease(disease: String): Resource<Unit> {
        return try {
            //launch a coroutine to run the suspend function
            CoroutineScope(Dispatchers.IO).launch {
                roomDataSource.getDisease(disease).firstOrNull()?.let {
                    roomDataSource.deleteDisease(it)
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getFilteredDiseases(sex: String, age: String): Flow<Resource<List<Disease>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getFilteredDiseases(sex, age).collect {
            emit(Resource.Success(it.map { entity -> entity.toDisease() }))
        }
    }

    override fun getAllDiseases(): Flow<Resource<List<Disease>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getAllDiseases().collect {
            emit(Resource.Success(it.map { entity -> entity.toDisease() }))
        }
    }
}