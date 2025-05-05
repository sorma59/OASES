package com.unimib.oases.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unimib.oases.data.local.OasesDatabase
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.repository.PatientRepositoryImpl
import com.unimib.oases.data.repository.UserRepositoryImpl
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Define migrations
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Migration1-2: image field added for Patient entity
            db.execSQL("ALTER TABLE patients ADD COLUMN image BLOB DEFAULT NULL")
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
    fun providePatientRepository(roomDataSource: RoomDataSource): PatientRepository {
        return PatientRepositoryImpl(roomDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext context: Context): UserRepository {
        return UserRepositoryImpl(context)
    }
}