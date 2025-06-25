package com.unimib.oases.data.util


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import javax.inject.Inject


class FirestoreManager @Inject constructor(
): FirestoreManagerInterface {
    private val db = FirebaseFirestore.getInstance()

    companion object {
        private val _onlineStatus = MutableStateFlow(false)

    }

    val onlineStatus: StateFlow<Boolean> = _onlineStatus.asStateFlow()



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

                       // add the pending data to the firestore database
                    }
                }
            }
        println("CLOUD LISTENER SUCCESSFULLY STARTED")
    }


    override fun isOnline(): Boolean{
        return _onlineStatus.value
    }


    override fun getInstance(): FirebaseFirestore {
        return db
    }



}