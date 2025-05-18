package com.unimib.oases.service

//@AndroidEntryPoint
//class BluetoothServerService : Service() {
//

//    lateinit var bluetoothManager: BluetoothCustomManager
//
//    private val serviceJob = SupervisorJob()
//    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
//
//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        startForeground(1, buildNotification())
//
//        serviceScope.launch {
//            bluetoothManager.startServer()
//        }
//
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        bluetoothManager.stopServer()
//        serviceJob.cancel()
//        super.onDestroy()
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    private fun buildNotification(): Notification {
//        return NotificationCompat.Builder(this, "bluetooth_channel")
//            .setContentTitle("Bluetooth Server Running")
//            .setContentText("Listening for incoming connections")
//            .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .build()
//    }
//
//    private fun createNotificationChannel() {
//        val serviceChannel = NotificationChannel(
//            "bluetooth_channel",
//            "Bluetooth Server Channel",
//            NotificationManager.IMPORTANCE_LOW
//        )
//        val manager = getSystemService(NotificationManager::class.java)
//        manager.createNotificationChannel(serviceChannel)
//    }
//}
