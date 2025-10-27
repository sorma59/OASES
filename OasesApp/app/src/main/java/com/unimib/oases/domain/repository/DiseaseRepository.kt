package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Disease
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow


interface DiseaseRepository {
    suspend fun addDisease(disease: Disease): Outcome
    suspend fun deleteDisease(disease: Disease): Outcome
    fun getFilteredDiseases(sex: String, age: String): Flow<Resource<List<Disease>>>
    fun getAllDiseases(): Flow<Resource<List<Disease>>>
}