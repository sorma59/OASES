package com.unimib.oases.data.util

import com.google.firebase.firestore.FirebaseFirestore
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.data.local.model.VisitVitalSignEntity

interface FirestoreManagerInterface {
    fun startListener()
    fun isOnline(): Boolean
    fun getInstance(): FirebaseFirestore
    fun deletePatient(patientId: String): Boolean
    suspend fun getHistoryPatients(): List<PatientEntity>
    fun addPatient(patient: PatientEntity, visit: VisitEntity): Boolean

    fun insertMalnutritionScreening(malnutritionScreening: MalnutritionScreeningEntity): Boolean
    fun insertTriageEvaluation(patientId: String, triageEvaluation: TriageEvaluationEntity): Boolean
    fun insertVitalSigns(patientId: String, vitalSign: List<VisitVitalSignEntity>): Boolean
    fun observeCurrentPatients()
}
