package com.unimib.oases.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.unimib.oases.MainActivity
import com.unimib.oases.R

class OasesNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID_GENERAL = "general_channel"
        const val CHANNEL_ID_FOREGROUND_SERVICE = "foreground_service_channel"

        const val NOTIFICATION_ID_BLUETOOTH_SERVICE = 1001
    }

    init {
        createNotificationChannels()
    }

    val mainIntent = Intent(context, MainActivity::class.java)
    val pendingIntent: PendingIntent? = PendingIntent.getActivity(
        context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE
    )

    /**
     * Creates two channels:
     *  - General: for regular notifications
     *  - Foreground: for persistent service notifications
     */
    private fun createNotificationChannels() {
        val generalChannel = NotificationChannel(
            CHANNEL_ID_GENERAL,
            "General Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Used for general app alerts like Bluetooth or patient updates."
        }

        val foregroundChannel = NotificationChannel(
            CHANNEL_ID_FOREGROUND_SERVICE,
            "Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Used for ongoing background operations (e.g. Bluetooth server)."
            setShowBadge(false)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(generalChannel)
        manager.createNotificationChannel(foregroundChannel)
    }

    /**
     * Simple one-time notification, e.g. when a patient is received.
     */
    @SuppressLint("MissingPermission")
    fun showSuccessfulNotification(patientId: String, patientName: String) {
        if (PermissionHelper.hasNotificationPermission()){
            val notification = NotificationCompat.Builder(context, CHANNEL_ID_GENERAL)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setContentTitle("New patient received: $patientName")
                .setContentText("$patientName and their data were successfully saved on this device.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            NotificationManagerCompat
                .from(context)
                .notify(patientId.hashCode(), notification)
        }
    }

    @SuppressLint("MissingPermission")
    fun showUnsuccessfulNotification(patientId: String, patientName: String) {
        if (PermissionHelper.hasNotificationPermission()){
            val notification = NotificationCompat.Builder(context, CHANNEL_ID_GENERAL)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setContentTitle("Error while receiving patient $patientName")
                .setContentText("An error occurred while receiving this patient, have it sent to this device again.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            NotificationManagerCompat
                .from(context)
                .notify(patientId.hashCode(), notification)
        }
    }

    /**
     * Returns a notification suitable for use in startForeground().
     */
    @SuppressLint("MissingPermission")
    fun showBluetoothServiceNotification(isRunning: Boolean = true) {
        if (PermissionHelper.hasNotificationPermission()){
            val notificationWithId = getBluetoothServiceNotification(isRunning)

            NotificationManagerCompat
                .from(context)
                .notify(notificationWithId.id, notificationWithId.notification)
        }
    }

    fun getBluetoothServiceNotification(isRunning: Boolean): NotificationWithId {
        val title = if (isRunning) "Bluetooth Server Running" else "Bluetooth Server Stopped"
        val text = if (isRunning)
            "Ready to receive patient data"
        else
            "Turn on Bluetooth to be able to receive patient data again"

        return NotificationWithId(
            NOTIFICATION_ID_BLUETOOTH_SERVICE,
            NotificationCompat.Builder(context, CHANNEL_ID_FOREGROUND_SERVICE)
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .setContentTitle(title)
                .setContentText(text)
                .setOngoing(isRunning) // Cannot be dismissed
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent) // Open oases when tapped
                .setShowWhen(false) // Do not show time
                .build()
        )
    }

//    fun cancelNotification(id: Int) {
//        NotificationManagerCompat.from(context).cancel(id)
//    }
//
//    fun cancelAll() {
//        NotificationManagerCompat.from(context).cancelAll()
//    }
}

data class NotificationWithId(
    val id: Int,
    val notification: Notification
)
