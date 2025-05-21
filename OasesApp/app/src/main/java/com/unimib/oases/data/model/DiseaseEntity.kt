package com.unimib.oases.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(tableName = TableNames.DISEASE)
data class DiseaseEntity (
    @PrimaryKey @ColumnInfo(name = "name") var name: String,
)