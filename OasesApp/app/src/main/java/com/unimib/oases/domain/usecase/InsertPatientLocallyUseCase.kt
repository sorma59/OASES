package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import javax.inject.Inject

class InsertPatientLocallyUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(patient: Patient) {
        patientRepository.addPatient(patient)
    }
}
