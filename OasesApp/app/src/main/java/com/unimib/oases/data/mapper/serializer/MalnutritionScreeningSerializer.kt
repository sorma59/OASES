package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.domain.model.Muac
import java.nio.ByteBuffer
import java.nio.ByteOrder

object MalnutritionScreeningSerializer {

    fun serialize(malnutritionScreening: MalnutritionScreening): ByteArray {
        val visitIdBytes = malnutritionScreening.visitId.toByteArray(Charsets.UTF_8)
        val weightBytes = malnutritionScreening.weight.toString().toByteArray(Charsets.UTF_8)
        val heightBytes = malnutritionScreening.height.toString().toByteArray(Charsets.UTF_8)
        val muacBytes = malnutritionScreening.muac.value.toString().toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + visitIdBytes.size +
            4 + weightBytes.size +
            4 + heightBytes.size +
            4 + muacBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(visitIdBytes.size)
        buffer.put(visitIdBytes)

        buffer.putInt(weightBytes.size)
        buffer.put(weightBytes)

        buffer.putInt(heightBytes.size)
        buffer.put(heightBytes)

        buffer.putInt(muacBytes.size)
        buffer.put(muacBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): MalnutritionScreening {
        val buffer = ByteBuffer.wrap(bytes)

        val visitId = buffer.readString()
        val weight = buffer.readString().toDouble()
        val height = buffer.readString().toDouble()
        val muacValue = buffer.readString().toDouble()

        return MalnutritionScreening(
            visitId = visitId,
            weight = weight,
            height = height,
            muac = Muac(muacValue)
        )

    }

}