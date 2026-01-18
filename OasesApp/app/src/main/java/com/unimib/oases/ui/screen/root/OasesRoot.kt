package com.unimib.oases.ui.screen.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.ui.components.scaffold.LoginScaffold
import com.unimib.oases.ui.components.scaffold.MainScaffold
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.login.AuthViewModel

@Composable
fun OasesRoot(
    bluetoothCustomManager: BluetoothCustomManager,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    val role by authViewModel.userRole.collectAsState()

    when (isAuthenticated){
        true -> {
            role?.let {

                val startDestination = when (it) {
                    Role.ADMIN -> Route.AdminDashboard
                    Role.DOCTOR, Role.NURSE -> Route.Home
                }

                MainScaffold(startDestination, bluetoothCustomManager, authViewModel)
            } ?: LoadingOverlay(true)
        }
        else -> LoginScaffold(authViewModel)
    }
}