package com.unimib.oases.data.local

import androidx.room.withTransaction
import com.unimib.oases.data.local.dao.ComplaintSummaryDao
import com.unimib.oases.data.local.dao.DiseaseDao
import com.unimib.oases.data.local.dao.MalnutritionScreeningDao
import com.unimib.oases.data.local.dao.PatientDao
import com.unimib.oases.data.local.dao.PatientDiseaseDao
import com.unimib.oases.data.local.dao.RoomsDao
import com.unimib.oases.data.local.dao.TriageEvaluationDao
import com.unimib.oases.data.local.dao.UserDao
import com.unimib.oases.data.local.dao.VisitDao
import com.unimib.oases.data.local.dao.VisitVitalSignDao
import com.unimib.oases.data.local.dao.VitalSignsDao
import com.unimib.oases.data.local.db.AuthDatabase
import com.unimib.oases.data.local.db.OasesDatabase
import com.unimib.oases.data.local.model.ComplaintSummaryEntity
import com.unimib.oases.data.local.model.DiseaseEntity
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.data.local.model.PatientDiseaseEntity
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.RoomEntity
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.data.local.model.User
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.VisitVitalSignEntity
import com.unimib.oases.data.local.model.VitalSignEntity
import com.unimib.oases.data.local.model.relation.PatientWithVisitInfoEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
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
    private val roomsDao: RoomsDao get() = appDatabase.roomsDao()
    private val complaintSummaryDao: ComplaintSummaryDao get() = appDatabase.complaintSummaryDao()

    // -------------------Patients-------------------
    suspend fun insertPatient(patient: PatientEntity) {
        patientDao.insert(patient)
    }

    suspend fun insertPatientAndCreateVisit(
        patient: PatientEntity,
        visit: VisitEntity,
    ) {
        appDatabase.withTransaction {
            insertPatient(patient)
            insertVisit(visit)
        }
    }

    suspend fun deletePatient(patient: PatientEntity) {
        patientDao.delete(patient)
    }

    fun deleteById(id: String) {
        patientDao.deleteById(id)
    }

    fun getPatients(): Flow<List<PatientEntity>> {
        return patientDao.getPatients()
    }

    fun getPatientsAndVisitsOn(date: LocalDate): Flow<List<PatientWithVisitInfoEntity>> {
        return patientDao.getPatientsAndVisitsOn(date)
    }

    fun getPatientById(id: String): Flow<PatientEntity?> {
        return patientDao.getPatientById(id)
    }

//    fun updateTriageState(patient: PatientEntity, triageState: String) {
//        patientDao.updateTriageState(patient.id, triageState)
//    }

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

    // ----------------Rooms---------------
    suspend fun insertRoom(room: RoomEntity) {
        roomsDao.insert(room)
    }

    fun getAllRooms(): Flow<List<RoomEntity>> {
        return roomsDao.getAllRooms()
    }

    suspend fun deleteRoom(room: RoomEntity) {
        roomsDao.delete(room)
    }

    fun getRoom(name: String): Flow<RoomEntity?> {
        return roomsDao.getRoom(name)
    }



    // ------------Patients Diseases----------------
    suspend fun insertPatientDisease(patientDisease: PatientDiseaseEntity) {
        patientDiseaseDao.insert(patientDisease)
    }

    suspend fun insertPatientDiseases(patientDiseases: List<PatientDiseaseEntity>){
        patientDiseaseDao.insertAll(patientDiseases)
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

    fun getMalnutritionScreening(visitId: String): Flow<MalnutritionScreeningEntity?> {
        return malnutritionScreeningDao.getMalnutritionScreening(visitId)
    }

    // -------------------Triage Evaluation-------------------
    suspend fun insertTriageEvaluation(triageEvaluation: TriageEvaluationEntity) {
        triageEvaluationDao.insert(triageEvaluation)
    }

    fun getTriageEvaluation(visitId: String): Flow<TriageEvaluationEntity?> {
        return triageEvaluationDao.getTriageEvaluation(visitId)
    }

    // -----------------Visits--------------------

    suspend fun insertVisit(visit: VisitEntity) {
        visitDao.insert(visit)
    }

    suspend fun insertTriageEvaluationAndUpdateVisit(
        visit: VisitEntity,
        triageEvaluation: TriageEvaluationEntity
    ) {
        appDatabase.withTransaction {
            insertVisit(visit)
            insertTriageEvaluation(triageEvaluation)
        }
    }

    suspend fun upsertVisit(visit: VisitEntity) {
        visitDao.upsert(visit)
    }

    fun getVisits(patientId: String): Flow<List<VisitEntity>> {
        return visitDao.getVisits(patientId)
    }

    fun getVisitById(visitId: String): Flow<VisitEntity> {
        return visitDao.getVisitById(visitId)
    }

    fun getVisitWithPatientInfo(visitId: String): Flow<PatientWithVisitInfoEntity> {
        return visitDao.getTodaysVisitWithPatientInfo(visitId)
    }

    fun getCurrentVisit(patientId: String): Flow<VisitEntity?> {
        return visitDao.getCurrentVisit(patientId)
    }

    // ----------------Visits Vital Signs----------------

    suspend fun insertVisitVitalSign(visitVitalSign: VisitVitalSignEntity) {
        visitVitalSignDao.insert(visitVitalSign)
    }

    suspend fun insertVisitVitalSigns(visitVitalSigns: List<VisitVitalSignEntity>) {
        visitVitalSignDao.insertAll(visitVitalSigns)
    }

    fun getVisitVitalSigns(visitId: String): Flow<List<VisitVitalSignEntity>> {
        return visitVitalSignDao.getVisitVitalSigns(visitId)
    }

    fun getLatestVisitVitalSigns(visitId: String): Flow<List<VisitVitalSignEntity>> {
        return visitVitalSignDao.getVisitLatestVitalSigns(visitId)
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

    // Complaint summaries --------------------------
    suspend fun insertComplaintSummary(complaintSummary: ComplaintSummaryEntity) {
        complaintSummaryDao.insert(complaintSummary)
    }

    suspend fun insertComplaintSummaries(complaintSummaries: List<ComplaintSummaryEntity>) {
        complaintSummaryDao.insertAll(complaintSummaries)
    }

    suspend fun deleteComplaintSummary(complaintSummary: ComplaintSummaryEntity) {
        complaintSummaryDao.delete(complaintSummary)
    }

    fun getVisitComplaintsSummaries(visitId: String): Flow<List<ComplaintSummaryEntity>> {
        return complaintSummaryDao.getVisitComplaintsSummaries(visitId)
    }

    fun getComplaintSummary(visitId: String, complaintId: String): Flow<ComplaintSummaryEntity?> {
        return complaintSummaryDao.getComplaintSummary(visitId, complaintId)
    }
}