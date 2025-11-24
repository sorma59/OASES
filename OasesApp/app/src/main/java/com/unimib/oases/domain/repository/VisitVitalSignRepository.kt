package com.unimib.oases.domain.repository


import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface VisitVitalSignRepository {
    suspend fun addVisitVitalSign(visitVitalSign: VisitVitalSign): Outcome<Unit>
    suspend fun addVisitVitalSigns(visitVitalSigns: List<VisitVitalSign>): Outcome<Unit>
    fun getVisitVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSign>>>
    fun getVisitLatestVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSign>>>
}