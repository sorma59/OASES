package com.unimib.oases.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unimib.oases.data.local.dao.DiseaseDao
import com.unimib.oases.data.local.dao.PatientDao
import com.unimib.oases.data.local.dao.PatientDiseaseDao
import com.unimib.oases.data.local.dao.UserDao
import com.unimib.oases.data.local.dao.VisitDao
import com.unimib.oases.data.model.DiseaseEntity
import com.unimib.oases.data.model.PatientDiseaseEntity
import com.unimib.oases.data.model.PatientEntity
import com.unimib.oases.data.model.User
import com.unimib.oases.data.model.VisitEntity

@Database(
    entities = [
        PatientEntity::class,
        User::class,
        VisitEntity::class,
        DiseaseEntity::class,
        PatientDiseaseEntity::class
    ],
    version = 3
)
@TypeConverters(Converters::class)
abstract class OasesDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun userDao(): UserDao
    abstract fun patientDiseaseDao(): PatientDiseaseDao
    abstract fun diseaseDao(): DiseaseDao
    abstract fun visitDao(): VisitDao
    // ... other DAOs
}
