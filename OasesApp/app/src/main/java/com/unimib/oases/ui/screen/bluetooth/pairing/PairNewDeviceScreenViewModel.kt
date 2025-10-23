package com.unimib.oases.ui.screen.bluetooth.pairing

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.unimib.oases.bluetooth.BluetoothCustomManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PairNewDeviceScreenViewModel @Inject constructor(
    private val bluetoothCustomManager: BluetoothCustomManager
) : ViewModel(){

    private val defaultDiscoverableDuration = 30

    val remainingTime = bluetoothCustomManager.remainingTime

    val discoveredDevices = bluetoothCustomManager.discoveredDevices

    val isDiscovering = bluetoothCustomManager.isDiscovering

    val deviceName = bluetoothCustomManager.getThisDeviceName()

    val pairingResult = bluetoothCustomManager.pairingResult

    val toastMessage = bluetoothCustomManager.toastMessage

    init {
        makeDeviceDiscoverable(defaultDiscoverableDuration)
        startScan()
    }

    fun onEvent(event: PairNewDeviceEvent) {
        when (event) {
            is PairNewDeviceEvent.StartScan -> startScan()

            is PairNewDeviceEvent.StopScan -> stopScan()

            is PairNewDeviceEvent.MakeDeviceDiscoverable -> makeDeviceDiscoverable(event.duration)

            is PairNewDeviceEvent.PairDevice -> pairDevice(event.device)

            is PairNewDeviceEvent.ToastShown -> bluetoothCustomManager.toastShown()
        }
    }

    private fun makeDeviceDiscoverable(duration: Int) {
        bluetoothCustomManager.makeDeviceDiscoverable(duration)
    }

    private fun startScan() {
        bluetoothCustomManager.startDiscovery()
    }

    private fun stopScan(){
        bluetoothCustomManager.stopDiscovery()
    }

    private fun pairDevice(device: BluetoothDevice) {
        bluetoothCustomManager.pairWithDevice(device)
    }
}