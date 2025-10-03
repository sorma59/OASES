package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.Sex

@Entity(tableName = TableNames.PATIENT)
data class PatientEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "public_id") val publicId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "birth_date") val birthDate: String,
    @ColumnInfo(name = "sex") val sex: Sex,
    @ColumnInfo(name = "village") val village: String,
    @ColumnInfo(name = "parish") val parish: String,
    @ColumnInfo(name = "sub_county") val subCounty: String,
    @ColumnInfo(name = "district") val district: String,
    @ColumnInfo(name = "next_of_kin") val nextOfKin: String,
    @ColumnInfo(name = "contact") val contact: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "room") val room: String,
    @ColumnInfo(name = "arrival_time") val arrivalTime: String,
    @ColumnInfo(name = "image") val image: ByteArray? = null
)

/**
 * Represents the different states a patient can be in during their hospital visit.
 */
enum class PatientStatus(val displayValue: String) {
    WAITING_FOR_TRIAGE("Waiting for triage"),
    WAITING_FOR_VISIT("Waiting for visit"),
    WAITING_FOR_TEST_RESULTS("Waiting for test results"),
    HOSPITALIZED("Hospitalized"),
    DISMISSED("Dismissed")
}