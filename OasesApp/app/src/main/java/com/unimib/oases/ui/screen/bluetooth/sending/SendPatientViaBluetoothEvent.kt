package com.unimib.oases.ui.screen.bluetooth.sending

import android.bluetooth.BluetoothDevice

sealed class SendPatientViaBluetoothEvent {
    data class SendPatient(val device: BluetoothDevice) : SendPatientViaBluetoothEvent()

    data object PatientItemClicked : SendPatientViaBluetoothEvent()
    data object SendResultShown : SendPatientViaBluetoothEvent()
    data object OnToastShown : SendPatientViaBluetoothEvent()
}