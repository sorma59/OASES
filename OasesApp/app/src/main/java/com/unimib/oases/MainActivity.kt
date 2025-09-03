package com.unimib.oases

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.util.FirestoreManager
import com.unimib.oases.ui.screen.root.OasesRoot
import com.unimib.oases.ui.theme.OasesTheme
import com.unimib.oases.util.LocalWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firestoreManager: FirestoreManager

    @Inject
    lateinit var bluetoothCustomManager: BluetoothCustomManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // start listener
        firestoreManager.startListener()


        // Define an ActivityResultLauncher for enabling Bluetooth
        val enableBluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Handle the result from Bluetooth enable request
                bluetoothCustomManager.handleEnableActivityResult(result.resultCode)
            }

        bluetoothCustomManager.setEnableBluetoothLauncher(enableBluetoothLauncher)

        // Define an ActivityResultLauncher for Bluetooth discoverability
        val discoverableBluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Handle the result from Bluetooth enable request
                bluetoothCustomManager.handleDiscoverableActivityResult(result.resultCode)
            }

        bluetoothCustomManager.setDiscoverableBluetoothLauncher(discoverableBluetoothLauncher)

        enableEdgeToEdge()

        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        setContent {
            OasesTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                    OasesRoot(bluetoothCustomManager)
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