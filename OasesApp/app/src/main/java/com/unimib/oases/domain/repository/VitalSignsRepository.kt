package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.VitalSigns
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface VitalSignsRepository {
    suspend fun addVitalSings(vitalSings: VitalSigns): Resource<Unit>
    fun getAllVitalSings(): Flow<Resource<List<VitalSigns>>>
}