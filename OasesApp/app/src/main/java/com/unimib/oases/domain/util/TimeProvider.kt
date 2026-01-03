package com.unimib.oases.domain.util

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class TimeProvider @Inject constructor(
    private val clock: Clock,
    private val zoneId: ZoneId
) {

    fun nowInstant(): Instant =
        Instant.now(clock)

    fun nowDate(): LocalDate =
        LocalDate.now(clock)

    fun nowDateTime(): LocalDateTime =
        LocalDateTime.now(clock)

    fun today(): LocalDate =
        LocalDate.now(clock.withZone(zoneId))
}