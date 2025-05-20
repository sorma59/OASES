package com.unimib.oases.data.mapper

import android.util.Log
import com.unimib.oases.data.model.PatientStatus
import com.unimib.oases.domain.model.Patient
import java.nio.ByteBuffer

object PatientSerializer {

    fun serialize(patient: Patient): ByteArray {
        val idBytes = patient.id.toByteArray(Charsets.UTF_8)
        val nameBytes = patient.name.toByteArray(Charsets.UTF_8)
        val sexBytes = patient.sex.toByteArray(Charsets.UTF_8)
        val villageBytes = patient.village.toByteArray(Charsets.UTF_8)
        val parishBytes = patient.parish.toByteArray(Charsets.UTF_8)
        val subCountyBytes = patient.subCounty.toByteArray(Charsets.UTF_8)
        val districtBytes = patient.district.toByteArray(Charsets.UTF_8)
        val nextOfKinBytes = patient.nextOfKin.toByteArray(Charsets.UTF_8)
        val contactBytes = patient.contact.toByteArray(Charsets.UTF_8)
        val statusBytes = patient.status.toByteArray(Charsets.UTF_8)
        val imageBytes = patient.image ?: ByteArray(0)

        val buffer = ByteBuffer.allocate(
            4 + // age
                    4 + idBytes.size +
                    4 + nameBytes.size +
                    4 + sexBytes.size +
                    4 + villageBytes.size +
                    4 + parishBytes.size +
                    4 + subCountyBytes.size +
                    4 + districtBytes.size +
                    4 + nextOfKinBytes.size +
                    4 + contactBytes.size +
                    4 + statusBytes.size +
                    4 + imageBytes.size
        ).order(java.nio.ByteOrder.BIG_ENDIAN)

        buffer.putInt(patient.age)

        buffer.putInt(idBytes.size)
        buffer.put(idBytes)

        buffer.putInt(nameBytes.size)
        buffer.put(nameBytes)

        buffer.putInt(sexBytes.size)
        buffer.put(sexBytes)

        buffer.putInt(villageBytes.size)
        buffer.put(villageBytes)

        buffer.putInt(parishBytes.size)
        buffer.put(parishBytes)

        buffer.putInt(subCountyBytes.size)
        buffer.put(subCountyBytes)

        buffer.putInt(districtBytes.size)
        buffer.put(districtBytes)

        buffer.putInt(nextOfKinBytes.size)
        buffer.put(nextOfKinBytes)

        buffer.putInt(contactBytes.size)
        buffer.put(contactBytes)

        buffer.putInt(statusBytes.size)
        buffer.put(statusBytes)

        buffer.putInt(imageBytes.size)
        buffer.put(imageBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Patient {
        val buffer = ByteBuffer.wrap(bytes)

        val age = buffer.int

        val id = buffer.readString()
        val name = buffer.readString()
        val sex = buffer.readString()
        val village = buffer.readString()
        val parish = buffer.readString()
        val subCounty = buffer.readString()
        val district = buffer.readString()
        val nextOfKin = buffer.readString()
        val contact = buffer.readString()
        val status = buffer.readString()

        val imageLength = buffer.int
        val image = if (imageLength > 0) {
            ByteArray(imageLength).also { buffer.get(it) }
        } else {
            null
        }

        return Patient(
            id = id,
            name = name,
            age = age,
            sex = sex,
            village = village,
            parish = parish,
            subCounty = subCounty,
            district = district,
            nextOfKin = nextOfKin,
            contact = contact,
            status = status,
            image = image
        )
    }

    private fun ByteBuffer.readString(): String {
        Log.d("PatientSerializer", "Before reading length: position=${this.position()}, limit=${this.limit()}, remaining=${this.remaining()}")
        val length = this.int
        Log.d("PatientSerializer", "Read length: $length, position=${this.position()}, limit=${this.limit()}, remaining=${this.remaining()}")
        if (length < 0 || length > 1024 * 10) { // 10KB max (adjust as needed)
            throw IllegalStateException("Invalid string length: $length")
        }
        if (this.remaining() < length){
            throw IllegalStateException("Not enough bytes left in the buffer, length: $length, remaining: ${this.remaining()}")
        }
        val bytes = ByteArray(length)
        this.get(bytes)
        return String(bytes, Charsets.UTF_8)
    }

    // ----------------Testing--------------------
    fun test() {
        val original = Patient("test", "John", 30, "M", "Kampala", "Central", "Wakiso", "UG", "Jane", "123456", PatientStatus.WAITING_FOR_TRIAGE.name, null)
        Log.d("PatientSerializer", "Original: $original")
        val bytes = serialize(original)
        val recovered = deserialize(bytes)
        Log.d("PatientSerializer", "Recovered: $recovered")
    }
}
