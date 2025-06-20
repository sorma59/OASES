package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(tableName = TableNames.PATIENT)
data class PatientEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "public_id") var publicId: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "age") var age: Int,
    @ColumnInfo(name = "sex") var sex: String,
    @ColumnInfo(name = "village") var village: String,
    @ColumnInfo(name = "parish") var parish: String,
    @ColumnInfo(name = "sub_county") var subCounty: String,
    @ColumnInfo(name = "district") var district: String,
    @ColumnInfo(name = "next_of_kin") var nextOfKin: String,
    @ColumnInfo(name = "contact") var contact: String,
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "image") var image: ByteArray? = null

)

/**
 * Represents the different states a patient can be in during their hospital visit.
 */
enum class PatientStatus {
    WAITING_FOR_TRIAGE,
    WAITING_FOR_VISIT,
    WAITING_FOR_TESTS_RESULTS,
    DISMISSED
}