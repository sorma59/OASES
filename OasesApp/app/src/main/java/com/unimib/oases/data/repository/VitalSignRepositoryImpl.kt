package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.VitalSign
import com.unimib.oases.domain.repository.VitalSignRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class VitalSignRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): VitalSignRepository {

    override suspend fun addVitalSign(vitalSign: VitalSign): Resource<Unit> {
        return try {
            roomDataSource.insertVitalSign(vitalSign.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("DiseaseRepository", "Error adding vital sign: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun deleteVitalSign(vitalSign: String): Resource<Unit> {
        return try {
            //launch a coroutine to run the suspend function
            CoroutineScope(Dispatchers.IO).launch {
                roomDataSource.getVitalSign(vitalSign).firstOrNull()?.let {
                    roomDataSource.deleteVitalSign(it)
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getAllVitalSigns(): Flow<Resource<List<VitalSign>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getAllVitalSigns().collect {
            emit(Resource.Success(it.map { entity -> entity.toDomain() }))
        }
    }
}