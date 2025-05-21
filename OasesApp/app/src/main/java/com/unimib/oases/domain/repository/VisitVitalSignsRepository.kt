package com.unimib.oases.domain.repository


import com.unimib.oases.domain.model.VisitVitalSigns
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface VisitVitalSignsRepository {
    suspend fun addVisitVitalSigns(visitVitalSigns: VisitVitalSigns): Resource<Unit>
    fun getVisitVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSigns>>>
}