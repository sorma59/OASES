package com.unimib.oases.data.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.unimib.oases.domain.repository.PatientRepository
import javax.inject.Inject


class FirestoreManager @Inject constructor(
    private val patientRepository: PatientRepository,
) {
    private val db = FirebaseFirestore.getInstance()



    fun startListener() {
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
                        patientRepository.doOfflineTasks()
                    } else {
                        println("FIRESTORE SERVER ONLINE")
                        patientRepository.doOnlineTasks()

                    }
                }
            }
        println("CLOUD LISTENER SUCCESSFULLY STARTED")
    }

}