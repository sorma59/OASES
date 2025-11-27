package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.screen.nurse_assessment.demographics.Sex
import java.nio.ByteBuffer
import java.nio.ByteOrder

object PatientSerializer {

    fun serialize(patient: Patient): ByteArray {
        val idBytes = patient.id.toByteArray(Charsets.UTF_8)
        val publicIdBytes = patient.publicId.toByteArray(Charsets.UTF_8)
        val nameBytes = patient.name.toByteArray(Charsets.UTF_8)
        val birthDateBytes = patient.birthDate.toByteArray(Charsets.UTF_8)
        val sexBytes = patient.sex.name.toByteArray(Charsets.UTF_8)
        val villageBytes = patient.village.toByteArray(Charsets.UTF_8)
        val parishBytes = patient.parish.toByteArray(Charsets.UTF_8)
        val subCountyBytes = patient.subCounty.toByteArray(Charsets.UTF_8)
        val districtBytes = patient.district.toByteArray(Charsets.UTF_8)
        val nextOfKinBytes = patient.nextOfKin.toByteArray(Charsets.UTF_8)
        val contactBytes = patient.contact.toByteArray(Charsets.UTF_8)
        val imageBytes = patient.image ?: ByteArray(0)

        val buffer = ByteBuffer.allocate(
            4 + // age
                    4 + idBytes.size +
                    4 + publicIdBytes.size +
                    4 + nameBytes.size +
                    4 + birthDateBytes.size +
                    4 + sexBytes.size +
                    4 + villageBytes.size +
                    4 + parishBytes.size +
                    4 + subCountyBytes.size +
                    4 + districtBytes.size +
                    4 + nextOfKinBytes.size +
                    4 + contactBytes.size +
                    4 + imageBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(patient.ageInMonths)

        buffer.putBytes(idBytes)

        buffer.putBytes(publicIdBytes)

        buffer.putBytes(nameBytes)

        buffer.putBytes(birthDateBytes)

        buffer.putBytes(sexBytes)

        buffer.putBytes(villageBytes)

        buffer.putBytes(parishBytes)

        buffer.putBytes(subCountyBytes)

        buffer.putBytes(districtBytes)

        buffer.putBytes(nextOfKinBytes)

        buffer.putBytes(contactBytes)

        buffer.putBytes(imageBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Patient {
        val buffer = ByteBuffer.wrap(bytes)

        val ageInMonths = buffer.int

        val id = buffer.readString()
        val publicId = buffer.readString()
        val name = buffer.readString()
        val birthDate = buffer.readString()
        val sex = Sex.valueOf(buffer.readString())
        val village = buffer.readString()
        val parish = buffer.readString()
        val subCounty = buffer.readString()
        val district = buffer.readString()
        val nextOfKin = buffer.readString()
        val contact = buffer.readString()

        val imageLength = buffer.int
        val image = if (imageLength > 0) {
            ByteArray(imageLength).also { buffer.get(it) }
        } else {
            null
        }

        return Patient(
            id = id,
            publicId = publicId,
            name = name,
            birthDate = birthDate,
            ageInMonths = ageInMonths,
            sex = sex,
            village = village,
            parish = parish,
            subCounty = subCounty,
            district = district,
            nextOfKin = nextOfKin,
            contact = contact,
            image = image,

        )
    }

    // ----------------Testing--------------------
    fun test() {
        val original = Patient(
            name = "John Doe",
            birthDate = "1990-01-01",
            ageInMonths = 30,
            sex = Sex.FEMALE,
            village = "Village",
            parish = "Parish",
            subCounty = "Sub-county",
            district = "District",
            nextOfKin = "Next of kin",
            contact = "123456789",
        )
        Log.d("PatientSerializer", "Original: $original")
        val bytes = serialize(original)
        val recovered = deserialize(bytes)
        Log.d("PatientSerializer", "Recovered: $recovered")
    }
}