package com.unimib.oases.data.bluetooth

import com.unimib.oases.domain.model.PatientFullData

interface PatientHandler {
    suspend fun onPatientReceived(patientFullData: PatientFullData)
}
