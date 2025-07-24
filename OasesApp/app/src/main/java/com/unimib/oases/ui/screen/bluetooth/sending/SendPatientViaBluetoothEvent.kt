package com.unimib.oases.ui.screen.bluetooth.sending

import android.bluetooth.BluetoothDevice
import com.unimib.oases.domain.model.Patient

sealed class SendPatientViaBluetoothEvent {
    data class SendPatient(val patient: Patient, val device: BluetoothDevice) : SendPatientViaBluetoothEvent()

    object SendResultShown : SendPatientViaBluetoothEvent()
}