package com.unimib.oases.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unimib.oases.data.local.dao.PatientDao
import com.unimib.oases.data.local.dao.UserDao
import com.unimib.oases.data.model.Patient
import com.unimib.oases.data.model.User

@Database(
    entities = [Patient::class, User::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class OasesDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun userDao(): UserDao
    // ... other DAOs
}
