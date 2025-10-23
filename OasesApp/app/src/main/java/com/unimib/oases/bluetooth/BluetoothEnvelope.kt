package com.unimib.oases.bluetooth

import kotlinx.serialization.Serializable

@Serializable
data class BluetoothEnvelope(
    val type: String,
    val payload: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BluetoothEnvelope

        if (type != other.type) return false
        if (!payload.contentEquals(other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + payload.contentHashCode()
        return result
    }
}

enum class BluetoothEnvelopeType {
    PATIENT,
    COMMAND
}

