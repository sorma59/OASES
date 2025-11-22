package com.unimib.oases.ui.components.scaffold

import android.content.Context
import android.util.Log
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
import com.unimib.oases.ui.util.SnackbarController
import com.unimib.oases.ui.util.ToastUtils
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
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

//    var currentRoute: Route by remember { mutableStateOf(startDestination) }

    // Create a single Json instance for decoding route arguments
//    val json = remember {
//        Json {
//            ignoreUnknownKeys = true
//            encodeDefaults = true
//        }
//    }
//
//    // Cache serializers for all known Route subclasses
//    val routeSerializers = remember {
//        Route::class.sealedSubclasses
//            .mapNotNull { subclass ->
//                subclass.simpleName?.let { routeName ->
//                    subclass.serializerOrNull()?.let { serializer ->
//                        routeName to serializer
//                    }
//                }
//            }
//            .toMap()
//    }
//
//    // Listen for route changes and decode automatically
//    LaunchedEffect(navBackStackEntry) {
//
//        val destinationRoute = navBackStackEntry?.destination?.route ?: return@LaunchedEffect
//
//        // Find a matching serializer by name prefix
//        val serializerEntry = routeSerializers.entries.find { (name, _) ->
//            destinationRoute.startsWith(name)
//        } ?: return@LaunchedEffect
//
//        val serializer = serializerEntry.value
//        Log.d("Prova", "about to change the current Route: $currentRoute")
//        currentRoute = run {
//            json.decodeFromString(serializer, destinationRoute)
//        }
//        Log.d("Prova", "changed the current Route: $currentRoute")
//    }

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