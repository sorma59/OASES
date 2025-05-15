package com.unimib.oases.ui.components.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.util.PermissionHelper

@Composable
fun BluetoothPermissionHandler(
    context: Context,
    onPermissionGranted: () -> Unit,
) {
    var showRationale by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val permissions = PermissionHelper.getRequiredPermissions()

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.all { it.value }) {
                onPermissionGranted()
            } else {
                showSettingsDialog = true
            }
        }

    // Check and request permissions
    LaunchedEffect(Unit) {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (deniedPermissions.isEmpty()) {
            onPermissionGranted()
        } else {
            val shouldShowRationale = deniedPermissions.any {
                shouldShowRequestPermissionRationale(context, it)
            }
            if (shouldShowRationale) {
                showRationale = true
            } else {
                permissionLauncher.launch(deniedPermissions.toTypedArray())
            }
        }
    }

    if (showRationale) {
        PermissionRationaleDialog(
            onDismiss = { showRationale = false },
            onConfirm = {
                showRationale = false
                permissionLauncher.launch(permissions)
            }
        )
    }

    if (showSettingsDialog) {
        GoToSettingsDialog(
            onDismiss = { showSettingsDialog = false },
            onConfirm = {
                showSettingsDialog = false
                openAppSettings(context)
            }
        )
    }
}

@Composable
fun PermissionRationaleDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bluetooth Permission Needed") },
        text = { Text("This app needs Bluetooth permissions to find and connect to devices. Please grant them to continue.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun GoToSettingsDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission Required") },
        text = { Text("You have denied Bluetooth permissions permanently. Please go to Settings to enable them manually.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Go to Settings") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun NoPermissionMessage(
    context: Context,
    bluetoothManager: BluetoothCustomManager,
    onBack: () -> Unit = {},
    showBackButton: Boolean = false
) {
    var showRationale by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val permissions = PermissionHelper.getRequiredPermissions()

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.all { it.value }) {
                bluetoothManager.updatePermissions()
            } else {
                showSettingsDialog = true
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        CenteredText(
            text = "Bluetooth permissions are required to find and connect to devices.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = {
            val deniedPermissions = permissions.filter {
                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
            }
            val shouldShowRationale = deniedPermissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as? ComponentActivity) ?: return@Button,
                    it
                )
            }

            if (shouldShowRationale) {
                showRationale = true
            } else {
                permissionLauncher.launch(permissions)
            }
        }) {
            Text("Grant Permissions")
        }

        if (showBackButton){
            Button(onClick = onBack) {
                Text("Go Back")
            }
        }
    }

    if (showRationale) {
        PermissionRationaleDialog(
            onDismiss = { showRationale = false },
            onConfirm = {
                showRationale = false
                permissionLauncher.launch(permissions)
            }
        )
    }

    if (showSettingsDialog) {
        GoToSettingsDialog(
            onDismiss = { showSettingsDialog = false },
            onConfirm = {
                showSettingsDialog = false
                openAppSettings(context)
            }
        )
    }
}

fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        (context as? ComponentActivity) ?: return false,
        permission
    )
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("AppSettings", "Unable to open app settings: ${e.message}")
        throw e
    }
}