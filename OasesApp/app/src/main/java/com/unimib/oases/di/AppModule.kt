package com.unimib.oases.di

import android.content.Context
import androidx.room.Room
import com.unimib.oases.data.local.db.AuthDatabase
import com.unimib.oases.data.local.db.OasesDatabase
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

    // ----------------Application Scope----------------
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    // ----------------Database--------------------

//    private val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            // Migration1-2: image field added for Patient entity
//            db.execSQL("ALTER TABLE patients ADD COLUMN image BLOB DEFAULT NULL")
//        }
//    }
//
//    private val MIGRATION_2_3 = object : Migration(2, 3) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            // Migration2-3: status field added for Patient entity
//            db.execSQL("ALTER TABLE patients ADD COLUMN status TEXT NOT NULL DEFAULT ''")
//            db.execSQL("""
//                CREATE TABLE visits (
//                    id TEXT NOT NULL DEFAULT '',
//                    date TEXT NOT NULL DEFAULT '',
//                    description TEXT NOT NULL DEFAULT '',
//                    patient_id TEXT NOT NULL DEFAULT '',
//                    triage_code TEXT NOT NULL DEFAULT '',
//                    PRIMARY KEY(id),
//                    FOREIGN KEY(patient_id) REFERENCES patients(id) ON UPDATE NO ACTION ON DELETE CASCADE
//                )
//            """.trimIndent())
//            db.execSQL("""
//                CREATE TABLE diseases (
//                    name TEXT NOT NULL DEFAULT '',
//                    PRIMARY KEY (name)
//                )
//            """.trimIndent())
//            db.execSQL("""
//                CREATE TABLE patient_diseases (
//                    patient_id TEXT NOT NULL DEFAULT '',
//                    disease_name TEXT NOT NULL DEFAULT '',
//                    diagnosis_date TEXT NOT NULL DEFAULT '',
//                    additional_info TEXT NOT NULL DEFAULT '',
//                    PRIMARY KEY(patient_id, disease_name),
//                    FOREIGN KEY(patient_id) REFERENCES patients(id) ON UPDATE NO ACTION ON DELETE CASCADE
//                    FOREIGN KEY(disease_name) REFERENCES diseases(name) ON UPDATE NO ACTION ON DELETE CASCADE
//                )
//            """.trimIndent())
//        }
//    }
//
//    private val MIGRATION_3_4 = object : Migration(3, 4) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            // Migration3-4:
//            db.execSQL("""
//                CREATE TABLE vital_signs (
//                    patient_id TEXT NOT NULL DEFAULT '',
//                    disease_name TEXT NOT NULL DEFAULT '',
//                    diagnosis_date TEXT NOT NULL DEFAULT '',
//                    additional_info TEXT NOT NULL DEFAULT '',
//                    PRIMARY KEY(patient_id, disease_name),
//                    FOREIGN KEY(patient_id) REFERENCES patients(id) ON UPDATE NO ACTION ON DELETE CASCADE
//                    FOREIGN KEY(disease_name) REFERENCES diseases(name) ON UPDATE NO ACTION ON DELETE CASCADE
//                )
//            """.trimIndent())
//        }
//    }

//

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
//        .fallbackToDestructiveMigration(true)
        .createFromAsset("databases/oases.db")
        .build()
    }

    @Provides
    @Singleton
    fun provideAuthDatabase(
        @ApplicationContext context: Context
    ): AuthDatabase {
        return Room.databaseBuilder(
            context,
            AuthDatabase::class.java,
            "auth_database"
        )
        .createFromAsset("databases/users.db")
        .build()
    }
}