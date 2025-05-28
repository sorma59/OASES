package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.model.VitalSignEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface VitalSignsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vitalSign: VitalSignEntity)

    @Delete
    suspend fun delete(vitalSign: VitalSignEntity)

    @Query("SELECT * FROM " + TableNames.VITAL_SIGN + " WHERE name = :vitalSign")
    fun getVitalSign(vitalSign: String): Flow<VitalSignEntity?>

    @Query("SELECT * FROM " + TableNames.VITAL_SIGN)
    fun getAllVitalSigns(): Flow<List<VitalSignEntity>>
}