package com.unimib.oases

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.di.BluetoothManagerEntryPoint
import com.unimib.oases.ui.navigation.AppNavigation
import com.unimib.oases.ui.theme.OasesTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var bluetoothCustomManager: BluetoothCustomManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothCustomManager = EntryPointAccessors.fromActivity(
            this,
            BluetoothManagerEntryPoint::class.java
        ).bluetoothCustomManager()

        // Define an ActivityResultLauncher for enabling Bluetooth
        val enableBluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Handle the result from Bluetooth enable request
                bluetoothCustomManager.handleEnableActivityResult(result.resultCode)
            }

        bluetoothCustomManager.setEnableBluetoothLauncher(enableBluetoothLauncher)

        val discoverableBluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Handle the result from Bluetooth enable request
                bluetoothCustomManager.handleDiscoverableActivityResult(result.resultCode)
            }

        bluetoothCustomManager.setDiscoverableBluetoothLauncher(discoverableBluetoothLauncher)

        bluetoothCustomManager.initialize(lifecycleScope)

        enableEdgeToEdge()
        setContent {
            OasesTheme {
                val navController = rememberNavController()
                Scaffold { padding ->
                    AppNavigation(navController, padding)
                }
            }
        }

        // Register receiver
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
        registerReceiver(bluetoothCustomManager.bluetoothReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothCustomManager.bluetoothReceiver)
        bluetoothCustomManager.deinitialize()
    }
}