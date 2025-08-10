package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.domain.repository.VitalSignRepository
import javax.inject.Inject

class GetVitalSignPrecisionUseCase @Inject constructor(
    private val vitalSignRepository: VitalSignRepository
) {
    operator fun invoke(name: String): NumericPrecision? {
        return vitalSignRepository.getPrecisionFor(name)
    }
}