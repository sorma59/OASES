package com.unimib.oases.data.bluetooth

import com.unimib.oases.data.transfer.PatientFullData
import com.unimib.oases.domain.model.Patient

interface PatientHandler {
    suspend fun onPatientReceived(patient: Patient)

    suspend fun onPatientWithTriageDataReceived(patientWithTriageData: PatientFullData)
}
