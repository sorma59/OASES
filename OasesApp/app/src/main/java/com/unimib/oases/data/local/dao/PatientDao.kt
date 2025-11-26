package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.relation.PatientWithVisitInfoEntity
import com.unimib.oases.util.DateAndTimeUtils
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Upsert
    suspend fun insert(patient: PatientEntity): Long

    @Delete
    suspend fun delete(patient: PatientEntity)

    @Query("DELETE FROM " + TableNames.PATIENT + " WHERE id = :id")
    fun deleteById(id: String)

    @Query("SELECT * FROM " + TableNames.PATIENT)
    fun getPatients(): Flow<List<PatientEntity>>

    // Query for home screen
    @Query("""
        SELECT
            p.id AS patient_id,
            p.public_id AS patient_public_id,
            p.name AS patient_name,
            p.birth_date AS patient_birth_date,
            p.sex AS patient_sex,
            p.village AS patient_village,
            p.parish AS patient_parish,
            p.sub_county AS patient_sub_county,
            p.district AS patient_district,
            p.next_of_kin AS patient_next_of_kin,
            p.contact AS patient_contact,
            p.image AS patient_image,
    
            v.id AS visit_id,
            v.patient_id AS visit_patient_id,
            v.triage_code AS visit_triage_code,
            v.patient_status AS visit_patient_status,
            v.room_name AS visit_room_name,
            v.arrival_time AS visit_arrival_time,
            v.date AS visit_date,
            v.description AS visit_description
        FROM
            ${TableNames.PATIENT} p
        INNER JOIN
            ${TableNames.VISIT} v ON p.id = v.patient_id
        WHERE
            -- This logic is incorrect for finding the latest visit by time.
            -- It compares a time string with a date string.
            -- It should be based on the latest arrivalTime for that day or the latest visit entry.
            -- Corrected logic to find the latest visit based on arrival_time:
            v.date = :today
        ORDER BY
            v.arrival_time DESC
    """)
    fun getPatientsWithTodaysVisitInfo(today: String = DateAndTimeUtils.getCurrentDate().toString()): Flow<List<PatientWithVisitInfoEntity>>

//    @Query("UPDATE " + TableNames.PATIENT + " SET status = :triageState WHERE id = :patientId")
//    fun updateTriageState(patientId: String, triageState: String)

    @Query("SELECT * FROM " + TableNames.PATIENT + " WHERE id = :id")
    fun getPatientById(id: String): Flow<PatientEntity?>
}