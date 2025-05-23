package com.unimib.oases.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unimib.oases.data.local.dao.UserDao
import com.unimib.oases.data.model.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class AuthDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}