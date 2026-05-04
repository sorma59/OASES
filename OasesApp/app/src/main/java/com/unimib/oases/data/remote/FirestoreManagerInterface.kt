package com.unimib.oases.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.unimib.oases.data.local.model.DispositionEntity
import com.unimib.oases.data.local.model.EvaluationEntity
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.data.local.model.PatientDiseaseEntity
import com.unimib.oases.data.local.model.ReassessmentEntity
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.data.local.model.VisitVitalSignEntity

interface FirestoreManagerInterface {
    fun startListener()
    fun isOnline(): Boolean
    fun getInstance(): FirebaseFirestore
    suspend fun deletePatient(patientId: String): Boolean
    suspend fun storeVisit(patientId: String): Boolean

    suspend fun getHistoryPatients(): List<PatientEntity>
    fun addPatient(patient: PatientEntity, visit: VisitEntity): Boolean

    fun insertMalnutritionScreening(malnutritionScreening: MalnutritionScreeningEntity): Boolean
    fun insertTriageEvaluation(patientId: String, triageEvaluation: TriageEvaluationEntity): Boolean
    fun insertVitalSigns(patientId: String, vitalSign: List<VisitVitalSignEntity>): Boolean
    fun observeCurrentPatients()
    suspend fun insertDisposition(dispositionEntity: DispositionEntity): Boolean
    suspend fun insertEvaluation(evaluationEntity: EvaluationEntity): Boolean
    suspend fun insertReassessment(reassessmentEntity: ReassessmentEntity): Boolean
    suspend fun updateStatusAndCloseVisit(visitId: String, status: String): Boolean
    suspend fun insertPatientDiseases(diseases: List<PatientDiseaseEntity>): Boolean
}
