package com.unimib.oases.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.unimib.oases.OasesApp
import com.unimib.oases.R
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.bluetooth.BluetoothEnvelope
import com.unimib.oases.data.bluetooth.BluetoothEnvelopeType
import com.unimib.oases.data.bluetooth.PatientHandler
import com.unimib.oases.data.mapper.serializer.PatientFullDataSerializer
import com.unimib.oases.data.mapper.serializer.PatientSerializer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothServerService () : Service() {

    private val appContext = OasesApp.getAppContext()
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        appContext.getSystemService(BluetoothManager::class.java)?.adapter
    }

    private var isServerRunning = false
    @Inject
    lateinit var bluetoothManager: BluetoothCustomManager
    private lateinit var patientHandler: PatientHandler

    private var serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    // ------------Sockets------------------
    private val connectionSocket = MutableStateFlow<BluetoothSocket?>(null)

    private var serverSocket: BluetoothServerSocket? = null

    // ---------------Connection uuids-------------------------------------
    private val appName = appContext.getString(R.string.app_name)
    private val appUuid = UUID.fromString(appContext.getString(R.string.app_uuid))

    // ------------Receiver--------------------------------

    val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {

                BluetoothAdapter.ACTION_STATE_CHANGED -> handleBluetoothStateChanged(intent)

            }
        }
    }

    private fun handleBluetoothStateChanged(intent: Intent){
        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
        when (state) {
            BluetoothAdapter.STATE_OFF -> {
                stopServer()
            }
            BluetoothAdapter.STATE_ON -> {
                attemptStartServer()
            }
        }
    }

    // ------------Service----------------

    override fun onCreate() {
        super.onCreate()
        patientHandler = bluetoothManager
        createNotificationChannel()

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, buildNotification())

        attemptStartServer()

        return START_STICKY
    }

    override fun onDestroy() {
        stopServer()
        serviceJob.cancel()
        unregisterReceiver(bluetoothReceiver)
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("BluetoothServer", "Service task removed, stopping server")
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "bluetooth_channel")
            .setContentTitle("Bluetooth Server Running")
            .setContentText("Listening for incoming connections")
            .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            "bluetooth_channel",
            "Bluetooth Server Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    // ------------------Server-------------------------

    private fun isBluetoothSupported(): Boolean{
        return bluetoothAdapter != null
    }

    private fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    private fun setConnectionSocket(socket: BluetoothSocket?){
        connectionSocket.value = socket
    }

    private fun attemptStartServer() {
        serviceScope.launch {
            startServer()
        }
    }

    private suspend fun startServer() {

        if (isServerRunning) return

        if (isBluetoothSupported()){
            try {
                if (isBluetoothEnabled()) {
                    serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(appName, appUuid)
                    isServerRunning = true
                    // Server started, waiting for client...
                    val socket = acceptClientConnection()

                    if (socket != null) {
                        listenForData(socket)
                    }
                }
            } catch (e: SecurityException) {
                Log.e("BluetoothServer", "Permission denied: ${e.message}")
            } catch (e: IOException) {
                Log.e("BluetoothServer", "Server socket error: ${e.message}")
            } finally {
                serverSocket?.close() // Always close server socket after accepting: one connection at a time
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun acceptClientConnection(): BluetoothSocket? {
        return withContext(Dispatchers.IO) { // Run in background thread
            try {
                val connectedSocket = serverSocket?.accept()
                Log.d("BluetoothServer", "Client connected: ${connectedSocket?.remoteDevice?.name}")
                setConnectionSocket(connectedSocket)
                connectedSocket
            } catch (e: IOException) {
                Log.e("BluetoothServer", "Error accepting connection: ${e.message}")
                null
            }
        }
    }

    private suspend fun listenForData(socket: BluetoothSocket) {
        try {
            val inputStream = socket.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))

            while (true) { // Keep listening for messages

                val line = withContext(Dispatchers.IO) {
                    try {
                        reader.readLine() // Reads until \n
                    } catch (e: IOException) {
                        Log.d("BluetoothServer", "IO Exception while reading: ${e.message}")
                        null // Treat IOException during read as disconnection
                    }
                }

                if (line == null) {
                    Log.d("BluetoothServer", "Client disconnected, no more data.")
                    break
                }

                Log.d("BluetoothServer", "Received message: $line")

                val envelope = Json.decodeFromString(BluetoothEnvelope.serializer(), line)

                when (envelope.type) {
                    BluetoothEnvelopeType.PATIENT.name -> {
                        val patient = PatientSerializer.deserialize(envelope.payload)
                        Log.d("BluetoothServer", "Received patient: $patient")
                        patientHandler.onPatientReceived(patient)
                    }
                    BluetoothEnvelopeType.PATIENT_WITH_TRIAGE_DATA.name -> {
                        val patientWithTriageData = PatientFullDataSerializer.deserialize(envelope.payload)
                        Log.d("BluetoothServer", "Received patient with triage data: $patientWithTriageData")
                        patientHandler.onPatientWithTriageDataReceived(patientWithTriageData)
                    }
                    BluetoothEnvelopeType.COMMAND.name -> {
                        when (val command = envelope.payload.toString(Charsets.UTF_8)) {
//                            disconnectCode -> {
//                                Log.d("BluetoothServer", "Disconnection request received")
//                            }
                            else -> Log.w("BluetoothServer", "Unknown command: $command")
                        }
                    }
                    else -> Log.w("BluetoothServer", "Unknown type: ${envelope.type}")
                }
            }
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Connection lost: ${e.message}")
        } finally {
            Log.d("BluetoothServer", "Restarting server...")
            restartServer()
        }
    }

    private suspend fun restartServer(){
        stopServer()
        delay(1000)
        attemptStartServer()
    }

    fun stopServer() {
        try {
            // Close server socket
            closeServerSocket()
            // Close connection socket
            closeConnectionSocket()
            // Set server running flag to false
            isServerRunning = false
            Log.d("BluetoothServer", "Server stopped")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun closeServerSocket() {
        try {
            serverSocket?.close()
            serverSocket = null
        } catch (e: IOException) {
            Log.e("Bluetooth", "Error closing server socket: ${e.message}")
            throw e // Rethrow to let the caller know about the error
        }
    }

    internal fun closeConnectionSocket() {
        try {
            connectionSocket.value?.close()
            setConnectionSocket(null)
        } catch (e: IOException) {
            Log.e("Bluetooth", "Error closing socket: ${e.message}")
            throw e // Rethrow to let the caller know about the error
        }
    }
}
