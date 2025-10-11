package com.unimib.oases.ui.components.util.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.unimib.oases.R
import com.unimib.oases.util.PermissionHelper

@Composable
fun NotificationPermissionHandler(
    context: Context
){
    var showRationale by remember { mutableStateOf(false) }

    val permissions = PermissionHelper.getNotificationPermission()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ }

    // Check and request permissions
    LaunchedEffect(Unit) {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (deniedPermissions.isNotEmpty()){
            val shouldShowRationale = deniedPermissions.any {
                shouldShowRequestPermissionRationale(
                    context,
                    it
                )
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
            title = "Notification Permission",
            text = """
                ${R.string.app_name} needs notification permissions to send notifications to you.
                Notifications are used to notify you when a patient's data is sent to this device when Oases is in the background.
                You can either grant the permissions or deny them, notifications are not strictly needed.
                """.trimIndent(),
            onDismiss = { showRationale = false },
            onConfirm = {
                showRationale = false
                permissionLauncher.launch(permissions)
            }
        )
    }
}
