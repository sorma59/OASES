package com.unimib.oases.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.unimib.oases.OasesApp
import com.unimib.oases.R
import com.unimib.oases.data.mapper.PatientSerializer
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.util.PermissionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothCustomManager @Inject constructor(){

    private val appContext = OasesApp.getAppContext()
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        appContext.getSystemService(BluetoothManager::class.java)?.adapter
    }


    private val _receivedPatients = MutableSharedFlow<Patient>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val receivedPatients: SharedFlow<Patient> = _receivedPatients

    private val appName = appContext.getString(R.string.app_name)
    private val appUuid = UUID.fromString(appContext.getString(R.string.app_uuid))
    private val disconnectCode = appContext.getString(R.string.disconnect_request_code)

    // ------------Server---------------------

    private val serverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var serverJob: Job? = null

    // ------------Discovery------------------
    private val _isDiscovering = MutableStateFlow(false)
    val isDiscovering: StateFlow<Boolean> = _isDiscovering

    private val _remainingTime = MutableStateFlow<String?>(null)
    val remainingTime: StateFlow<String?> = _remainingTime

    private val _pairingResult = MutableStateFlow("")
    val pairingResult: StateFlow<String> = _pairingResult

    private val countdownScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var timerJob: Job? = null

    private var discoverableUntil = 0L

    // ------------Devices------------------
    private val _discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<BluetoothDevice>> = _discoveredDevices

    private var _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val pairedDevices: StateFlow<List<BluetoothDevice>> = _pairedDevices

    // ------------Sockets------------------
    private val connectionSocket = MutableStateFlow<BluetoothSocket?>(null)

    private var serverSocket: BluetoothServerSocket? = null

    // ---------------------Permissions----------------------
    private val _hasPermissions = MutableStateFlow(false)
    val hasPermissions: StateFlow<Boolean> = _hasPermissions.asStateFlow()

    // -------------Toast message-----------------
    private var _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    // ------------Snackbar message-----------------
    private var _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> handleDiscoveryStarted()

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> handleDiscoveryFinished()

                BluetoothDevice.ACTION_FOUND -> handleDeviceFound(intent)

                BluetoothAdapter.ACTION_STATE_CHANGED -> handleBluetoothStateChanged(intent)

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> handleBondStateChanged(intent)
            }
        }
    }

    // Mutable reference to launcher set by the Activity
    private var enableBluetoothLauncher: ActivityResultLauncher<Intent>? = null

    private var discoverableBluetoothLauncher: ActivityResultLauncher<Intent>? = null

    // -------------------Initialize and Deinitialize-------------------

    fun initialize(){
        Log.d("BluetoothServer", "Initializing")
        ensureBluetoothEnabled(
            onEnabled = {
                fetchPairedDevices()
                attemptStartServer()
            },
            onDenied = {
                updateToastMessage(appContext.getString(R.string.bluetooth_server_not_started))
            },
        )
    }

    fun deinitialize() {
        stopServer()
        emptyPairedDevices()
        emptyDiscoveredDevices()
        updateDiscoveryStatus(false)
        stopCountdown()
        discoverableUntil = 0L
        updateRemainingTime(null)
        serverJob?.cancel()
        serverJob = null
    }

    // -------------------Launchers setup--------------------------------------
    fun setEnableBluetoothLauncher(launcher: ActivityResultLauncher<Intent>) {
        enableBluetoothLauncher = launcher
    }

    fun setDiscoverableBluetoothLauncher(launcher: ActivityResultLauncher<Intent>) {
        discoverableBluetoothLauncher = launcher
    }

    // ------------------------State management------------------------------

    private fun updateDiscoveryStatus(isDiscovering: Boolean) {
        _isDiscovering.value = isDiscovering
    }

    private fun updateDiscoverabilityEndingTime(time: Int) {
        discoverableUntil = System.currentTimeMillis() + (time * 1000)
    }

    private fun updateToastMessage(message: String){
        _toastMessage.value = message
    }

    private fun updateSnackbarMessage(message: String){
        _snackbarMessage.value = message
    }

    private fun setConnectionSocket(socket: BluetoothSocket?){
        connectionSocket.value = socket
    }

    private fun updatePairingResult(result: String){
        _pairingResult.value = result
    }

    private fun updateRemainingTime(remainingTime: String?){
        _remainingTime.value = remainingTime
    }

    fun updatePermissions() {
        _hasPermissions.value = PermissionHelper.hasPermissions()
    }

    @SuppressLint("MissingPermission")
    fun fetchPairedDevices(){
        ensureBluetoothEnabled(
            onEnabled = {
                val pairedDevices = bluetoothAdapter?.bondedDevices
                if (pairedDevices != null)
                    _pairedDevices.value = pairedDevices.toList()
            }
        )
    }

    private fun emptyPairedDevices(){
        _pairedDevices.value = emptyList()
    }

    private fun emptyDiscoveredDevices() {
        _discoveredDevices.value = emptyList()
    }

    private fun addDiscoveredDevice(device: BluetoothDevice){
        if (!_discoveredDevices.value.contains(device))
            _discoveredDevices.value += device
    }

    // -----------------------Receiver handlers----------------------------------

    private fun handleDiscoveryStarted(){
        updateDiscoveryStatus(true)
    }

    private fun handleDiscoveryFinished(){
        updateDiscoveryStatus(false)
    }

    private fun handleDeviceFound(intent: Intent){
        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        else
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        device?.let {
            addDiscoveredDevice(it)
        }
    }

    private fun handleBluetoothStateChanged(intent: Intent){
        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
        when (state) {
            BluetoothAdapter.STATE_OFF -> {
                updateSnackbarMessage(appContext.getString(R.string.bluetooth_turning_off))
                deinitialize()
            }
            BluetoothAdapter.STATE_ON -> {
                initialize()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleBondStateChanged(intent: Intent){
        val device =
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE)

        if (bondState == BluetoothDevice.BOND_BONDED) {
            updatePairingResult("Paired with ${device?.name}")
        } else if (bondState == BluetoothDevice.BOND_NONE) {
            updatePairingResult("Failed to pair with ${device?.name}")
        }
    }

    // Function to ensure Bluetooth is enabled, using the ActivityResultLauncher
    fun ensureBluetoothEnabled(
        onEnabled: () -> Unit,
        onDenied: () -> Unit = {updateToastMessage(appContext.getString(R.string.bluetooth_denied))},
        onNotSupported: () -> Unit = {updateToastMessage(appContext.getString(R.string.bluetooth_not_supported))},
        onMissingPermission: () -> Unit = {updateToastMessage(appContext.getString(R.string.bluetooth_missing_permissions))},
    ) {
        if (!isBluetoothSupported()) {
            onNotSupported()
        } else {

            if (PermissionHelper.hasPermissions()){
                launchIntentIfNotEnabled(
                    onEnabled = onEnabled,
                    onDenied = onDenied
                )
            } else
                onMissingPermission()
        }
    }

    private fun launchIntentIfNotEnabled(
        onEnabled: () -> Unit,
        onDenied: () -> Unit = {updateToastMessage(appContext.getString(R.string.bluetooth_denied))},
    ) {
        if (!isBluetoothEnabled()) {
            launchBluetoothIntent(
                intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                launcher = enableBluetoothLauncher!!
            )
            // Save the callback to be used when the result is received
            bluetoothEnableCallback = object : BluetoothCallback {
                override fun onEnabled() {
                    onEnabled()
                }

                override fun onDenied() {
                    onDenied()
                }
            }
        } else
            onEnabled()
    }

    private fun launchBluetoothIntent(intent: Intent, launcher: ActivityResultLauncher<Intent>){
        // Launch the intent to enable Bluetooth
        launcher.launch(intent)
    }

    // Handle the Bluetooth enable result in the Activity
    fun handleEnableActivityResult(resultCode: Int) {
        if (resultCode == RESULT_OK) {
            bluetoothEnableCallback?.onEnabled()
        } else {
            bluetoothEnableCallback?.onDenied()
        }
    }

    fun handleDiscoverableActivityResult(resultCode: Int) {

        Log.d("BluetoothDiscoverable", "handleDiscoverableActivityResult called with resultCode: $resultCode")

        if (resultCode != BluetoothAdapter.STATE_CONNECTING && resultCode > 0) {
            // User accepted and set discoverable duration
            bluetoothDiscoverableCallback?.onEnabled()
        } else {
            // User canceled or denied
            bluetoothDiscoverableCallback?.onDenied()
        }

        // Clear the callback so it doesn't persist longer than needed
        bluetoothDiscoverableCallback = null
    }

    // Handle the Bluetooth enable result in the Activity
//    fun handleDiscoverableActivityResult(resultCode: Int) {
//        if (resultCode == RESULT_OK) {
//            bluetoothDiscoverableCallback?.onEnabled()
//        } else {
//            bluetoothDiscoverableCallback?.onDenied()
//        }
//    }

    interface BluetoothCallback {
        fun onEnabled()
        fun onDenied()
    }

    private var bluetoothEnableCallback: BluetoothCallback? = null

    private var bluetoothDiscoverableCallback: BluetoothCallback? = null

    private fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    // ---------------Connection------------------------

    private fun attemptStartServer() {
        serverJob?.cancel()
        serverJob = serverScope.launch {
            startServer()
        }
    }


    private suspend fun startServer() {

        var enabled = false

        ensureBluetoothEnabled(
            onEnabled = {
                enabled = true
            },
            onDenied = {
                updateToastMessage(appContext.getString(R.string.bluetooth_server_not_started))
            },
        )

        if (enabled){
            try {
                if (isBluetoothEnabled()) {
                    serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(appName, appUuid)
                    Log.d("BluetoothServer", "Server started, waiting for client...")
                    delay(1000)
                    val socket = withContext(Dispatchers.IO) {
                        acceptClientConnection()
                    }

                    if (socket != null) {
                        listenForData(socket) // Pass the new socket explicitly
                    }

                }
            } catch (e: SecurityException) {
                Log.e("BluetoothServer", "Permission denied: ${e.message}")
            } catch (e: IOException) {
                Log.e("BluetoothServer", "Server socket error: ${e.message}")
            } finally {
                serverSocket?.close() // Always close server socket after accepting
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

    fun stopServer() {
        try {
            // Close server socket
            closeServerSocket()
            // Close connection socket
            closeConnectionSocket()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun connectToServer(device: BluetoothDevice): BluetoothSocket? {
        if (!PermissionHelper.hasPermissions()) {
            Log.e("BluetoothClient", "Missing Bluetooth permissions")
            return null
        }

        return withContext(Dispatchers.IO) { // Run in background thread
            try {
                closeConnectionSocket()
                delay(500)
                val socket = device.createRfcommSocketToServiceRecord(appUuid)
                // Cancel discovery before connecting
                bluetoothAdapter?.cancelDiscovery()
                Log.d("BluetoothClient", "Connecting to server...")
                socket.connect()
                Log.d("BluetoothClient", "Connected to server!")
                setConnectionSocket(socket)
                socket // Return the connected socket
            } catch (e: SecurityException) {
                Log.e("BluetoothClient", "Permission denied: ${e.message}")
                null
            } catch (e: IOException) {
                Log.e("BluetoothClient", "Could not connect: ${e.message}")
                null
            }
        }
    }

    fun sendData(message: String) {
        try {
            val outputStream = connectionSocket.value?.outputStream ?: return
            val writer = BufferedWriter(OutputStreamWriter(outputStream, Charsets.UTF_8))
            writer.write(message)
            writer.newLine()
            writer.flush() // 💥 Must flush per message
            Log.d("Bluetooth", "Message sent: $message")
        } catch (e: IOException) {
            Log.e("Bluetooth", "Error sending message: ${e.message}")
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

    private fun closeServerSocket() {
        try {
            serverSocket?.close()
            serverSocket = null
        } catch (e: IOException) {
            Log.e("Bluetooth", "Error closing server socket: ${e.message}")
            throw e // Rethrow to let the caller know about the error
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
                    "data" -> {
                        val patient = PatientSerializer.deserialize(envelope.payload)
                        Log.d("BluetoothServer", "Received patient: $patient")
                        _receivedPatients.emit(patient)
                    }
                    "code" -> {
                        when (val command = envelope.payload.toString(Charsets.UTF_8)) {
                            disconnectCode -> {
                                Log.d("BluetoothServer", "Disconnection request received")
                            }
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

//    suspend fun disconnect(){
//        try {// Send a disconnect message
//
//            Log.d("BluetoothClient", "Sending disconnection code")
//            sendDisconnectionRequest()
//            Log.d("BluetoothClient", "Flushing stream")
//            connectionSocket.value?.outputStream?.flush()
//            Log.d("BluetoothClient", "Sleeping...")
//            delay(5000)
//            Log.d("BluetoothClient", "Closing socket")
//            closeConnectionSocket()
//        } catch (e: Exception){
//            Log.e("Bluetooth", "Error disconnecting: ${e.message}")
//        }
//    }

//    private fun sendDisconnectionRequest(){
//        val commandBytes = disconnectCode.toByteArray(Charsets.UTF_8)
//        val envelope = BluetoothEnvelope(
//            type = "code",
//            payload = commandBytes
//        )
//        val jsonEnvelope = Json.encodeToString(BluetoothEnvelope.serializer(), envelope)
//        sendData(jsonEnvelope)
//    }

    // ------------------Discovery-----------------------

    @SuppressLint("MissingPermission")
    fun startDiscovery() {
        ensureBluetoothEnabled(
            onEnabled = {
                if (bluetoothAdapter?.isDiscovering == true) {
                    bluetoothAdapter?.cancelDiscovery()
                    Handler(Looper.getMainLooper()).postDelayed({
                        bluetoothAdapter?.startDiscovery()
                    }, 500)  // Delay of 500ms (half a second) to allow cancelDiscovery() to complete
                } else
                    bluetoothAdapter?.startDiscovery()
            }
        )
    }

    // Stop discovery
    fun stopDiscovery() {
        if (ContextCompat.checkSelfPermission(
                appContext, Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED){
            Log.e("StopDiscovery", "Missing Bluetooth permissions")
        } else {
            bluetoothAdapter?.cancelDiscovery()
        }
    }

    // ------------------Pairing---------------------------

    fun makeDeviceDiscoverable(duration: Int) {
        ensureBluetoothEnabled(
            onEnabled = {
                val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration)
                }
                launchBluetoothIntent(discoverableIntent, discoverableBluetoothLauncher!!)
                bluetoothDiscoverableCallback = object : BluetoothCallback {
                    override fun onEnabled() {
                        Log.d("BluetoothDiscoverable", "onEnabled")
                        updateDiscoverabilityEndingTime(duration)
                        startCountdown()
                    }

                    override fun onDenied() {
                        Log.d("BluetoothDiscoverable", "onDenied")
                    }
                }
            }
        )
    }


    private fun startCountdown() {
        stopCountdown()
        timerJob = countdownScope.launch(Dispatchers.Main) {
            while (true) {
                val remainingMillis = discoverableUntil - System.currentTimeMillis()
                if (remainingMillis <= 0) {
                    updateRemainingTime(null)
                    break
                }
                val seconds = (remainingMillis / 1000).toInt()
                val minutesPart = seconds / 60
                val secondsPart = seconds % 60
                val formatted = "$minutesPart:${secondsPart.toString().padStart(2, '0')}"
                updateRemainingTime(formatted)

                delay(999)
            }
        }
    }

    private fun stopCountdown() {
        timerJob?.cancel()
        timerJob = null
    }

    @SuppressLint("MissingPermission")
    fun getThisDeviceName(): String? {
        if (isBluetoothSupported())
            if (PermissionHelper.hasPermissions())
                return bluetoothAdapter?.name
        return null
    }

    @SuppressLint("MissingPermission")
    fun pairWithDevice(device: BluetoothDevice): Boolean {

        var enabled = false

        ensureBluetoothEnabled(
            onEnabled = { enabled = true}
        )
        return if (enabled){
            try {
                updatePairingResult("Pairing with ${device.name}...")
                val method = device.javaClass.getMethod("createBond")
                val result = method.invoke(device) as Boolean
                if (!result)
                    updateToastMessage("Failed to pair with ${device.name}")
                result
            } catch (e: Exception) {
                Log.e("BluetoothClient", "Error creating bond: ${e.message}")
                false
            }
        } else
            false
    }

    private fun isBluetoothSupported(): Boolean{
        return bluetoothAdapter != null
    }
}




