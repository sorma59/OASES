package com.unimib.oases.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity (
    @PrimaryKey val id: String,
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

){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PatientEntity

        if (age != other.age) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (sex != other.sex) return false
        if (village != other.village) return false
        if (parish != other.parish) return false
        if (subCounty != other.subCounty) return false
        if (district != other.district) return false
        if (nextOfKin != other.nextOfKin) return false
        if (contact != other.contact) return false
        if (status != other.status) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = age
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + sex.hashCode()
        result = 31 * result + village.hashCode()
        result = 31 * result + parish.hashCode()
        result = 31 * result + subCounty.hashCode()
        result = 31 * result + district.hashCode()
        result = 31 * result + nextOfKin.hashCode()
        result = 31 * result + contact.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}

/**
 * Represents the different states a patient can be in during their hospital visit.
 */
enum class PatientStatus {
    WAITING_FOR_TRIAGE,
    WAITING_FOR_VISIT,
    WAITING_FOR_TESTS_RESULTS,
    DISMISSED
}