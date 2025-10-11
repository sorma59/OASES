package com.unimib.oases.ui.components.scaffold

import android.content.Context
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.ui.components.util.permission.BluetoothPermissionHandler
import com.unimib.oases.ui.components.util.permission.NoPermissionMessage
import com.unimib.oases.ui.components.util.permission.NotificationPermissionHandler
import com.unimib.oases.ui.navigation.AppNavigation
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Screen.Companion.screenOf
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.SnackbarController
import com.unimib.oases.ui.util.ToastUtils
import kotlinx.coroutines.launch

@Composable
fun MainScaffold(
    startDestination: String,
    bluetoothManager: BluetoothCustomManager,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val snackbarHostState = remember { SnackbarHostState() }

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    val screen = remember(currentRoute) { screenOf(currentRoute ?: startDestination) }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val hasPermissions by bluetoothManager.hasPermissions.collectAsState()

    val onNavigationIconClick = { screenType: OasesTopAppBarType ->
        when (screenType) {
            OasesTopAppBarType.MENU -> {
                scope.launch {
                    drawerState.open()
                }
            }
            OasesTopAppBarType.BACK -> appViewModel.onNavEvent(NavigationEvent.NavigateBack)
        }
    }

    BluetoothPermissionHandler(
        context = context,
        onPermissionGranted = {
            bluetoothManager.updatePermissions()
            bluetoothManager.initialize()
        }
    )



    if (!hasPermissions)
        NoPermissionMessage(context, bluetoothManager)
    else {

        NotificationPermissionHandler(
            context = context,
        )

        LaunchedEffect(Unit) {
            SnackbarController.setHostState(snackbarHostState)
        }

        OasesDrawer(
            drawerState = drawerState,
            authViewModel = authViewModel,
            navController = navController,
        ) {
            Scaffold(
                topBar = {
                    OasesTopAppBar(
                        onNavigationIconClick = { onNavigationIconClick(screen.type) },
                        screen = screen
                    )

                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { padding ->

                AppNavigation(
                    startDestination,
                    navController,
                    authViewModel,
                    appViewModel,
                    Modifier
                        .consumeWindowInsets(padding)
                        .padding(padding)
                )
            }
        }
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
        launch {
            appViewModel.uiEvents.collect { event ->
                when (event) {
                    is UiEvent.ShowToast -> ToastUtils.showToast(navController.context, event.message)
                    is UiEvent.ShowSnackbar -> SnackbarController.showMessage(
                        event.message,
                        event.actionLabel,
                        event.onAction
                    )
                }
            }
        }
    }
}

sealed class UiEvent {
    data class ShowToast(val message: String, val context: Context): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val onAction: (() -> Unit)? = null
    ): UiEvent()
}