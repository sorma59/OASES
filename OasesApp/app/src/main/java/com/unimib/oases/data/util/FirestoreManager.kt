package com.unimib.oases.data.util

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.util.DateAndTimeUtils
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
                        //syncPatientsToFirestore()
                    }
                }
            }

        observeCurrentPatients()

        println("CLOUD LISTENER SUCCESSFULLY STARTED")
    }

    private fun observeCurrentPatients() {
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
                            if (patient != null && visit != null) Pair(patient, visit) else null
                        } catch (e: Exception) {
                            null
                        }

                        when (dc.type) {
                            com.google.firebase.firestore.DocumentChange.Type.ADDED,
                            com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                patientAndVisit?.let { (patient, visit) ->
                                    roomDataSource.insertPatientAndCreateVisit(patient, visit)
                                    Log.d("FirestoreManager", "Synced/Updated patient: ${patient.name}")
                                }
                            }
                            com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                patientAndVisit?.let { (patient, _) ->
                                    // You need to implement a delete method in RoomDataSource
                                    roomDataSource.deletePatient(patient)
                                    Log.d("FirestoreManager", "Deleted patient from local: ${patient.id}")
                                }
                            }

                        }
                    }

                    syncPatientsToFirestore()
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
                    Log.d("FirestoreManager", "Patient ${patient.name} update queued for Firestore.")
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
        // We remove 'suspend' and 'await' to let Firebase handle the queuing internally
        return try {
            // This line updates the local cache IMMEDIATELY and queues the
            // network request for when the connection returns.
            db.collection("currentPatients").document(patientId).delete()
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Patient $patientId deletion synced with server.")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreManager", "Permanent failure deleting patient: $e")
                }

            true // Returns true because the 'intent' to delete was successfully registered
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error queuing deletion: $e")
            false
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
                "visitEntity" to visit
            )

            // We use .set() on a specific document ID (the patient's ID)
            // to prevent duplicates and allow offline queuing.
            db.collection("currentPatients")
                .document(patient.id)
                .set(patientData)
                .addOnSuccessListener {
                    Log.d("FirestoreManager", "Patient ${patient.name} successfully queued for Firestore.")
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

    private fun syncPatientsToFirestore() {
        scope.launch {
            try {
                val localPatients =
                    roomDataSource.getActivePatientsAndVisitsOn(DateAndTimeUtils.getCurrentDate()).first()
                if (localPatients.isEmpty()) return@launch
                println("SYNC: Found ${localPatients.size} current patients.")
                println("SYNC: Starting upload of current patients...")
                val batch = db.batch()
                localPatients.forEach { patient ->
                    val docRef = db.collection("currentPatients").document(patient.patientEntity.id)
                    batch.set(docRef, patient)
                }
                batch.commit()
                    .addOnSuccessListener { println("SYNC: Successfully uploaded ${localPatients.size} current patients.") }
                    .addOnFailureListener { e -> println("SYNC: Failed to upload current patients: $e") }

            } catch (e: Exception) {
                println("SYNC: Error during synchronization: ${e.message}")
            } finally {
                 importPatientsFromFirestore()
            }
        }
    }

    private fun importPatientsFromFirestore() {
        scope.launch {
            try {
                db.collection("currentPatients")
                    .get()
                    .addOnSuccessListener { result ->
                        scope.launch {
                            val patientsToImport = result.mapNotNull { document ->
                                try {
                                    val patientData = document.get("patientEntity") as? Map<String, Any>
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

                                    val visitData = document.get("visitEntity") as? Map<String, Any>
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

                                    if (patient != null && visit != null) Pair(patient, visit) else null
                                } catch (e: Exception) {
                                    null
                                }
                            }

                            if (patientsToImport.isNotEmpty()) {
                                patientsToImport.forEach { (patient, visit) ->
                                    roomDataSource.insertPatientAndCreateVisit(patient, visit)
                                }
                                println("IMPORT: Successfully imported ${patientsToImport.size} patients.")
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        println("IMPORT: Error fetching patients: $e")
                    }
            } catch (e: Exception) {
                println("IMPORT: unexpected error: ${e.message}")
            }
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
}
