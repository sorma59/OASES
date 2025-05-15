package com.unimib.oases.ui.components.scaffold

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.ui.components.util.BluetoothPermissionHandler
import com.unimib.oases.ui.components.util.NoPermissionMessage
import com.unimib.oases.ui.navigation.AppNavigation

@Composable
fun MainScaffold(
    navController: NavHostController,
    bluetoothManager: BluetoothCustomManager,
) {
        Scaffold { padding ->
            AppNavigation(navController, padding, bluetoothManager)
        }
}