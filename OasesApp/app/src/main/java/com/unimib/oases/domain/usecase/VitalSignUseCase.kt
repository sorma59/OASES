package com.unimib.oases.domain.usecase


import com.unimib.oases.domain.model.VitalSign
import com.unimib.oases.domain.repository.VitalSignRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VitalSignUseCase @Inject constructor (
    private val repo: VitalSignRepository
){
    suspend fun addVitalSign(vitalSign: VitalSign) {
        repo.addVitalSign(vitalSign)
    }

    suspend fun deleteVitalSign(vitalSign: VitalSign) {
        repo.deleteVitalSign(vitalSign)
    }

    fun getVitalSigns(): Flow<Resource<List<VitalSign>>> {
        return repo.getAllVitalSigns()
    }
}

