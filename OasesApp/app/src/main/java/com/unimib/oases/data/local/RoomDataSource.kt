package com.unimib.oases.data.local

import com.unimib.oases.data.local.dao.PatientDao
import com.unimib.oases.data.local.dao.UserDao
import com.unimib.oases.data.model.Patient
import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val appDatabase: OasesDatabase
) {
    private val patientDao: PatientDao get() = appDatabase.patientDao()
    private val userDao: UserDao get() = appDatabase.userDao()

    // You can add other methods that directly interact with DAOs here
    suspend fun insertPatient(patient: Patient) {
        patientDao.insert(patient)
    }

    fun getPatients(): Flow<List<Patient>> {
        return patientDao.getPatients()
    }

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
}