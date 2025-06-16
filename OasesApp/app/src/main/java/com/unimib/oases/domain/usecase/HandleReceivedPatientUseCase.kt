package com.unimib.oases.domain.usecase

import android.util.Log
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HandleReceivedPatientUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {

    suspend fun invoke(patient: Patient) = withContext(Dispatchers.IO) {
        try {
            // Add the patient to the db
            var patientResult = patientRepository.addPatient(patient)
            if (patientResult is Resource.Error) {
                patientResult = patientRepository.addPatient(patient)
                if (patientResult is Resource.Error) {
                    throw Exception("Failed to insert patient $patient")
                }
            }
        } catch (e: Exception) {
            Log.e("PatientWithTriageDataInsertUseCase", "Failed to insert patient full data", e)
        }
    }
}