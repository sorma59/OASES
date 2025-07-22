package com.unimib.oases.di

import com.unimib.oases.data.repository.DiseaseRepositoryImpl
import com.unimib.oases.data.repository.PatientDiseaseRepositoryImpl
import com.unimib.oases.data.repository.PatientRepositoryImpl
import com.unimib.oases.data.repository.TriageEvaluationRepositoryImpl
import com.unimib.oases.data.repository.UserRepositoryImpl
import com.unimib.oases.data.repository.VisitRepositoryImpl
import com.unimib.oases.data.repository.VisitVitalSignRepositoryImpl
import com.unimib.oases.data.repository.VitalSignRepositoryImpl
import com.unimib.oases.domain.repository.DiseaseRepository
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.domain.repository.VitalSignRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Choose the appropriate Hilt component
abstract class RepositoryModule {

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindDiseaseRepository(
        diseaseRepositoryImpl: DiseaseRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): DiseaseRepository // Return the interface

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindPatientDiseaseRepository(
        patientDiseaseRepositoryImpl: PatientDiseaseRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): PatientDiseaseRepository // Return the interface

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindPatientRepository(
        patientRepositoryImpl: PatientRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): PatientRepository // Return the interface

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): UserRepository // Return the interface

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindTriageEvaluationRepository(
        triageEvaluationRepositoryImpl: TriageEvaluationRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): TriageEvaluationRepository // Return the interface

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindVisitRepository(
        visitRepositoryImpl: VisitRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): VisitRepository // Return the interface

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindVisitVitalSignRepository(
        visitRepositoryImpl: VisitVitalSignRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): VisitVitalSignRepository // Return the interface

    @Binds
    @Singleton // The scope here must match or be wider than the scope on the repository
    abstract fun bindVitalSignRepository(
        vitalSignRepositoryImpl: VitalSignRepositoryImpl // Hilt knows how to create this due to @Inject constructor
    ): VitalSignRepository // Return the interface
}