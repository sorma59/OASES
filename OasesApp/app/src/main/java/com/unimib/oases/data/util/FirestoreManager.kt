package com.unimib.oases.data.util

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.DispositionEntity
import com.unimib.oases.data.local.model.EvaluationEntity
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.ReassessmentEntity
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.VisitVitalSignEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.get

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
                                    malnutritionScreening
                                )
                            } else null
                        } catch (e: Exception) {
                            null
                        }

                        when (dc.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                patientAndVisit?.let { (patient, visit, triageData, vitals, malnutritionScreening) ->
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

    override fun deletePatient(patientId: String): Boolean {
        return try {
            // 1. Reference to the document we are moving
            val currentDocRef = db.collection("currentPatients").document(patientId)
            val pastDocRef = db.collection("pastPatients").document(patientId)

            // 2. Fetch the data first to move it
            // Note: Since we want to handle this gracefully offline, we use a Get call
            currentDocRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val data = snapshot.data
                    val visitId = (data?.get("visitEntity") as? Map<*, *>)?.get("id") as? String
                        ?: "unknown_visit"

                    val batch = db.batch()

                    // 3. Set the data in pastPatients using the visitId as the field key
                    // Using merge() ensures we don't overwrite other visits already stored for this patient
                    batch.set(
                        pastDocRef,
                        mapOf(visitId to data),
                        com.google.firebase.firestore.SetOptions.merge()
                    )

                    // 4. Delete from currentPatients
                    batch.delete(currentDocRef)

                    // 5. Execute
                    batch.commit()
                        .addOnSuccessListener {
                            Log.d(
                                "FirestoreManager",
                                "Patient $patientId moved to pastPatients successfully."
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirestoreManager", "Failed to move patient: $e")
                        }
                }
            }.addOnFailureListener { e ->
                Log.e("FirestoreManager", "Could not find patient to delete: $e")
            }

            true // Return true to indicate the process started
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error in delete/move process: ${e.message}")
            false
        } finally {
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

//    private fun syncPatientsToFirestore() {
//        scope.launch {
//            try {
//                val localPatients =
//                    roomDataSource.getActivePatientsAndVisitsOn(DateAndTimeUtils.getCurrentDate()).first()
//                if (localPatients.isEmpty()) return@launch
//                println("SYNC: Found ${localPatients.size} current patients.")
//                println("SYNC: Starting upload of current patients...")
//                val batch = db.batch()
//                localPatients.forEach { patient ->
//                    val triageEvaluation = roomDataSource.getTriageEvaluation(patient.visitEntity.id).first()
//                    // 2. Build the data map including the new field
//                    val patientData = mutableMapOf(
//                        "patientEntity" to patient.patientEntity,
//                        "visitEntity" to patient.visitEntity,
//                        "triageEvaluation" to triageEvaluation // Added this line
//                    )
//                    val docRef = db.collection("currentPatients").document(patient.patientEntity.id)
//                    batch.set(docRef, patientData)
//                }
//                batch.commit()
//                    .addOnSuccessListener { println("SYNC: Successfully uploaded ${localPatients.size} current patients.") }
//                    .addOnFailureListener { e -> println("SYNC: Failed to upload current patients: $e") }
//
//            } catch (e: Exception) {
//                println("SYNC: Error during synchronization: ${e.message}")
//            } finally {
//                 //importPatientsFromFirestore()
//            }
//        }
//    }

//    private fun importPatientsFromFirestore() {
//        scope.launch {
//            try {
//                db.collection("currentPatients")
//                    .get()
//                    .addOnSuccessListener { result ->
//                        scope.launch {
//                            val patientsToImport = result.mapNotNull { document ->
//                                try {
//                                    val patientData = document.get("patientEntity") as? Map<String, Any>
//                                    val patient = patientData?.let {
//                                        PatientEntity(
//                                            id = it["id"] as String,
//                                            publicId = it["publicId"] as String,
//                                            name = it["name"] as String,
//                                            birthDate = it["birthDate"] as String,
//                                            sex = it["sex"] as String,
//                                            village = it["village"] as String,
//                                            parish = it["parish"] as String,
//                                            subCounty = it["subCounty"] as String,
//                                            district = it["district"] as String,
//                                            nextOfKin = it["nextOfKin"] as String,
//                                            contact = it["contact"] as String,
//                                            image = null
//                                        )
//                                    }
//
//                                    val visitData = document.get("visitEntity") as? Map<String, Any>
//                                    val visit = visitData?.let {
//                                        VisitEntity(
//                                            id = it["id"] as String,
//                                            patientId = it["patientId"] as String,
//                                            triageCode = it["triageCode"] as String,
//                                            patientStatus = it["patientStatus"] as String,
//                                            roomName = it["roomName"] as? String,
//                                            arrivalTime = it["arrivalTime"] as String,
//                                            date = it["date"] as String,
//                                            description = it["description"] as String
//                                        )
//                                    }
//
//                                    val triageEvaluation = document.get("triageEvaluation") as? Map<String, Any>
//                                    val triageData = triageEvaluation?.let {
//                                        TriageEvaluationEntity(
//                                            visitId = it["visitId"] as String,
//                                            redSymptomIds = it["redSymptomIds"] as List<String>,
//                                            yellowSymptomIds = it["yellowSymptomIds"] as List<String>,
//                                        )
//
//                                    }
//
////                                    val vitalsData = document.get("vitalSigns") as? Map<String, Any>
////                                    val vitals = vitalsData?.let {
////                                            it -> VisitVitalSignEntity(
////                                        visitId = it["visitId"] as String,
////                                        timestamp = it["timestamp"] as String,
////                                        vitalSignName = it["vitalSignName"] as String,
////                                        value = it["value"] as Double,
////                                    )
////                                    }
//
//
//
//                                    if (patient != null && visit != null && triageData != null) PatientSyncData(patient, visit, triageData) else null
//                                } catch (e: Exception) {
//                                    println("IMPORT: Error parsing patient: $e")
//                                    null
//                                }
//                            }
//
//                            if (patientsToImport.isNotEmpty()) {
//                                patientsToImport.forEach { (patient, visit, triageData) ->
//                                    roomDataSource.insertPatientAndCreateVisit(patient, visit)
//                                    triageData?.let {roomDataSource.insertTriageEvaluation(triageData)}
//                                    //vitals?.let {roomDataSource.insertVisitVitalSign(vitals)}
//                                }
//                                println("IMPORT: Successfully imported ${patientsToImport.size} patients.")
//                            }
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        println("IMPORT: Error fetching patients: $e")
//                    }
//            } catch (e: Exception) {
//                println("IMPORT: unexpected error: ${e.message}")
//            }
//        }
//    }

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
            val patientId = roomDataSource.getVisitById(dispositionEntity.visitId).first().patientId
            db.collection("currentPatients")
                .document(patientId)
                .update("disposition", dispositionEntity.dispositionTypeLabel)
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Disposition for $patientId updated.")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error updating disposition: $e")
                }
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate insertDisposition: ${e.message}")
            false
        }
    }

    override suspend fun insertEvaluation(evaluationEntity: EvaluationEntity): Boolean {
        return try {
            val patientId = roomDataSource.getVisitById(evaluationEntity.visitId).first().patientId

            // Manually map the entity to a Map.
            // Firestore handles standard Lists, but we convert complex objects to Maps.
            val evaluationMap = mapOf(
                "visitId" to evaluationEntity.visitId,
                "complaintId" to evaluationEntity.complaintId,

                // Maps lists of complex objects to lists of maps
                //"treeAnswers" to evaluationEntity.treeAnswers,
                //"detailQuestionAnswers" to evaluationEntity.detailQuestionAnswers,

                // Firestore allows lists, but List<List<...>> can be tricky.
                // Explicitly passing it as the property name:
                //"algorithmsQuestionsAndAnswers" to evaluationEntity.algorithmsQuestionsAndAnswers,

                // "symptomIds" to evaluationEntity.symptomIds,

                // "suggestedTests" to evaluationEntity.suggestedTests,
                //  "labelledTests" to evaluationEntity.labelledTests,
                //  "additionalTests" to evaluationEntity.additionalTests,

                // "immediateTreatments" to evaluationEntity.immediateTreatments,
                //  "supportiveTherapies" to evaluationEntity.supportiveTherapies
            )

            db.collection("currentPatients")
                .document(patientId)
                .update("evaluation", evaluationMap)
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Evaluation for $patientId successfully updated.")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error updating evaluation: $e")
                }
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate insertEvaluation: ${e.message}")
            false
        }
    }

    override suspend fun insertReassessment(reassessmentEntity: ReassessmentEntity): Boolean {
        return try {
            val patientId = roomDataSource.getVisitById(reassessmentEntity.visitId).first().patientId
            db.collection("currentPatients")
                .document(patientId)
                .update("reassessment", reassessmentEntity.findings)
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Reassessment for $patientId updated.")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Error updating reassessment: $e")
                }
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to initiate insertReassessment: ${e.message}")
            false
        }
    }

    override suspend fun updateStatusAndCloseVisit(visitId: String, status: String): Boolean {
        return try {
            val patientId = roomDataSource.getVisitById(visitId).first().patientId
            // 1. Update the patientStatus inside the nested visitEntity object
            // We use dot notation "visitEntity.patientStatus" to target only that field
            db.collection("currentPatients")
                .document(patientId)
                .update("visitEntity.patientStatus", status)
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Status updated to $status for $patientId. Proceeding to close visit.")
                    deletePatient(patientId)
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Failed to update status before closing visit: $e")
                }

            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error in updateStatusAndCloseVisit: ${e.message}")
            false
        }
    }




}

data class PatientSyncData(
    val patient: PatientEntity,
    val visit: VisitEntity,
    val triage: TriageEvaluationEntity?,
    val vitals: List<VisitVitalSignEntity>, // Example 4th entity
    val malnutritionScreening: MalnutritionScreeningEntity?
)