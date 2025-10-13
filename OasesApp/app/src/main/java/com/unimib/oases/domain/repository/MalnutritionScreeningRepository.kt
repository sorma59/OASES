package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface MalnutritionScreeningRepository {

    suspend fun insertMalnutritionScreening(malnutritionScreening: MalnutritionScreening): Resource<Unit>

    fun getMalnutritionScreening(visitId: String): Flow<Resource<MalnutritionScreening?>>

}