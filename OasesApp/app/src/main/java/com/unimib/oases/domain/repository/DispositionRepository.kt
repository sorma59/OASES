package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Disposition
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface DispositionRepository {
    suspend fun insertDisposition(disposition: Disposition): Outcome<Unit>
    fun getDisposition(visitId: String): Flow<Resource<Disposition>>
}