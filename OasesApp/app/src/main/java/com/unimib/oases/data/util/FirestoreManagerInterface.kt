package com.unimib.oases.data.util

import com.google.firebase.firestore.FirebaseFirestore
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.VisitEntity

interface FirestoreManagerInterface {
    fun startListener()
    fun isOnline(): Boolean
    fun getInstance(): FirebaseFirestore
    fun deletePatient(patientId: String): Boolean
    suspend fun getHistoryPatients(): List<PatientEntity>
    fun addPatient(patient: PatientEntity, visit: VisitEntity): Boolean
}
