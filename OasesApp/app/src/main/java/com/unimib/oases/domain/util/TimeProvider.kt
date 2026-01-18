package com.unimib.oases.domain.util

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class TimeProvider @Inject constructor(
    private val clock: Clock,
    private val zoneId: ZoneId
) {
    // This creates a new clock instance set specifically to Africa/Kampala
    private val zonedClock: Clock get() = clock.withZone(zoneId)

    /**
     * Use this for absolute timestamps (e.g., for an API or DB sync).
     * Instant is ALWAYS UTC, so zoneId is not needed here.
     */
    fun nowInstant(): Instant = Instant.now(clock)

    /**
     * Returns the current date in Kampala.
     */
    fun today(): LocalDate = LocalDate.now(zonedClock)

    /**
     * Returns the current date and time in Kampala.
     */
    fun nowDateTime(): LocalDateTime = LocalDateTime.now(zonedClock)

    /**
     * Returns the current time in Kampala.
     */
    fun nowTime(): LocalTime = LocalTime.now(zonedClock)
}