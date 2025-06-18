package com.unimib.oases.data.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class FirestoreManager @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    private val _onlineStatus = MutableStateFlow(false)
    val onlineStatus: StateFlow<Boolean> = _onlineStatus.asStateFlow()


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
                        _onlineStatus.value = false
                    } else {
                        println("FIRESTORE SERVER ONLINE")
                        _onlineStatus.value = true
                    }
                }

            }
        println("CLOUD LISTENER SUCCESSFULLY STARTED")
    }

}