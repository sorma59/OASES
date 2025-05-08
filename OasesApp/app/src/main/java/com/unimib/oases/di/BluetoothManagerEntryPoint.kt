package com.unimib.oases.di

import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface BluetoothManagerEntryPoint {
    fun bluetoothCustomManager(): BluetoothCustomManager
}