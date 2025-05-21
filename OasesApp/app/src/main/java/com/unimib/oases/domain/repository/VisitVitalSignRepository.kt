package com.unimib.oases.domain.repository


import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface VisitVitalSignRepository {
    suspend fun addVisitVitalSign(visitVitalSign: VisitVitalSign): Resource<Unit>
    fun getVisitVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSign>>>
}