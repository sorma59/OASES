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

                        if (connectionSuccess != null){

                            val patientFullDataResource = retrievePatientFullDataUseCase(patientId)

                            if (patientFullDataResource is Resource.Error)
                                throw Exception(patientFullDataResource.message)

                            val patientFullData = (patientFullDataResource as Resource.Success).data!!

                            // Connection was successful

                            /*
                                        val currentVisit = getCurrentVisitUseCase(patient.id)

                                        if (currentVisit != null){

                                            val triageEvaluation = triageEvaluationRepository.getTriageEvaluation(currentVisit.id).data!!

                                            val result = malnutritionScreeningRepository.getMalnutritionScreening(currentVisit.id)
                                            val malnutritionScreening = if (result is Resource.Success) result.data else null

                                            // Get the patient's current visit's vital signs
                                            val vitalSignsDeferred = async(Dispatchers.IO) {
                                                try {
                                                    val resource = visitVitalSignRepository.getVisitVitalSigns(currentVisit.id)
                                                        .first { it is Resource.Success || it is Resource.Error } // Wait for Success or Error
                                                    resource.data ?: emptyList<VisitVitalSign>()
                                                } catch (e: NoSuchElementException) {
                                                    // This catch block is important if the flow could complete
                                                    // without ever emitting Success or Error (highly unlikely with current setup).
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

                                            val complaintsSummariesDeferred = async(Dispatchers.IO) {
                                                try {
                                                    val resource =
                                                        complaintSummaryRepository.getVisitComplaintsSummaries(
                                                            currentVisit.id
                                                        )
                                                            .first { it is Resource.Success || it is Resource.Error } // Wait for Success or Error
                                                    resource.data ?: emptyList<ComplaintSummary>()
                                                } catch (e: NoSuchElementException) {
                                                    Log.e("SendPatient", "Complaints flow completed without Success/Error for visit ${currentVisit.id}", e)
                                                    emptyList<ComplaintSummary>()
                                                } catch (e: Exception) {
                                                    Log.e("SendPatient", "Error collecting complaints for visit ${currentVisit.id}", e)
                                                    emptyList<ComplaintSummary>()
                                                }
                                            }

                                            // Wait for both tasks to complete
                                            val diseases: List<PatientDisease> = diseasesDeferred.await()
                                            val vitalSigns: List<VisitVitalSign> = vitalSignsDeferred.await()
                                            val complaintsSummaries: List<ComplaintSummary> = complaintsSummariesDeferred.await()

                                            val patientWithTriageData = PatientFullData(
                                                patientDetails = patient,
                                                visit = currentVisit,
                                                patientDiseases = diseases,
                                                vitalSigns = vitalSigns,
                                                triageEvaluation = triageEvaluation,
                                                malnutritionScreening = malnutritionScreening,
                                                complaintsSummaries = complaintsSummaries
                                            )
            */

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
