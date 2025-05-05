package com.unimib.oases.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "patients")
@Parcelize
data class Patient (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "age") var age: Int,
    @ColumnInfo(name = "sex") var sex: String,
    @ColumnInfo(name = "village") var village: String,
    @ColumnInfo(name = "parish") var parish: String,
    @ColumnInfo(name = "sub_county") var subCounty: String,
    @ColumnInfo(name = "district") var district: String,
    @ColumnInfo(name = "next_of_kin") var nextOfKin: String,
    @ColumnInfo(name = "contact") var contact: String,
    @ColumnInfo(name = "image") var image: ByteArray? = null

) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Patient

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
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}