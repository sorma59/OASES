package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM " + TableNames.USER + " WHERE username = :username")
    fun getUser(username: String): Flow<User?>

    @Query("SELECT * FROM " + TableNames.USER)
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM " + TableNames.USER + " WHERE role = :role")
    fun getAllUsersByRole(role: Role): Flow<List<User>>
}