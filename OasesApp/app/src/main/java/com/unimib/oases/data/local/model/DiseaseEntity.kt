package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(tableName = TableNames.DISEASE)
data class DiseaseEntity (
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sex_specificity") val sexSpecificity: String,
    @ColumnInfo(name = "age_specificity") val ageSpecificity: String,
)