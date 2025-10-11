package com.unimib.oases.domain.model

sealed class BluetoothPatientHandlingResult {
    data class PatientReceived(val patient: Patient) : BluetoothPatientHandlingResult()
    data class Error(val message: String) : BluetoothPatientHandlingResult()
}