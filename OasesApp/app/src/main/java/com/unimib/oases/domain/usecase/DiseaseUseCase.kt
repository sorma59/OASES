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

    fun getDiseases(): Flow<Resource<List<Disease>>> {
        val result = repo.getAllDiseases()
        return result
    }

    fun deleteDisease(disease: Disease){
       // repo.deleteDisease(user.username)
    }





}