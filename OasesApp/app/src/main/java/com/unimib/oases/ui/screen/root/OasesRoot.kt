package com.unimib.oases.ui.screen.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.ui.components.scaffold.LoginScaffold
import com.unimib.oases.ui.components.scaffold.MainScaffold
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthState.Authenticated
import com.unimib.oases.ui.screen.login.AuthViewModel

@Composable
fun OasesRoot(
    bluetoothCustomManager: BluetoothCustomManager,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    when(authState){
        is Authenticated -> {

            val startDestination = when ((authState as Authenticated).user.role) {
                Role.ADMIN -> Screen.AdminDashboard.route
                Role.DOCTOR, Role.NURSE -> Screen.Home.route
            }

            MainScaffold(startDestination, bluetoothCustomManager, authViewModel)
        }
        else -> LoginScaffold(authViewModel)
    }
}