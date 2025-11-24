package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.data.local.model.relation.PatientWithVisitInfoEntity
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

    // *** NEW, POWERFUL FUNCTION FOR YOUR HOME SCREEN ***
    @Query("""
        SELECT
            p.*,  -- Select all columns from the patient table for the @Embedded PatientEntity
            v.patient_status AS status,
            v.triage_code AS code,
            v.room_name AS room, -- Use the correct column name 'roomName' from VisitEntity
            v.arrival_time AS arrivalTime
        FROM
            ${TableNames.PATIENT} p
        INNER JOIN
            ${TableNames.VISIT} v ON p.id = v.patient_id
        WHERE
            -- This logic is incorrect for finding the latest visit by time.
            -- It compares a time string with a date string.
            -- It should be based on the latest arrivalTime for that day or the latest visit entry.
            -- Corrected logic to find the latest visit based on arrival_time:
            v.arrival_time = (
                SELECT MAX(v2.arrival_time) 
                FROM ${TableNames.VISIT} v2 
                WHERE v2.patient_id = p.id
            )
        ORDER BY
            v.arrival_time DESC
    """)
    fun getPatientsWithLatestVisitInfo(): Flow<List<PatientWithVisitInfoEntity>>

//    @Query("UPDATE " + TableNames.PATIENT + " SET status = :triageState WHERE id = :patientId")
//    fun updateTriageState(patientId: String, triageState: String)

    @Query("SELECT * FROM " + TableNames.PATIENT + " WHERE id = :id")
    fun getPatientById(id: String): Flow<PatientEntity?>
}