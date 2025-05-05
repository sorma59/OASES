package com.unimib.oases.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unimib.oases.data.model.Patient

@Database(entities = [Patient::class], version = 2)
@TypeConverters(Converters::class)
abstract class OasesDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    // ... other DAOs
}
