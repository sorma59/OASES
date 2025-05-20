package com.unimib.oases.data.bluetooth

import com.unimib.oases.domain.model.Patient

interface PatientHandler {
    fun onPatientReceived(patient: Patient)
}
