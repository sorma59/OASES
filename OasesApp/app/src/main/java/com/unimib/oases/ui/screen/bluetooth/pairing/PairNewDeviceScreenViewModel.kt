package com.unimib.oases.ui.screen.bluetooth.pairing

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.util.PermissionHelper
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

    fun makeDeviceDiscoverable(duration: Int) {
        bluetoothCustomManager.makeDeviceDiscoverable(duration)
    }

    fun startScan() {
        bluetoothCustomManager.startDiscovery()
    }

    fun stopScan(){
        bluetoothCustomManager.stopDiscovery()
    }

    @SuppressLint("MissingPermission")
    fun pairDevice(device: BluetoothDevice) {
        if (PermissionHelper.hasPermissions())
            bluetoothCustomManager.pairWithDevice(device)
    }
}