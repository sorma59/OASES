package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.DiseaseEntity
import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.SexSpecificity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseDao {
    @Upsert
    suspend fun insert(disease: DiseaseEntity)

    @Delete
    suspend fun delete(disease: DiseaseEntity)

    @Query("SELECT * FROM " + TableNames.DISEASE + " WHERE name = :disease GROUP BY 'group'")
    fun getDisease(disease: String): Flow<DiseaseEntity?>

    @Query("""
        SELECT * FROM ${TableNames.DISEASE} 
        WHERE sex_specificity IN (:sex, 'ALL') AND age_specificity IN (:age, 'ALL')
        ORDER BY CASE `group`
            WHEN 'ALLERGIES' THEN 1
            WHEN 'MEDICATIONS' THEN 2
            WHEN 'VACCINATIONS' THEN 3
            WHEN 'INFECTIOUS' THEN 4
            WHEN 'HAEMATOLOGICAL' THEN 5
            WHEN 'GASTROINTESTINAL' THEN 6
            WHEN 'CARDIOVASCULAR' THEN 7
            WHEN 'LUNG' THEN 8
            WHEN 'KIDNEY' THEN 9
            WHEN 'ENDOCRINE' THEN 10
            WHEN 'NEUROPSYCHIATRIC' THEN 11
            WHEN 'MEDICAL_CONDITIONS' THEN 12
            WHEN 'MEDICAL_EVENTS' THEN 13
            WHEN 'OBSTETRIC_HISTORY' THEN 14
            ELSE 99 
        END ASC
    """)
    fun getFilteredDiseases(sex: SexSpecificity, age: AgeSpecificity): Flow<List<DiseaseEntity>>

    @Query("""
        SELECT * FROM ${TableNames.DISEASE} 
        ORDER BY CASE `group`
            WHEN 'ALLERGIES' THEN 1
            WHEN 'MEDICATIONS' THEN 2
            WHEN 'VACCINATIONS' THEN 3
            WHEN 'INFECTIOUS' THEN 4
            WHEN 'HAEMATOLOGICAL' THEN 5
            WHEN 'GASTROINTESTINAL' THEN 6
            WHEN 'CARDIOVASCULAR' THEN 7
            WHEN 'LUNG' THEN 8
            WHEN 'KIDNEY' THEN 9
            WHEN 'ENDOCRINE' THEN 10
            WHEN 'NEUROPSYCHIATRIC' THEN 11
            WHEN 'MEDICAL_CONDITIONS' THEN 12
            WHEN 'MEDICAL_EVENTS' THEN 13
            WHEN 'OBSTETRIC_HISTORY' THEN 14
            ELSE 99 
        END ASC
    """)
    fun getAllDiseases(): Flow<List<DiseaseEntity>>
}
