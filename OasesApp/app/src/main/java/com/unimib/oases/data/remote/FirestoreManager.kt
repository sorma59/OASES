package com.unimib.oases.data.remote

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.DetailQuestionAnswer
import com.unimib.oases.data.local.model.DispositionEntity
import com.unimib.oases.data.local.model.EvaluationEntity
import com.unimib.oases.data.local.model.FindingSnapshot
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.data.local.model.PatientDiseaseEntity
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.ReassessmentEntity
import com.unimib.oases.data.local.model.TreeAnswers
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.VisitVitalSignEntity
import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.domain.model.Ward
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.ui.screen.medical_visit.disposition.HomeTreatment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreManager @Inject constructor(
    private val roomDataSource: RoomDataSource,
) : FirestoreManagerInterface {

    private val db = FirebaseFirestore.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        private val _onlineStatus = MutableStateFlow(false)
    }

    override fun startListener() {
        println("STARTING CLOUD LISTENER..")
        db.collection("connectionStatus").document("current")
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                if (error != null) {
                    println("Listen failed: $error")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    if (snapshot.metadata.isFromCache) {
                        println("FIRESTORE SERVER OFFLINE")
                        _onlineStatus.value = false
                    } else {
                        println("FIRESTORE SERVER ONLINE")
                        _onlineStatus.value = true

                        syncHistoryToFirestore()
                        //importPatientsFromFirestore()
                        //syncPatientsToFirestore()
                    }
                }
            }

        println("CLOUD LISTENER SUCCESSFULLY STARTED")
    }

    override fun observeCurrentPatients() {
        db.collection("currentPatients")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("FirestoreManager", "Listen failed: $error")
                    return@addSnapshotListener
                }

                scope.launch {
                    snapshots?.documentChanges?.forEach { dc ->
                        val doc = dc.document

                        // Use your existing logic to parse the document
                        val patientAndVisit = try {
                            val patientData = doc.get("patientEntity") as? Map<*, *>
                            val visitData = doc.get("visitEntity") as? Map<*, *>
                            val triageEvaluation = doc.get("triageEvaluation") as? Map<*, *>
                            val vitalsData = doc.get("vitalSigns") as? List<*>
                            val malnutritionScreeningData =
                                doc.get("malnutritionScreening") as? Map<*, *>
                            val patientDiseases = doc.get("patientDiseases") as? List<*>

                            val patient = patientData?.let {
                                PatientEntity(
                                    id = it["id"] as String,
                                    publicId = it["publicId"] as String,
                                    name = it["name"] as String,
                                    birthDate = it["birthDate"] as String,
                                    sex = it["sex"] as String,
                                    village = it["village"] as String,
                                    parish = it["parish"] as String,
                                    subCounty = it["subCounty"] as String,
                                    district = it["district"] as String,
                                    nextOfKin = it["nextOfKin"] as String,
                                    contact = it["contact"] as String,
                                    image = null
                                )
                            }
                            val visit = visitData?.let {
                                VisitEntity(
                                    id = it["id"] as String,
                                    patientId = it["patientId"] as String,
                                    triageCode = it["triageCode"] as String,
                                    patientStatus = it["patientStatus"] as String,
                                    roomName = it["roomName"] as? String,
                                    arrivalTime = it["arrivalTime"] as String,
                                    date = it["date"] as String,
                                    description = it["description"] as String
                                )
                            }

                            val triageData = triageEvaluation?.let {
                                TriageEvaluationEntity(
                                    visitId = it["visitId"] as String,
                                    redSymptomIds = it["redSymptomIds"] as List<String>,
                                    yellowSymptomIds = it["yellowSymptomIds"] as List<String>,
                                )

                            }

                            val vitalsList = mutableListOf<VisitVitalSignEntity>()

                            vitalsData?.forEach { item ->
                                val vitalSignMap = item as? Map<*, *>
                                if (vitalSignMap != null) {
                                    try {
                                        vitalsList.add(
                                            VisitVitalSignEntity(
                                                // Extract data from each map in the array
                                                visitId = vitalSignMap["visitId"] as String,
                                                vitalSignName = vitalSignMap["vitalSignName"] as String,
                                                timestamp = vitalSignMap["timestamp"] as String,
                                                value = (vitalSignMap["value"] as? Number)?.toDouble()
                                                    ?: 0.0
                                            )
                                        )
                                    } catch (e: Exception) {
                                        Log.e(
                                            "FirestoreManager",
                                            "Error parsing individual vital sign: ${e.message}"
                                        )
                                    }
                                }
                            }

                            val patientDiseasesList = patientDiseases?.map { item ->
                                PatientDiseaseEntity(
                                    patientId = (item as? Map<*, *>)?.get("patientId") as String,
                                    additionalInfo = item["additionalInfo"] as String,
                                    diseaseName = item["diseaseName"] as String,
                                    diagnosisDate = item["diagnosisDate"] as String,
                                    isDiagnosed = (item["diagnosed"] as? Boolean) ?: true,
                                    freeTextValue = item["freeTextValue"] as String,
                                )
                            } ?: emptyList()

                            val malnutritionScreening = malnutritionScreeningData?.let {
                                MalnutritionScreeningEntity(
                                    visitId = it["visitId"] as String,
                                    weightInKg = it["weightInKg"] as Double,
                                    heightInCm = it["heightInCm"] as Double,
                                    bmi = it["bmi"] as Double,
                                    muacInCm = it["muacInCm"] as Double,
                                    muacCategory = it["muacCategory"] as String,
                                )
                            }


                            if (patient != null && visit != null) {
                                PatientSyncData(
                                    patient,
                                    visit,
                                    triageData,
                                    vitalsList,
                                    malnutritionScreening,
                                    patientDiseasesList
                                )
                            } else null
                        } catch (e: Exception) {
                            Log.e("FirestoreManager", "Error parsing document: $e")
                            null
                        }

                        when (dc.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                patientAndVisit?.let { (patient, visit, triageData, vitals, malnutritionScreening, diseases) ->
                                    roomDataSource.insertPatientAndCreateVisit(patient, visit)
                                    triageData?.let {
                                        roomDataSource.insertTriageEvaluation(
                                            triageData
                                        )
                                    }
                                    vitals.let { roomDataSource.insertVisitVitalSigns(vitals) }
                                    malnutritionScreening?.let {
                                        roomDataSource.insertMalnutritionScreening(
                                            malnutritionScreening
                                        )
                                    }

                                    diseases?.let {
                                        roomDataSource.insertPatientDiseases(diseases)
                                    }
                                    Log.d(
                                        "FirestoreManager",
                                        "Synced/Updated patient: ${patient.name}"
                                    )
                                }
                            }

                            DocumentChange.Type.REMOVED -> {
                                patientAndVisit?.let { (patient, _) ->
                                    // You need to implement a delete method in RoomDataSource
                                    roomDataSource.deletePatient(patient)
                                    Log.d(
                                        "FirestoreManager",
                                        "Deleted patient from local: ${patient.id}"
                                    )
                                }
                            }

                        }
                    }
                    // syncPatientsToFirestore()
                }
            }
    }

    override fun isOnline(): Boolean {
        return _onlineStatus.value
    }

    override fun getInstance(): FirebaseFirestore {
        return db
    }

    fun updatePatient(patient: PatientEntity): Boolean {
        return try {
            // Using dot notation "patientEntity" updates only the patient object
            // inside the document, leaving "visitEntity" untouched.
            db.collection("currentPatients")
                .document(patient.id)
                .update("patientEntity", patient)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Patient ${patient.name} update queued for Firestore."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error updating patient in Firestore: $e")
                }

            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate updatePatient: ${e.message}")
            false
        }
    }

    fun updateVisit(visit: VisitEntity): Boolean {
        return try {
            db.collection("currentPatients")
                .document(visit.patientId)
                .update("visitEntity", visit)
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Visit ${visit.id} update queued for Firestore.")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error updating visit in Firestore: $e")
                }

            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate updateVisit: ${e.message}")
            false
        }
    }

    override suspend fun deletePatient(patientId: String): Boolean {
        return try {
            db.collection("currentPatients")
                .document(patientId)
                .delete()
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Patient $patientId successfully deleted.")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error deleting patient: $e")
                }
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate deletePatient: ${e.message}")
            false
        } finally {
            syncHistoryToFirestore()
        }
    }

    override suspend fun storeVisit(patientId: String): Boolean {
        return try {
            val currentDocRef = db.collection("currentPatients").document(patientId)

            // OPTIMIZATION: Use a subcollection for visits.
            // Storing all visits in a single document with SetOptions.merge()
            // becomes exponentially slower as the document size grows.
            val pastVisitsCollection = db.collection("pastPatients")
                .document(patientId)
                .collection("visits")

            // 1. Await the data fetch
            val snapshot = currentDocRef.get().await()

            if (snapshot.exists()) {
                val data = snapshot.data ?: return false
                val visitId = (data["visitEntity"] as? Map<*, *>)?.get("id") as? String
                    ?: "unknown_visit_${System.currentTimeMillis()}"

                val batch = db.batch()

                // Migration logic: Write to a new document in the subcollection.
                // This is a much faster operation than merging into a large document.
                batch.set(pastVisitsCollection.document(visitId), data)

                // 2. Delete from current collection
                batch.delete(currentDocRef)

                // 3. Commit atomically and await completion
                batch.commit().await()

                Log.d("FirestoreManager", "Migration for $patientId completed successfully.")
                true
            } else {
                Log.e("FirestoreManager", "Patient $patientId not found in currentPatients.")
                false
            }
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error in storeVisit: ${e.message}")
            false
        } finally {
            // Trigger history sync only after the migration is truly finished
            syncHistoryToFirestore()
        }
    }


    private fun syncHistoryToFirestore() {
        scope.launch {
            try {
                val cachedPatients = roomDataSource.getCachedPatients().first()
                if (cachedPatients.isEmpty()) return@launch
                println("SYNC: Found ${cachedPatients.size} cached patients.")
                println("SYNC: Starting upload of cached patients...")

                val batch = db.batch()
                cachedPatients.forEach { patient ->
                    val docRef = db.collection("cachedPatients").document(patient.id)
                    batch.set(docRef, patient)
                }

                roomDataSource.clearCachedPatients()

                batch.commit()
                    .addOnSuccessListener { println("SYNC: Successfully uploaded ${cachedPatients.size} cached patients.") }
                    .addOnFailureListener { e -> println("SYNC: Failed to upload cached patients: $e") }

            } catch (e: Exception) {
                println("SYNC: Error during synchronization: ${e.message}")
            }
        }
    }

    override fun addPatient(patient: PatientEntity, visit: VisitEntity): Boolean {
        return try {// Create the data structure matching your "currentPatients" schema
            val patientData = mapOf(
                "patientEntity" to patient,
                "visitEntity" to visit,
            )

            // We use .set() on a specific document ID (the patient's ID)
            // to prevent duplicates and allow offline queuing.
            db.collection("currentPatients")
                .document(patient.id)
                .set(patientData)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Patient ${patient.name} successfully queued for Firestore."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding patient to Firestore: $e")
                }

            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate addPatient: ${e.message}")
            false
        }
    }

    override suspend fun getHistoryPatients(): List<PatientEntity> {
        return try {
            // Target 'cachedPatients' collection and fetch data directly from root
            val snapshot = db.collection("cachedPatients").get().await()
            snapshot.mapNotNull { document ->
                try {
                    PatientEntity(
                        id = document.getString("id") ?: "",
                        publicId = document.getString("publicId") ?: "",
                        name = document.getString("name") ?: "",
                        birthDate = document.getString("birthDate") ?: "",
                        sex = document.getString("sex") ?: "",
                        village = document.getString("village") ?: "",
                        parish = document.getString("parish") ?: "",
                        subCounty = document.getString("subCounty") ?: "",
                        district = document.getString("district") ?: "",
                        nextOfKin = document.getString("nextOfKin") ?: "",
                        contact = document.getString("contact") ?: "",
                        image = null
                    )
                } catch (e: Exception) {
                    Log.e("FirestoreManager", "Error parsing history patient: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error getting history patients: ${e.message}")
            emptyList()
        }
    }

    override fun insertMalnutritionScreening(malnutritionScreening: MalnutritionScreeningEntity): Boolean {
        scope.launch {
            try {
                // Get the visit locally to find the associated patientId
                val visit = roomDataSource.getVisitById(malnutritionScreening.visitId).first()
                db.collection("currentPatients")
                    .document(visit.patientId)
                    .update("malnutritionScreening", malnutritionScreening)
                    .addOnSuccessListener {
                        Log.d("FirestoreManager", "MalnutritionScreening successfully queued.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreManager", "Error adding MalnutritionScreening: $e")
                    }
            } catch (e: Exception) {
                Log.e(
                    "FirestoreManager",
                    "Failed to initiate insertMalnutritionScreening: ${e.message}"
                )
            }
        }
        return true
    }


    override fun insertVitalSigns(
        patientId: String,
        vitalSign: List<VisitVitalSignEntity>
    ): Boolean {
        if (vitalSign.isEmpty()) return true

        return try {
            db.collection("currentPatients")
                .document(patientId)
                .update("vitalSigns", arrayUnion(*vitalSign.toTypedArray()))
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Vital signs for patient $patientId successfully queued for Firestore."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding vital signs to Firestore: $e")
                }
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate insertVitalSigns: ${e.message}")
            false
        }
    }

    override fun insertTriageEvaluation(
        patientId: String,
        triageEvaluation: TriageEvaluationEntity
    ): Boolean {
        return try {
            db.collection("currentPatients")
                .document(patientId)
                .update("triageEvaluation", triageEvaluation)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "TriageEvaluation successfully queued for patient $patientId."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding TriageEvaluation: $e")
                }
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate insertTriageEvaluation: ${e.message}")
            false
        }
    }

    override suspend fun insertDisposition(dispositionEntity: DispositionEntity): Boolean {
        return try {
            val visit = roomDataSource.getVisitById(dispositionEntity.visitId).first()
            val patientId = visit.patientId
            val gson = Gson()

            val dispositionMap = mutableMapOf<String, Any?>(
                "visitId" to dispositionEntity.visitId,
                "dispositionTypeLabel" to dispositionEntity.dispositionTypeLabel,
                "ward" to dispositionEntity.ward,
                "homeTreatments" to gson.toJson(dispositionEntity.homeTreatments), // Serialized
                "prescribedTherapiesText" to dispositionEntity.prescribedTherapiesText,
                "finalDiagnosisText" to dispositionEntity.finalDiagnosisText
            )

            db.collection("currentPatients")
                .document(patientId)
                .update("disposition", dispositionMap)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Disposition successfully queued for patient $patientId."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding TriageEvaluation: $e")
                }
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to insert disposition: ${e.message}")
            false
        }
    }

    // Add this import at the top of your file

    override suspend fun insertEvaluation(evaluationEntity: EvaluationEntity): Boolean {
        return try {
            val visit = roomDataSource.getVisitById(evaluationEntity.visitId).first()
            val patientId = visit.patientId
            val gson = Gson()

            // We convert the problematic large lists into JSON strings.
            // This solves: 1. The "Nested Array" error 2. The serialization lag.
            val evaluationMap = mutableMapOf<String, Any>(
                "visitId" to evaluationEntity.visitId,
                "complaintId" to evaluationEntity.complaintId,

                // Serialize complex/large lists to JSON strings
                "treeAnswers" to gson.toJson(evaluationEntity.treeAnswers),
                "detailQuestionAnswers" to gson.toJson(evaluationEntity.detailQuestionAnswers),
                "algorithmsQuestionsAndAnswers" to gson.toJson(evaluationEntity.algorithmsQuestionsAndAnswers),

                "symptomIds" to evaluationEntity.symptomIds,
                "suggestedTests" to gson.toJson(evaluationEntity.suggestedTests),
                "labelledTests" to gson.toJson(evaluationEntity.labelledTests),
                "additionalTests" to evaluationEntity.additionalTests,
                "immediateTreatments" to gson.toJson(evaluationEntity.immediateTreatments),
                "supportiveTherapies" to gson.toJson(evaluationEntity.supportiveTherapies)
            )

            // Use .set with merge to handle the update efficiently
            db.collection("currentPatients")
                .document(patientId)
                .update("evaluation", evaluationMap)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Evaluation for $patientId successfully queued for Firestore."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding TriageEvaluation: $e")
                }

            Log.d("FirestoreManager", "Evaluation for $patientId optimized and uploaded.")
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to insert evaluation: ${e.message}")
            false
        }
    }

    override suspend fun insertReassessment(reassessmentEntity: ReassessmentEntity): Boolean {
        return try {
            val visit = roomDataSource.getVisitById(reassessmentEntity.visitId).first()
            val patientId = visit.patientId
            val gson = Gson()

            val reassessmentMap = mutableMapOf<String, Any>(
                "visitId" to reassessmentEntity.visitId,
                "complaintId" to reassessmentEntity.complaintId,
                "symptomIds" to reassessmentEntity.symptomIds,
                "findings" to gson.toJson(reassessmentEntity.findings), // Serialized
                "definitiveTherapies" to gson.toJson(reassessmentEntity.definitiveTherapies) // Serialized
            )

            db.collection("currentPatients")
                .document(patientId)
                .update("reassessment", reassessmentMap)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Reassessment for $patientId successfully queued for Firestore."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding Reassessment: $e")
                }

            Log.d("FirestoreManager", "Reassessment for $patientId optimized and updated.")
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to insert reassessment: ${e.message}")
            false
        }
    }

    override suspend fun updateStatusAndCloseVisit(visitId: String, status: String): Boolean {
        return try {
            val patientId = roomDataSource.getVisitById(visitId).first().patientId
            roomDataSource.addToCache(patientId)
            db.collection("currentPatients")
                .document(patientId)
                .update("visitEntity.patientStatus", status)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Status for $patientId successfully queued for Firestore."
                    )
                    scope.launch {
                        storeVisit(patientId)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding status to Firestore: $e")
                }



            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error in updateStatusAndCloseVisit: ${e.message}")
            false
        }
    }

    override suspend fun insertPatientDiseases(diseases: List<PatientDiseaseEntity>): Boolean {

        if (diseases.isEmpty()) return true

        return try {
            // Assume all diseases in the list belong to the same patient
            val patientId = diseases.first().patientId

            // We use update so we don't overwrite the patient doc, just the diseases field
            db.collection("currentPatients")
                .document(patientId)
                .update("patientDiseases", diseases)
                .addOnSuccessListener {
                    Log.d(
                        "FirestoreManager",
                        "Diseases for $patientId successfully queued for Firestore."
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error adding diseases to Firestore: $e")
                }

            Log.d("FirestoreManager", "Diseases for $patientId successfully updated in Firestore.")
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error updating patient diseases: ${e.message}")
            false
        }
    }

    override suspend fun getPatientVisits(patientId: String): List<VisitEntity> {
        return try {
            val gson = Gson()
            val snapshot = db.collection("pastPatients")
                .document(patientId)
                .collection("visits")
                .get()
                .await()

            val visits = mutableListOf<VisitEntity>()

            snapshot.documents.forEach { document ->
                try {
                    val data = document.data ?: return@forEach

                    // ── Visit ────────────────────────────────────────────────
                    val visitData = data["visitEntity"] as? Map<*, *> ?: return@forEach
                    val visit = VisitEntity(
                        id = visitData["id"] as String,
                        patientId = visitData["patientId"] as String,
                        triageCode = visitData["triageCode"] as String,
                        patientStatus = visitData["patientStatus"] as String,
                        roomName = visitData["roomName"] as? String,
                        arrivalTime = visitData["arrivalTime"] as String,
                        date = visitData["date"] as String,
                        description = visitData["description"] as String
                    )
                    visits.add(visit)
                    roomDataSource.insertVisit(visit)

                    // ── Triage ───────────────────────────────────────────────
                    val triageData = data["triageEvaluation"] as? Map<*, *>
                    triageData?.let {
                        val triage = TriageEvaluationEntity(
                            visitId = it["visitId"] as String,
                            redSymptomIds = it["redSymptomIds"] as List<String>,
                            yellowSymptomIds = it["yellowSymptomIds"] as List<String>,
                        )
                        roomDataSource.insertTriageEvaluation(triage)
                    }

                    // ── Vitals ───────────────────────────────────────────────
                    val vitalsData = data["vitalSigns"] as? List<*>
                    vitalsData?.mapNotNull { item ->
                        val map = item as? Map<*, *> ?: return@mapNotNull null
                        VisitVitalSignEntity(
                            visitId = map["visitId"] as String,
                            vitalSignName = map["vitalSignName"] as String,
                            timestamp = map["timestamp"] as String,
                            value = (map["value"] as? Number)?.toDouble() ?: 0.0
                        )
                    }?.let { roomDataSource.insertVisitVitalSigns(it) }

                    // ── Malnutrition ─────────────────────────────────────────
                    val malnutritionData = data["malnutritionScreening"] as? Map<*, *>
                    malnutritionData?.let {
                        val malnutrition = MalnutritionScreeningEntity(
                            visitId = it["visitId"] as String,
                            weightInKg = it["weightInKg"] as Double,
                            heightInCm = it["heightInCm"] as Double,
                            bmi = it["bmi"] as Double,
                            muacInCm = it["muacInCm"] as Double,
                            muacCategory = it["muacCategory"] as String,
                        )
                        roomDataSource.insertMalnutritionScreening(malnutrition)
                    }

                    // ── Diseases ─────────────────────────────────────────────
                    val diseasesData = data["patientDiseases"] as? List<*>
                    diseasesData?.mapNotNull { item ->
                        val map = item as? Map<*, *> ?: return@mapNotNull null
                        PatientDiseaseEntity(
                            patientId = map["patientId"] as String,
                            diseaseName = map["diseaseName"] as String,
                            additionalInfo = map["additionalInfo"] as String,
                            diagnosisDate = map["diagnosisDate"] as String,
                            isDiagnosed = (map["diagnosed"] as? Boolean) ?: true,
                            freeTextValue = map["freeTextValue"] as String,
                        )
                    }?.let { roomDataSource.insertPatientDiseases(it) }

                    // ── Evaluation (Gson-serialized fields) ──────────────────
                    val evaluationData = data["evaluation"] as? Map<*, *>
                    evaluationData?.let {
                        val evaluation = EvaluationEntity(
                            visitId = it["visitId"] as String,
                            complaintId = it["complaintId"] as String,
                            treeAnswers = gson.fromJson(
                                it["treeAnswers"] as String,
                                object : TypeToken<List<TreeAnswers>>() {}.type
                            ),
                            detailQuestionAnswers = gson.fromJson(
                                it["detailQuestionAnswers"] as String,
                                object : TypeToken<List<DetailQuestionAnswer>>() {}.type
                            ),
                            algorithmsQuestionsAndAnswers = gson.fromJson(
                                it["algorithmsQuestionsAndAnswers"] as String,
                                object : TypeToken<List<List<QuestionAndAnswer>>>() {}.type
                            ),
                            symptomIds = it["symptomIds"] as List<String>,
                            suggestedTests = gson.fromJson(
                                it["suggestedTests"] as String,
                                object : TypeToken<List<LabelledTest>>() {}.type
                            ),
                            labelledTests = gson.fromJson(
                                it["labelledTests"] as String,
                                object : TypeToken<List<LabelledTest>>() {}.type
                            ),
                            additionalTests = it["additionalTests"] as String,
                            immediateTreatments = gson.fromJson(
                                it["immediateTreatments"] as String,
                                object : TypeToken<List<ImmediateTreatment>>() {}.type
                            ),
                            supportiveTherapies = gson.fromJson(
                                it["supportiveTherapies"] as String,
                                object : TypeToken<List<TherapyText>>() {}.type
                            ),
                        )
                        roomDataSource.insertEvaluation(evaluation)
                    }

                    // ── Reassessment (Gson-serialized fields) ────────────────
                    val reassessmentData = data["reassessment"] as? Map<*, *>
                    reassessmentData?.let {
                        val reassessment = ReassessmentEntity(
                            visitId = it["visitId"] as String,
                            complaintId = it["complaintId"] as String,
                            symptomIds = it["symptomIds"] as List<String>,
                            findings = gson.fromJson(
                                it["findings"] as String,
                                object : TypeToken<List<FindingSnapshot>>() {}.type
                            ),
                            definitiveTherapies = gson.fromJson(
                                it["definitiveTherapies"] as String,
                                object : TypeToken<List<TherapyText>>() {}.type
                            ),
                        )
                        roomDataSource.insertReassessment(reassessment)
                    }

                    // ── Disposition (Gson-serialized fields) ─────────────────
                    val dispositionData = data["disposition"] as? Map<*, *>
                    dispositionData?.let {
                        val disposition = DispositionEntity(
                            visitId = it["visitId"] as String,
                            dispositionTypeLabel = it["dispositionTypeLabel"] as String,
                            ward = gson.fromJson(
                                it["ward"] as? String,
                                Ward::class.java
                            ),
                            homeTreatments = gson.fromJson(
                                it["homeTreatments"] as String,
                                object : TypeToken<List<HomeTreatment>>() {}.type
                            ),
                            prescribedTherapiesText = it["prescribedTherapiesText"] as String,
                            finalDiagnosisText = it["finalDiagnosisText"] as String,
                        )
                        roomDataSource.insertDisposition(disposition)
                    }

                } catch (e: Exception) {
                    Log.e("FirestoreManager", "Error parsing visit document ${document.id}: ${e.message}")
                }
            }

            visits
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error fetching visits for patient $patientId: ${e.message}")
            emptyList()
        }
    }
}

data class PatientSyncData(
    val patient: PatientEntity,
    val visit: VisitEntity,
    val triage: TriageEvaluationEntity?,
    val vitals: List<VisitVitalSignEntity>, // Example 4th entity
    val malnutritionScreening: MalnutritionScreeningEntity?,
    val patientDiseases: List<PatientDiseaseEntity>?
)