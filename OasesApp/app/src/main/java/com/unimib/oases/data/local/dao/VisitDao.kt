package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.data.local.model.relation.PatientWithVisitInfoEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface VisitDao {

    @Upsert
    suspend fun insert(visit: VisitEntity)

    @Upsert
    suspend fun upsert(visit: VisitEntity)

    @Query("SELECT * FROM " + TableNames.VISIT + " WHERE patient_id = :patientId")
    fun getVisits(patientId: String): Flow<List<VisitEntity>>

    @Query("SELECT * FROM ${TableNames.VISIT} WHERE id = :visitId")
    fun getVisitById(visitId: String): Flow<VisitEntity>

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
        WHERE v.id = :visitId
    """)
    fun getVisitWithPatientInfo(visitId: String): Flow<PatientWithVisitInfoEntity>

    @Query("SELECT * FROM " + TableNames.VISIT + " WHERE patient_id = :patientId AND date = :today LIMIT 1")
    fun getCurrentVisit(patientId: String, today: String = LocalDate.now().toString()): Flow<VisitEntity?>

}