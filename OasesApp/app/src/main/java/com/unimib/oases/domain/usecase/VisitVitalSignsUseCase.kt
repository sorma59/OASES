package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class VisitVitalSignsUseCase @Inject constructor(
    private val visitVitalSignRepository: VisitVitalSignRepository
){
    suspend fun addVisitVitalSign(visitVitalSign: VisitVitalSign): Outcome<Unit> {
        return visitVitalSignRepository.addVisitVitalSign(visitVitalSign)
    }

    fun getVisitVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSign>>> {
        return visitVitalSignRepository.getVisitVitalSigns(visitId)
    }

}