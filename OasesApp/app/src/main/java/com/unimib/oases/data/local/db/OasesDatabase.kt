package com.unimib.oases.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unimib.oases.data.local.Converters
import com.unimib.oases.data.local.dao.ComplaintSummaryDao
import com.unimib.oases.data.local.dao.DiseaseDao
import com.unimib.oases.data.local.dao.MalnutritionScreeningDao
import com.unimib.oases.data.local.dao.PatientDao
import com.unimib.oases.data.local.dao.PatientDiseaseDao
import com.unimib.oases.data.local.dao.RoomsDao
import com.unimib.oases.data.local.dao.TriageEvaluationDao
import com.unimib.oases.data.local.dao.VisitDao
import com.unimib.oases.data.local.dao.VisitVitalSignDao
import com.unimib.oases.data.local.dao.VitalSignsDao
import com.unimib.oases.data.local.model.ComplaintSummaryEntity
import com.unimib.oases.data.local.model.DiseaseEntity
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.data.local.model.PatientDiseaseEntity
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.RoomEntity
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.VisitVitalSignEntity
import com.unimib.oases.data.local.model.VitalSignEntity

@Database(
    entities = [
        DiseaseEntity::class,
        MalnutritionScreeningEntity::class,
        PatientEntity::class,
        PatientDiseaseEntity::class,
        TriageEvaluationEntity::class,
        VisitEntity::class,
        VisitVitalSignEntity::class,
        VitalSignEntity::class,
        RoomEntity::class,
        ComplaintSummaryEntity::class
    ],
    version = 3
)
// If modified, assets database must be modified too
@TypeConverters(Converters::class)
abstract class OasesDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun patientDiseaseDao(): PatientDiseaseDao
    abstract fun diseaseDao(): DiseaseDao
    abstract fun malnutritionScreeningDao(): MalnutritionScreeningDao
    abstract fun triageEvaluationDao(): TriageEvaluationDao
    abstract fun visitDao(): VisitDao
    abstract fun visitVitalSignDao(): VisitVitalSignDao
    abstract fun vitalSignDao(): VitalSignsDao
    abstract fun roomsDao(): RoomsDao
    abstract fun complaintSummaryDao(): ComplaintSummaryDao
}