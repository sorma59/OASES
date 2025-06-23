package com.unimib.oases.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.unimib.oases.data.remote.dto.PatientDto

import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreSourceImpl @Inject constructor(val firestore: FirebaseFirestore) :
    FirebaseFirestoreSource {

    companion object {
        private const val PATIENTS_COLLECTION = "patients"
    }


    override suspend fun addPatient(patient: PatientDto): Resource<Unit> {
        return try {
            firestore.collection(PATIENTS_COLLECTION)
                .document(patient.id)
                .set(patient)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error occurred")
        }
    }

    override suspend fun getPatients(): Flow<Resource<List<PatientDto>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePatient(patient: PatientDto): Resource<Unit> {
        TODO("Not yet implemented")
    }

}