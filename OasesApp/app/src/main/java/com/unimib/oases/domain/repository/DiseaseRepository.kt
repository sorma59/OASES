package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Disease
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow


interface DiseaseRepository {
    suspend fun addDisease(disease: Disease): Resource<Unit>
    fun deleteDisease(disease: String): Resource<Unit>
    fun getFilteredDiseases(sex: String, age: String): Flow<Resource<List<Disease>>>
    fun getAllDiseases(): Flow<Resource<List<Disease>>>
}