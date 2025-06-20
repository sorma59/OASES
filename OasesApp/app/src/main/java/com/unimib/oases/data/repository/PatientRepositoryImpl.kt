package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.data.mapper.toPatient
import com.unimib.oases.data.util.FirestoreManager
import com.unimib.oases.di.ApplicationScope
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val firestoreManager: FirestoreManager,
) : PatientRepository {

    private val _receivedPatients = MutableStateFlow<List<Patient>>(emptyList())
    override val receivedPatients: StateFlow<List<Patient>> = _receivedPatients.asStateFlow()

    private val _newPatientEvents = MutableSharedFlow<Patient>()
    override val newPatientEvents: SharedFlow<Patient> = _newPatientEvents.asSharedFlow()

    override suspend fun addPatient(patient: Patient): Resource<Unit> {
        return try {
            Log.e("PatientRepositoryImpl", "Adding patient: $patient")
            roomDataSource.insertPatient(patient.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun removePatientFromRecentlyReceived(patient: Patient) {
        val currentPatients = _receivedPatients.firstOrNull() ?: emptyList()

        val updatedPatients = currentPatients - patient

        _receivedPatients.emit(updatedPatients)
    }

    override suspend fun deletePatient(patient: Patient): Resource<Unit> {

        return try {
            roomDataSource.deletePatient(patient.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }

    }

    override suspend fun getPatientById(patientId: String): Flow<Resource<Patient?>> = flow {
        emit(Resource.Loading())
        try {
            roomDataSource.getPatientById(patientId).collect {
                emit(Resource.Success(it?.toPatient()))
            }
        } catch
            (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getPatients(): Flow<Resource<List<Patient>>> = flow {


        emit(Resource.Loading())
        roomDataSource.getPatients().collect {
                it.forEach { patient ->
                    println(patient)
                }

            emit(Resource.Success(it.asReversed().map { entity -> entity.toPatient() }))
        }
    }.catch { e ->
        Log.e("PatientRepositoryImpl", "Error getting patients: ${e.message}")
        emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
    }

    override suspend fun updateTriageState(patient: Patient, triageState: String): Resource<Unit> {
        return try {
            roomDataSource.updateTriageState(patient.toEntity(), triageState)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateStatus(patient: Patient, status: String): Resource<Unit> {
        return try {
            roomDataSource.updateStatus(patient.toEntity(), status)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}