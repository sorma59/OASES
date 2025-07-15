package com.unimib.oases.data.remote

import com.unimib.oases.data.remote.dto.PatientDto
import com.unimib.oases.data.util.FirestoreManagerInterface
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseFirestoreSourceImpl @Inject constructor(
    private val firestoreManager: FirestoreManagerInterface
) :
    FirebaseFirestoreSource {

    private
    companion object {
        private const val PATIENTS_COLLECTION = "patients"
    }


    override suspend fun addPatient(patient: PatientDto): Resource<Unit> {
        println("PRINTING ONLINE STATUS FROM API" + firestoreManager.isOnline())

        TODO("Not yet implemented")

    }

    override suspend fun getPatients(): Flow<Resource<List<PatientDto>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePatient(patient: PatientDto): Resource<Unit> {
        TODO("Not yet implemented")
    }

}