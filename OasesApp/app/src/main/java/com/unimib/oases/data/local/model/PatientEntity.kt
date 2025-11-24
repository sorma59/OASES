package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(tableName = TableNames.PATIENT)
data class PatientEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "public_id") val publicId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "birth_date") val birthDate: String,
    @ColumnInfo(name = "sex") val sex: String,
    @ColumnInfo(name = "village") val village: String,
    @ColumnInfo(name = "parish") val parish: String,
    @ColumnInfo(name = "sub_county") val subCounty: String,
    @ColumnInfo(name = "district") val district: String,
    @ColumnInfo(name = "next_of_kin") val nextOfKin: String,
    @ColumnInfo(name = "contact") val contact: String,
    @ColumnInfo(name = "image") val image: ByteArray? = null
)