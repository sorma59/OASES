package com.unimib.oases.data.local

import com.unimib.oases.data.local.dao.DiseaseDao
import com.unimib.oases.data.local.dao.MalnutritionScreeningDao
import com.unimib.oases.data.local.dao.PatientDao
import com.unimib.oases.data.local.dao.PatientDiseaseDao
import com.unimib.oases.data.local.dao.TriageEvaluationDao
import com.unimib.oases.data.local.dao.UserDao
import com.unimib.oases.data.local.dao.VisitDao
import com.unimib.oases.data.local.dao.VisitVitalSignDao
import com.unimib.oases.data.local.dao.VitalSignsDao
import com.unimib.oases.data.local.db.AuthDatabase
import com.unimib.oases.data.local.db.OasesDatabase
import com.unimib.oases.data.local.model.DiseaseEntity
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.data.local.model.PatientDiseaseEntity
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.data.local.model.User
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.VisitVitalSignEntity
import com.unimib.oases.data.local.model.VitalSignEntity
import com.unimib.oases.domain.model.VisitStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val appDatabase: OasesDatabase,
    private val authDatabase: AuthDatabase
) {
    private val patientDao: PatientDao get() = appDatabase.patientDao()
    private val userDao: UserDao get() = authDatabase.userDao()
    private val patientDiseaseDao: PatientDiseaseDao get() = appDatabase.patientDiseaseDao()
    private val diseaseDao: DiseaseDao get() = appDatabase.diseaseDao()
    private val malnutritionScreeningDao: MalnutritionScreeningDao get() = appDatabase.malnutritionScreeningDao()
    private val triageEvaluationDao: TriageEvaluationDao get() = appDatabase.triageEvaluationDao()
    private val visitDao: VisitDao get() = appDatabase.visitDao()
    private val visitVitalSignDao: VisitVitalSignDao get() = appDatabase.visitVitalSignDao()
    private val vitalSignDao: VitalSignsDao get() = appDatabase.vitalSignDao()

    // -------------------Patients-------------------
    suspend fun insertPatient(patient: PatientEntity) {
        patientDao.insert(patient)
    }

    suspend fun deletePatient(patient: PatientEntity) {
        patientDao.delete(patient)
    }

    fun getPatients(): Flow<List<PatientEntity>> {
        return patientDao.getPatients()
    }

    fun getPatientById(id: String): Flow<PatientEntity?> {
        return patientDao.getPatientById(id)
    }

//    fun updateTriageState(patient: PatientEntity, triageState: String) {
//        patientDao.updateTriageState(patient.id, triageState)
//    }

    suspend fun updateStatus(patient: PatientEntity, status: String) {
        patientDao.updateStatus(patient.id, status)
    }

    // ----------------Users---------------

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    fun getUser(username: String): Flow<User?> {
        return userDao.getUser(username)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    fun getAllUsersByRole(role: Role): Flow<List<User>> {
        return userDao.getAllUsersByRole(role)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    // ------------Patients Diseases----------------
    suspend fun insertPatientDisease(patientDisease: PatientDiseaseEntity) {
        patientDiseaseDao.insert(patientDisease)
    }

    suspend fun deletePatientDisease(patientId: String, diseaseName: String) {
        patientDiseaseDao.delete(patientId, diseaseName)
    }

    fun getPatientDiseases(patientId: String): Flow<List<PatientDiseaseEntity>> {
        return patientDiseaseDao.getPatientDiseases(patientId)
    }

    // -------------------Diseases-------------------
    suspend fun insertDisease(disease: DiseaseEntity) {
        diseaseDao.insert(disease)
    }

    fun getFilteredDiseases(sex: String, age: String): Flow<List<DiseaseEntity>> {
        return diseaseDao.getFilteredDiseases(sex, age)
    }

    fun getAllDiseases(): Flow<List<DiseaseEntity>> {
        return diseaseDao.getAllDiseases()
    }

    fun getDisease(disease: String): Flow<DiseaseEntity?> {
        return diseaseDao.getDisease(disease)
    }

    suspend fun deleteDisease(disease: DiseaseEntity) {
        diseaseDao.delete(disease)
    }

    // -------------------Malnutrition Screening-------------------
    suspend fun insertMalnutritionScreening(malnutritionScreening: MalnutritionScreeningEntity) {
        malnutritionScreeningDao.insert(malnutritionScreening)
    }

    fun getMalnutritionScreening(visitId: String): MalnutritionScreeningEntity? {
        return malnutritionScreeningDao.getMalnutritionScreening(visitId)
    }

    // -------------------Triage Evaluation-------------------
    suspend fun insertTriageEvaluation(triageEvaluation: TriageEvaluationEntity) {
        triageEvaluationDao.insert(triageEvaluation)
    }

    fun getTriageEvaluation(visitId: String): TriageEvaluationEntity {
        return triageEvaluationDao.getTriageEvaluation(visitId)
    }

    // -----------------Visits--------------------

    suspend fun insertVisit(visit: VisitEntity) {
        visitDao.insert(visit)
    }

    suspend fun upsertVisit(visit: VisitEntity) {
        visitDao.upsert(visit)
    }

    fun getVisits(patientId: String): Flow<List<VisitEntity>> {
        return visitDao.getVisits(patientId)
    }

    fun getCurrentVisit(patientId: String): VisitEntity? {
        return visitDao.getCurrentVisit(patientId, VisitStatus.CLOSED.name)
    }

    // ----------------Visits Vital Signs----------------

    suspend fun insertVisitVitalSigns(visitVitalSign: VisitVitalSignEntity) {
        visitVitalSignDao.insert(visitVitalSign)
    }

    fun getVisitVitalSigns(visitId: String): Flow<List<VisitVitalSignEntity>> {
        return visitVitalSignDao.getVisitVitalSigns(visitId)
    }

    // -------------------Vital Signs--------------------

    suspend fun insertVitalSign(vitalSign: VitalSignEntity) {
        vitalSignDao.insert(vitalSign)
    }

    fun getAllVitalSigns(): Flow<List<VitalSignEntity>> {
        return vitalSignDao.getAllVitalSigns()
    }

    fun getVitalSign(vitalSign: String): Flow<VitalSignEntity?> {
        return vitalSignDao.getVitalSign(vitalSign)
    }

    suspend fun deleteVitalSign(vitalSign: VitalSignEntity) {
        vitalSignDao.delete(vitalSign)
    }
}