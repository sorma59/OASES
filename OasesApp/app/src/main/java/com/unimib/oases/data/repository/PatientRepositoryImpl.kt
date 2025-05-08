package com.unimib.oases.data.repository

import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.data.mapper.toPatient
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
    private val bluetoothCustomManager: BluetoothCustomManager
) : PatientRepository {

    private val _receivedPatients = MutableSharedFlow<Patient>()
    override val receivedPatients: SharedFlow<Patient> = _receivedPatients.asSharedFlow()

    private val _newPatientEvents = MutableSharedFlow<Patient>()
    override val newPatientEvents: SharedFlow<Patient> = _newPatientEvents.asSharedFlow()

    init {
        listenForPatients()
    }

    private fun listenForPatients() {
        // Listening for Bluetooth patients
        CoroutineScope(Dispatchers.IO).launch {
            bluetoothCustomManager.receivedPatients.collect { patient ->
                var result = addPatient(patient)
                if (result is Resource.Error) {
                    // Try once more
                    result = addPatient(patient)
                    if (result is Resource.Error) {
                        // If still fails, log the error
                        return@collect
                    }
                }
                _receivedPatients.emit(patient)
                _newPatientEvents.emit(patient)
            }
        }
    }

    override suspend fun addPatient(patient: Patient): Resource<Unit> {
        return try {
            roomDataSource.insertPatient(patient.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun getPatients(): Flow<Resource<List<Patient>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getPatients().collect {
            emit(Resource.Success(it.map{entity -> entity.toPatient()}))
        }
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
    }

    override suspend fun getPatientById(id: String): Patient? {
        return roomDataSource.getPatientById(id)?.toPatient()
    }
}