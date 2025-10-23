package com.unimib.oases.bluetooth

import com.unimib.oases.domain.model.PatientFullData

interface PatientHandler {
    suspend fun onPatientReceived(patientFullData: PatientFullData)
}
