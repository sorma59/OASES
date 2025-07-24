package com.unimib.oases.ui.screen.bluetooth.pairing

import android.bluetooth.BluetoothDevice

sealed class PairNewDeviceEvent {
    object StartScan : PairNewDeviceEvent()
    object StopScan : PairNewDeviceEvent()
    data class MakeDeviceDiscoverable(val duration: Int = 30) : PairNewDeviceEvent()
    data class PairDevice(val device: BluetoothDevice) : PairNewDeviceEvent()

    object ToastShown : PairNewDeviceEvent()
}