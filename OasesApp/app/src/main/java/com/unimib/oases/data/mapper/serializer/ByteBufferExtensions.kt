package com.unimib.oases.data.mapper.serializer

import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets // Recommended

/**
 * Reads a UTF-8 encoded string from the ByteBuffer.
 * The string is expected to be prefixed with an Int representing its length in bytes.
 */
fun ByteBuffer.readString(): String {
    val length = this.int
    // Consider more robust error handling or logging for invalid lengths
    if (length !in 0..1_048_576) { // 1MB sanity check, adjust as needed
        throw IllegalArgumentException("Invalid string length: $length. Buffer state: pos=${position()} lim=${limit()} cap=${capacity()}")
    }
    if (this.remaining() < length) {
        throw BufferUnderflowException()
    }
    if (length == 0) {
        return ""
    }
    val bytes = ByteArray(length)
    this.get(bytes)
    return String(bytes, StandardCharsets.UTF_8)
}

fun ByteBuffer.putBytes(bytes: ByteArray) {
    this.putInt(bytes.size)
    this.put(bytes)
}

// Symmetrical helpers for nullable strings
fun ByteBuffer.putNullableBytes(bytes: ByteArray?) {
    if (bytes != null) {
        this.put(1.toByte()) // Presence flag
        this.putBytes(bytes)
    } else {
        this.put(0.toByte()) // Absence flag
    }
}

fun ByteBuffer.readNullableString(): String? {
    val presenceFlag = this.get()
    return if (presenceFlag == 1.toByte()) {
        this.readString() // Read the full string if present
    } else {
        null
    }
}

/**
 * Writes a nullable value to the ByteBuffer.
 *
 * This function first writes a single byte to indicate whether the value is present or not.
 * - `1` is written if the `value` is not null.
 * - `0` is written if the `value` is null.
 *
 * If the `value` is not null, the provided `put` lambda is then called to serialize
 * and write the actual value to the buffer.
 *
 * This allows for the serialization of optional or nullable fields in a compact way.
 *
 * @param T The type of the value to be written.
 * @param value The nullable value to write to the buffer.
 * @param put A lambda function that takes a non-null value of type `T` and writes it to the ByteBuffer.
 */
fun ByteBuffer.putNullable(nullableByteArray: ByteArray?){
    nullableByteArray?.let {
        this.put(1) // Presence flag
        this.putInt(nullableByteArray.size)
        this.put(nullableByteArray)
    } ?: this.put(0) // Absence flag
}

/**
 * Reads a nullable value from the ByteBuffer.
 * It first reads a byte to determine if the value is null (0) or present (1).
 * If the value is present, it uses the provided `reader` lambda to deserialize it.
 *
 * @param T The type of the object to read.
 * @param reader A lambda function that takes the ByteBuffer and returns an object of type T.
 * @return The deserialized object of type T, or null if the stored value was null.
 */
fun <T> ByteBuffer.readNullable(deserializer: (ByteArray) -> T): T? {
    val presenceFlag = this.get().toInt()
    return if (presenceFlag == 1) {
        val size = this.int
        val itemBytes = ByteArray(size).also { this.get(it) }
        deserializer(itemBytes)
    } else
        null
}