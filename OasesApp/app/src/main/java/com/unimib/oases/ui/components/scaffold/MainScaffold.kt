package com.unimib.oases.ui.components.scaffold

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.ui.navigation.AppNavigation
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.root.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScaffold(
    startDestination: String,
    bluetoothManager: BluetoothCustomManager,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold { padding ->
        AppNavigation(startDestination, navController, padding, bluetoothManager, authViewModel, appViewModel)
    }

//    LaunchedEffect(Unit) {
//        appViewModel.uiEvents.collect { uiEvent ->
//            when (uiEvent) {
//                is UiEvent.Toast -> ToastUtils.showToast(context, uiEvent.message)
//                is UiEvent.Dialog -> showDialog(uiEvent.message)
//            }
//        }
//    }

    // Collect global navigation/UI events
    LaunchedEffect(Unit) {
        launch {
            appViewModel.navEvents.collect { event ->
                when (event) {
                    is NavigationEvent.Navigate -> navController.navigate(event.route)
                    NavigationEvent.NavigateBack -> navController.popBackStack()
                }
            }
        }
//        launch {
//            appViewModel.uiEvents.collect { event ->
//                when (event) {
//                    is UiEvent.ShowToast -> ToastUtils.showToast(navController.context, event.message)
//                    is UiEvent.ShowDialog -> showDialog(event.message)
//                }
//            }
//        }
    }
}