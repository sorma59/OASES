package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.util.TimeProvider
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPatientsWithVisitInfoUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
    private val timeProvider: TimeProvider
) {
    operator fun invoke(): Flow<Resource<List<PatientWithVisitInfo>>> {
        return patientRepository.getPatientsAndVisitsOn(timeProvider.today())
    }
}