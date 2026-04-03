package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.HistoryEntity
import com.unimib.oases.data.local.model.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("INSERT OR REPLACE INTO history SELECT * FROM patients WHERE id = :patientId")
    suspend fun copyPatientToHistory(patientId: String)

    @Query("DELETE FROM " + TableNames.HISTORY)
    fun delete()

    @Query("SELECT * FROM " + TableNames.HISTORY)
    fun getPatients(): Flow<List<HistoryEntity>>

}