package com.unimib.oases.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Helper class to handle Bluetooth permissions based on Android version.
 */
object PermissionHelper {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /**
     * Returns the required Bluetooth permissions based on API level.
     * Uses BLUETOOTH and BLUETOOTH_ADMIN for API < 31.
     */
    fun getBluetoothPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }
    }

    /**
     * Checks if all required Bluetooth permissions are granted.
     */
    fun hasBluetoothPermissions(): Boolean {
        return getBluetoothPermissions().all {
            ContextCompat.checkSelfPermission(appContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // ----------------------------
    // Notification permission (Android 13+)
    // ----------------------------
    fun needsNotificationPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun hasNotificationPermission(): Boolean {
        return if (needsNotificationPermission()) {
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true // Below API 33, permission not needed
    }

    fun getNotificationPermission(): Array<String> {
        return if (needsNotificationPermission()) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else emptyArray()
    }
}