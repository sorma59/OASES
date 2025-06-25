package com.unimib.oases.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.unimib.oases.data.remote.FirebaseFirestoreSource
import com.unimib.oases.data.remote.FirebaseFirestoreSourceImpl
import com.unimib.oases.data.util.FirestoreManager
import com.unimib.oases.data.util.FirestoreManagerInterface
import com.unimib.oases.domain.repository.PatientRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestoreManager(): FirestoreManagerInterface {
        return FirestoreManager()
    }


    @Provides
    @Singleton
    fun provideFirestoreSource(firestoreManager: FirestoreManagerInterface): FirebaseFirestoreSource {
        return FirebaseFirestoreSourceImpl(firestoreManager)
    }



}