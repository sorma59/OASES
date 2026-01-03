package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import java.time.LocalDate
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
//    private val firestoreApi: FirebaseFirestoreSource,
//    private val firestoreManager: FirestoreManager
) : PatientRepository {

//    init {
//        CoroutineScope(dispatcher).launch {
//            firestoreManager.onlineStatus.collect { isOnline ->
//                if (isOnline) {
//                    doOnlineTasks()
//                } else {
//                    doOfflineTasks()
//                }
//            }
//        }
//    }


    override suspend fun addPatient(patient: Patient): Outcome<String> {

        return try {
            roomDataSource.insertPatient(patient.toEntity())

//            if(firestoreManager.isOnline()){
//                Log.e("PatientRepositoryImpl", "Adding patient to firestore: $patient")
//                //firestoreManager.addPatient(patient.toEntity())
//            } else {
//                Log.e("PatientRepositoryImpl", "Adding patient to queue: $patient")
//            }

            Outcome.Success(patient.id)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addPatientAndCreateVisit(patient: Patient, visit: Visit): Outcome<PatientAndVisitIds> {
        return try {
            roomDataSource.insertPatientAndCreateVisit(patient.toEntity(), visit.toEntity())
            Outcome.Success(PatientAndVisitIds(patient.id, visit.id))
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deletePatient(patient: Patient): Outcome<Unit> {
        return try {
            roomDataSource.deletePatient(patient.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun deletePatientById(patientId: String): Outcome<Unit> {
        return try {
            roomDataSource.deleteById(patientId)
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun getPatientById(patientId: String): Flow<Resource<Patient>> = flow {
        roomDataSource.getPatientById(patientId)
            .onStart { emit(Resource.Loading()) }
            .catch { exception ->
                Log.e(
                    "PatientRepository",
                    "Error getting patient data for patientId $patientId: ${exception.message}",
                    exception
                )
                emit(Resource.Error(exception.message ?: "An error occurred"))
            }
            .collect { entity ->
                val resource = when (entity) {
                    null -> Resource.NotFound()
                    else -> Resource.Success(entity.toDomain())
                }
                emit(resource)
            }
    }

    override fun doOnlineTasks(){
        println("Doing Online tasks")
    }

    override fun doOfflineTasks(){
        println("Doing Offline tasks")
    }

    override fun getPatients(): Flow<Resource<List<Patient>>> = flow {
        roomDataSource.getPatients()
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                Log.e("PatientRepositoryImpl", "Error getting patients: ${it.message}")
                emit(Resource.Error(it.localizedMessage ?: "Unknown error occurred"))
            }
            .collect { entities ->
                val patients = entities
                    .asReversed()
                    .map { entity ->
                        entity.toDomain()
                    }
                emit(Resource.Success(patients))
            }
    }

    override fun getPatientsAndVisitsOn(date: LocalDate): Flow<Resource<List<PatientWithVisitInfo>>>  = flow {
        roomDataSource.getPatientsAndVisitsOn(date)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                Log.e("PatientRepositoryImpl", "Error getting patients and their visits info: ${it.message}")
                emit(Resource.Error(it.localizedMessage ?: "Unknown error occurred"))
            }
            .collect { entities ->
                val patientsWithVisitInfo = entities.map { entity -> entity.toDomain() }
                emit(Resource.Success(patientsWithVisitInfo))
            }
    }

//    override suspend fun updateTriageState(patient: Patient, triageState: String): Resource<Unit> {
//        return try {
//            roomDataSource.updateTriageState(patient.toEntity(), triageState)
//            Resource.Success(Unit)
//        } catch (e: Exception) {
//            Resource.Error(e.message ?: "An error occurred")
//        }
//    }
}