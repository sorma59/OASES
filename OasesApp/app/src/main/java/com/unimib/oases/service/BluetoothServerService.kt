package com.unimib.oases.service

import android.annotation.SuppressLint
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
import com.unimib.oases.OasesApp
import com.unimib.oases.R
import com.unimib.oases.bluetooth.BluetoothCustomManager
import com.unimib.oases.bluetooth.BluetoothEnvelope
import com.unimib.oases.bluetooth.BluetoothEnvelopeType
import com.unimib.oases.bluetooth.PatientHandler
import com.unimib.oases.data.mapper.serializer.PatientFullDataSerializer
import com.unimib.oases.util.OasesNotificationManager
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

    private val isServerRunning: Boolean
        get() = serverSocket != null

    @Inject
    lateinit var bluetoothManager: BluetoothCustomManager

    @Inject
    lateinit var notificationManager: OasesNotificationManager

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
                stopServer(shouldUpdateNotification = true)
            }
            BluetoothAdapter.STATE_ON -> {
                attemptStartServer()
            }
        }
    }

    // ------------Service----------------

    override fun onCreate() {
        super.onCreate()
        setServiceNotification()
        patientHandler = bluetoothManager

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        attemptStartServer()

        return START_STICKY
    }

    override fun onDestroy() {
        stopServer()
        serviceJob.cancel()
        unregisterReceiver(bluetoothReceiver)
        stopForeground(0)
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("BluetoothServer", "Service task removed, stopping server")
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

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

                    updateServerNotification()
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

    private fun setServiceNotification() {
        val notificationWithId =
            notificationManager.getBluetoothServiceNotification(isServerRunning)
        startForeground(notificationWithId.id, notificationWithId.notification)
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
                        val patientFullData = PatientFullDataSerializer.deserialize(envelope.payload)
                        Log.d("BluetoothServer", "Received patient with triage data: $patientFullData")
                        patientHandler.onPatientReceived(patientFullData)
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

    fun stopServer(shouldUpdateNotification: Boolean = false) {
        try {
            // Close server socket
            closeServerSocket()
            // Close connection socket
            closeConnectionSocket()
            // Update notification
            if (shouldUpdateNotification)
                updateServerNotification()
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

    private fun updateServerNotification() {
        notificationManager.showBluetoothServiceNotification(isServerRunning)
    }
}
