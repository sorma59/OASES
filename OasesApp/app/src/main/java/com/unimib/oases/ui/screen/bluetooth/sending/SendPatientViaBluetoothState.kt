package com.unimib.oases.ui.screen.bluetooth.sending

import android.bluetooth.BluetoothDevice
import com.unimib.oases.domain.model.Patient

data class SendPatientViaBluetoothState(
    val receivedId: String,
    val patient: Patient? = null,
    val pairedDevices: List<BluetoothDevice> = emptyList(),

    val patientRetrievalState: PatientRetrievalState = PatientRetrievalState(),
    val patientSendingState: PatientSendingState = PatientSendingState(),

    val toastMessage: String? = null
)

data class PatientRetrievalState(
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PatientSendingState(
    val result: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)