package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import javax.inject.Inject

class InsertPatientLocallyUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(patient: Patient): Resource<Unit> {
        return patientRepository.addPatient(patient)
    }
}
