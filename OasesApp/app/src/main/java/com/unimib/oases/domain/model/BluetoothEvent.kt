package com.unimib.oases.domain.model

sealed class BluetoothEvent {

    data class PatientReceived(val patientFullData: PatientFullData): BluetoothEvent()
    data class ErrorWhileReceivingPatient(val errorMessage: String): BluetoothEvent()

}