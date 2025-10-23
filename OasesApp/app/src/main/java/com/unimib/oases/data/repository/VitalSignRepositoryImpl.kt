package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.domain.model.VitalSign
import com.unimib.oases.domain.repository.VitalSignRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class VitalSignRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): VitalSignRepository {

    private val precisionMap = mutableMapOf<String, NumericPrecision>()

    override suspend fun addVitalSign(vitalSign: VitalSign): Resource<Unit> {
        return try {
            roomDataSource.insertVitalSign(vitalSign.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("DiseaseRepository", "Error adding vital sign: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteVitalSign(vitalSign: VitalSign): Resource<Unit> {
        return try {
            roomDataSource.deleteVitalSign(vitalSign.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getAllVitalSigns(): Flow<Resource<List<VitalSign>>> = flow {
        roomDataSource.getAllVitalSigns()
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                Log.e("VitalSignRepository", "Error getting all vital signs: ${it.message}")
                emit(Resource.Error(it.message ?: "Unknown error"))
            }
            .collect { vitalSigns ->
                precisionMap.clear()
                precisionMap.putAll(
                    vitalSigns.associate { it.name to it.precision}
                )
                emit(Resource.Success(vitalSigns.map { entity -> entity.toDomain() }))
            }
    }

    override fun getPrecisionFor(name: String): NumericPrecision? {
        return precisionMap[name]
    }
}