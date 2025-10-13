package com.unimib.oases.domain.usecase

import android.bluetooth.BluetoothDevice
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.bluetooth.BluetoothEnvelope
import com.unimib.oases.data.bluetooth.BluetoothEnvelopeType
import com.unimib.oases.data.mapper.serializer.PatientFullDataSerializer
import com.unimib.oases.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SendPatientViaBluetoothUseCase @Inject constructor(
    private val bluetoothManager: BluetoothCustomManager,
    private val retrievePatientFullDataUseCase: RetrievePatientFullDataUseCase
) {
    suspend operator fun invoke(patientId: String, device: BluetoothDevice): Resource<Unit> {
        return try {
            var enabled = false
            withContext(Dispatchers.IO){
                bluetoothManager.ensureBluetoothEnabled(
                    onEnabled = {
                        enabled = true
                    }
                )
                return@withContext if (enabled){
                    try {
                        val connectionSuccess = bluetoothManager.connectToServer(device)

                        if (connectionSuccess != null){ // Connection was successful

                            val patientFullDataResource = retrievePatientFullDataUseCase(patientId)

                            if (patientFullDataResource is Resource.Error)
                                throw Exception(patientFullDataResource.message)

                            val patientFullData = (patientFullDataResource as Resource.Success).data!!

                            // Serialize the patient data
                            val bytes = PatientFullDataSerializer.serialize(patientFullData)

                            // Enclose the data in a BluetoothEnvelope
                            val envelope = BluetoothEnvelope(
                                type = BluetoothEnvelopeType.PATIENT.name,
                                payload = bytes
                            )

                            // Convert the envelope to JSON
                            val jsonEnvelope = Json.encodeToString(BluetoothEnvelope.serializer(), envelope)

                            // Send data
                            bluetoothManager.sendData(jsonEnvelope)

                            // Make sure the patient data is sent and received
                            delay(1000)
                            Resource.Success(Unit)
                        } else
                            Resource.Error("Could not connect to device")

                    } catch (e: Exception) {
                        Resource.Error(e.message ?: "An error occurred")
                    } finally {
                        // Disconnect from the device
                        bluetoothManager.closeConnectionSocket()
                    }
                } else
                    Resource.Error("Bluetooth is not enabled")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
