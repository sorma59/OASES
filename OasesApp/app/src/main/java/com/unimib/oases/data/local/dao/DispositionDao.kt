package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.DispositionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DispositionDao {
    @Upsert
    suspend fun insert(disposition: DispositionEntity)

    @Query("SELECT * FROM " + TableNames.DISPOSITION + " WHERE visit_id = :visitId")
    fun getDisposition(visitId: String): Flow<DispositionEntity?>
}