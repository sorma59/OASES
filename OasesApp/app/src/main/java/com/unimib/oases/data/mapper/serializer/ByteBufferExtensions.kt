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
    if (length < 0 || length > 1_048_576) { // 1MB sanity check, adjust as needed
        throw IllegalArgumentException("Invalid string length: $length. Buffer state: pos=${position()} lim=${limit()} cap=${capacity()}")
    }
    if (this.remaining() < length) {
        throw BufferUnderflowException()
    }
    val bytes = ByteArray(length)
    this.get(bytes)
    return String(bytes, StandardCharsets.UTF_8)
}