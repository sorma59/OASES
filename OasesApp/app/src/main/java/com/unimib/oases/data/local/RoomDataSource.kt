package com.unimib.oases.data.local

import com.unimib.oases.data.local.dao.DiseaseDao
import com.unimib.oases.data.local.dao.PatientDao
import com.unimib.oases.data.local.dao.PatientDiseaseDao
import com.unimib.oases.data.local.dao.UserDao
import com.unimib.oases.data.local.dao.VisitDao
import com.unimib.oases.data.model.DiseaseEntity
import com.unimib.oases.data.model.PatientDiseaseEntity
import com.unimib.oases.data.model.PatientEntity
import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import com.unimib.oases.data.model.VisitEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val appDatabase: OasesDatabase
) {
    private val patientDao: PatientDao get() = appDatabase.patientDao()
    private val userDao: UserDao get() = appDatabase.userDao()
    private val patientDiseaseDao: PatientDiseaseDao get() = appDatabase.patientDiseaseDao()
    private val diseaseDao: DiseaseDao get() = appDatabase.diseaseDao()
    private val visitDao: VisitDao get() = appDatabase.visitDao()

    // -------------------Patients-------------------
    suspend fun insertPatient(patient: PatientEntity) {
        patientDao.insert(patient)
    }

    fun getPatients(): Flow<List<PatientEntity>> {
        return patientDao.getPatients()
    }

    suspend fun getPatientById(id: String): PatientEntity? {
        return patientDao.getPatientById(id)
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

    fun deleteUser(user: User) {
        userDao.delete(user)
    }

    // ------------Patient Disease----------------
    suspend fun insertPatientDisease(patientDisease: PatientDiseaseEntity) {
        patientDiseaseDao.insert(patientDisease)
    }

    suspend fun deletePatientDisease(patientId: String, diseaseName: String) {
        patientDiseaseDao.delete(patientId, diseaseName)
    }

    fun getPatientDiseases(patientId: String): Flow<List<DiseaseEntity>> {
        return patientDiseaseDao.getPatientDiseases(patientId)
    }

    // -------------------Diseases-------------------
    suspend fun insertDisease(disease: DiseaseEntity) {
        diseaseDao.insert(disease)
    }

    fun getAllDiseases(): Flow<List<DiseaseEntity>> {
        return diseaseDao.getAllDiseases()
    }

    // -----------------Visits--------------------

    suspend fun insertVisit(visit: VisitEntity) {
        visitDao.insertVisit(visit)
    }

    suspend fun upsertVisit(visit: VisitEntity) {
        visitDao.upsertVisit(visit)
    }

    fun getVisits(patientId: String): Flow<List<VisitEntity>> {
        return visitDao.getVisits(patientId)
    }
}