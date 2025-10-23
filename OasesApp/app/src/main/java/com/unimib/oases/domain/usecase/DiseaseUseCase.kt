package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.repository.DiseaseRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiseaseUseCase @Inject constructor(
    private val repo: DiseaseRepository
) {

    suspend fun addDisease(disease: Disease) {
        repo.addDisease(disease)
    }

    fun getFilteredDiseases(sex: String, age: String): Flow<Resource<List<Disease>>> {
        val result = repo.getFilteredDiseases(sex, age)
        return result
    }

    fun getDiseases(): Flow<Resource<List<Disease>>> {
        val result = repo.getAllDiseases()
        return result
    }

    suspend fun deleteDisease(disease: Disease){
       repo.deleteDisease(disease)
    }
}