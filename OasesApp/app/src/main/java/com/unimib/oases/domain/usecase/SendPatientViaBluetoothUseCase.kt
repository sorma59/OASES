package com.unimib.oases.domain.usecase

import android.bluetooth.BluetoothDevice
import android.util.Log
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.bluetooth.BluetoothEnvelope
import com.unimib.oases.data.bluetooth.BluetoothEnvelopeType
import com.unimib.oases.data.bluetooth.transfer.PatientFullData
import com.unimib.oases.data.mapper.serializer.PatientFullDataSerializer
import com.unimib.oases.data.mapper.serializer.PatientSerializer.serialize
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SendPatientViaBluetoothUseCase @Inject constructor(
    private val bluetoothManager: BluetoothCustomManager,
    private val visitRepository: VisitRepository,
    private val patientDiseaseRepository: PatientDiseaseRepository,
    private val visitVitalSignRepository: VisitVitalSignRepository
) {
    suspend operator fun invoke(patient: Patient, device: BluetoothDevice): Resource<Unit> {
        return try {
            var enabled = false
            var result: Resource<Unit> = Resource.Loading()
            withContext(Dispatchers.IO){
                bluetoothManager.ensureBluetoothEnabled(
                    onEnabled = {
                        enabled = true
                    }
                )
                if (enabled){
                    try {

                        val connectionSuccess = bluetoothManager.connectToServer(device)

                        if (connectionSuccess != null){
                            // Connection was successful

                            val currentVisit = visitRepository.getCurrentVisit(patient.id)

                            if (currentVisit != null){
                                // Get the patient's current visit's vital signs
                                val vitalSignsDeferred = async(Dispatchers.IO) {
                                    try {
                                        val resource = visitVitalSignRepository.getVisitVitalSigns(currentVisit.id)
                                            .first { it is Resource.Success || it is Resource.Error } // Wait for Success or Error
                                        resource.data ?: emptyList<VisitVitalSign>()
                                    } catch (e: NoSuchElementException) {
                                        // This catch block is important if the flow could complete
                                        // without ever emitting Success or Error (highly unlikely with your repo setup,
                                        // as Loading should be followed by Success/Error, or Error is emitted by catch).
                                        Log.e("SendPatient", "Vital signs flow completed without Success/Error for visit ${currentVisit.id}", e)
                                        emptyList<VisitVitalSign>()
                                    } catch (e: Exception) {
                                        Log.e("SendPatient", "Error collecting vital signs for visit ${currentVisit.id}", e)
                                        emptyList<VisitVitalSign>()
                                    }
                                }

                                val diseasesDeferred = async(Dispatchers.IO) {
                                    try {
                                        val resource = patientDiseaseRepository.getPatientDiseases(patient.id)
                                            .first { it is Resource.Success || it is Resource.Error } // Wait for Success or Error
                                        resource.data ?: emptyList<PatientDisease>()
                                    } catch (e: NoSuchElementException) {
                                        Log.e("SendPatient", "Diseases flow completed without Success/Error for patient ${patient.id}", e)
                                        emptyList<PatientDisease>()
                                    } catch (e: Exception) {
                                        Log.e("SendPatient", "Error collecting diseases for patient ${patient.id}", e)
                                        emptyList<PatientDisease>()
                                    }
                                }

                                // Wait for both tasks to complete
                                val diseases: List<PatientDisease> = diseasesDeferred.await()
                                val vitalSigns: List<VisitVitalSign> = vitalSignsDeferred.await()

                                val patientWithTriageData = PatientFullData(
                                    patientDetails = patient,
                                    visit = currentVisit,
                                    patientDiseases = diseases,
                                    vitalSigns = vitalSigns
                                )

                                // Serialize the patient data
                                val bytes = PatientFullDataSerializer.serialize(patientWithTriageData)

                                // Enclose the data in a BluetoothEnvelope
                                val envelope = BluetoothEnvelope(
                                    type = BluetoothEnvelopeType.PATIENT_WITH_TRIAGE_DATA.name,
                                    payload = bytes
                                )

                                // Convert the envelope to JSON
                                val jsonEnvelope = Json.encodeToString(BluetoothEnvelope.serializer(), envelope)

                                // Send data
                                bluetoothManager.sendData(jsonEnvelope)
                            } else {
                                val patientBytes = serialize(patient)
                                val envelope = BluetoothEnvelope(
                                    type = BluetoothEnvelopeType.PATIENT.name,
                                    payload = patientBytes
                                )
                                val patientEnvelopeJson = Json.encodeToString(BluetoothEnvelope.serializer(), envelope)

                                // Send the patient data
                                bluetoothManager.sendData(patientEnvelopeJson)
                            }

                            // Make sure the patient data is sent and received
                            delay(1000)
                            // Disconnect from the device
                            bluetoothManager.closeConnectionSocket()
                            result = Resource.Success(Unit)
                        } else
                            result = Resource.Error("Could not connect to device")

                    } catch (e: Exception) {
                        result = Resource.Error(e.message ?: "An error occurred")
                    }
                }
            }
            result

        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
