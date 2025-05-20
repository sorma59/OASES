package com.unimib.oases.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.local.OasesDatabase
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.repository.DiseaseRepositoryImpl
import com.unimib.oases.data.repository.PatientRepositoryImpl
import com.unimib.oases.data.repository.UserRepositoryImpl
import com.unimib.oases.domain.repository.DiseaseRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.UserRepository
import com.unimib.oases.domain.usecase.InsertPatientLocallyUseCase
import com.unimib.oases.domain.usecase.SendPatientViaBluetoothUseCase
import com.unimib.oases.ui.screen.patient_registration.RegistrationScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    // Define migrations
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Migration1-2: image field added for Patient entity
            db.execSQL("ALTER TABLE patients ADD COLUMN image BLOB DEFAULT NULL")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Migration1-2: image field added for Patient entity
            db.execSQL("ALTER TABLE patients ADD COLUMN status TEXT DEFAULT NULL")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): OasesDatabase {
        return Room.databaseBuilder(
            context,
            OasesDatabase::class.java,
            "oases_database"
        )
            .addMigrations(MIGRATION_1_2) // Add migrations here
            .addMigrations(MIGRATION_2_3)
            .createFromAsset("databases/users.db")
            .build()
    }

    // Provide RoomDataSource, which will now have the AppDatabase as a parameter
    @Provides
    @Singleton
    fun provideRoomDataSource(appDatabase: OasesDatabase): RoomDataSource {
        return RoomDataSource(appDatabase)
    }

    @Provides
    @Singleton
    fun providePatientRepository(
        @ApplicationScope applicationScope: CoroutineScope,
        roomDataSource: RoomDataSource,
        bluetoothManager: BluetoothCustomManager,
    ): PatientRepository {
        return PatientRepositoryImpl(roomDataSource, bluetoothManager, applicationScope)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        roomDataSource: RoomDataSource
    ): UserRepository {
        return UserRepositoryImpl(roomDataSource)
    }

    @Provides
    @Singleton
    fun provideDiseaseRepository(
        roomDataSource: RoomDataSource
    ): DiseaseRepository {
        return DiseaseRepositoryImpl(roomDataSource)
    }

    @Provides
    fun provideSendPatientUseCase(
        bluetoothManager: BluetoothCustomManager,
    ): SendPatientViaBluetoothUseCase {
        return SendPatientViaBluetoothUseCase(bluetoothManager)
    }

    @Provides
    fun provideInsertPatientLocallyUseCase(
        patientRepository: PatientRepository
    ): InsertPatientLocallyUseCase {
        return InsertPatientLocallyUseCase(patientRepository)
    }

    @Provides
    fun provideRegistrationScreenViewModel(
        patientRepository: PatientRepository,
        @ApplicationScope applicationScope: CoroutineScope
    ): RegistrationScreenViewModel {
        return RegistrationScreenViewModel(patientRepository, applicationScope)
    }
}