package com.unimib.oases.domain.usecase


import com.unimib.oases.domain.model.VitalSigns
import com.unimib.oases.domain.repository.VitalSignsRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

data class VitalSingsUseCase (
    private val repo: VitalSignsRepository
){
    suspend fun addVitalSings(vitalSings: VitalSigns) {
        repo.addVitalSings(vitalSings)
    }

    fun getVitalSings(): Flow<Resource<List<VitalSigns>>> {
        val result = repo.getAllVitalSings()
        return result
    }
}

