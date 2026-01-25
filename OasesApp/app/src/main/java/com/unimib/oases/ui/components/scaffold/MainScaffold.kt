package com.unimib.oases.ui.components.scaffold

import android.util.Log
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.bluetooth.BluetoothCustomManager
import com.unimib.oases.ui.components.util.permission.BluetoothPermissionHandler
import com.unimib.oases.ui.components.util.permission.NoPermissionMessage
import com.unimib.oases.ui.components.util.permission.NotificationPermissionHandler
import com.unimib.oases.ui.navigation.AppNavigation
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.ToastUtils
import com.unimib.oases.ui.util.snackbar.SnackbarController
import com.unimib.oases.ui.util.snackbar.SnackbarData
import com.unimib.oases.ui.util.snackbar.SnackbarType
import kotlinx.coroutines.launch

@Composable
fun MainScaffold(
    startDestination: Route,
    bluetoothManager: BluetoothCustomManager,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val hasPermissions by bluetoothManager.hasPermissions.collectAsState()

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

        NotificationPermissionHandler(context)

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
                        navController = navController,
                        onBack = {
                            appViewModel.onNavEvent(NavigationEvent.NavigateBack)
                        },
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    )

                },
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->

                        // Determine colors based on the type set in the controller
                        val type = SnackbarController.currentType

                        GenerateSnackbar(type, data)
                    }
                }
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

    // Collect global navigation/UI events
    LaunchedEffect(Unit) {
        launch {
            appViewModel.navEvents.collect { event ->
                when (event) {
                    is NavigationEvent.Navigate -> navController.navigate(event.route)
                    is NavigationEvent.PopAndNavigate -> {
                        navController.popBackStack()
                        navController.navigate(event.route)
                    }
                    NavigationEvent.NavigateBack -> navController.popBackStack()
                    is NavigationEvent.NavigateBackWithResult<*> -> {

                        val previousEntry = navController.previousBackStackEntry
                        if (previousEntry == null) {
                            Log.e("Navigation", "Cannot set result, previous back stack entry is null")
                            navController.popBackStack() // Still pop, but log error
                            return@collect
                        }

                        previousEntry.savedStateHandle[event.key] = event.result

                        navController.popBackStack()
                    }

                }
            }
        }
        launch {
            appViewModel.uiEvents.collect { event ->
                when (event) {
                    is UiEvent.ShowToast -> ToastUtils.showToast(navController.context, event.message)
                    is UiEvent.ShowSnackbar -> SnackbarController.showMessage(
                        event.snackbarData.message,
                        event.snackbarData.type,
                        event.snackbarData.actionLabel,
                        event.snackbarData.onAction
                    )
                }
            }
        }
    }
}

@Composable
private fun GenerateSnackbar(
    type: SnackbarType,
    data: androidx.compose.material3.SnackbarData
) {
    when (type) {
        SnackbarType.INFO -> {
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                actionColor = MaterialTheme.colorScheme.primary,
                dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.medium
            )
        }

        SnackbarType.SUCCESS -> {
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actionColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dismissActionContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.medium
            )
        }

        SnackbarType.ERROR -> {
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                actionColor = MaterialTheme.colorScheme.onErrorContainer,
                dismissActionContentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}

sealed class UiEvent {
    data class ShowToast(val message: String): UiEvent()
    data class ShowSnackbar(val snackbarData: SnackbarData): UiEvent()
}