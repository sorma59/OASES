package com.unimib.oases.ui.screen.bluetooth.sending

import android.bluetooth.BluetoothDevice
import com.unimib.oases.domain.model.PatientWithVisitInfo

data class SendPatientViaBluetoothState(
    val patientId: String,
    val visitId: String,
    val patientWithVisitInfo: PatientWithVisitInfo? = null,
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