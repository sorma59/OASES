package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface MalnutritionScreeningRepository {

    suspend fun insertMalnutritionScreening(malnutritionScreening: MalnutritionScreening): Outcome

    fun getMalnutritionScreening(visitId: String): Flow<Resource<MalnutritionScreening>>

}