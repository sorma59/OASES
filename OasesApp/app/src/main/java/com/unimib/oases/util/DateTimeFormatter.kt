package com.unimib.oases.util

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateTimeFormatter {

    val hoursAndMinutesFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    /**
     * Calculates age based on a birth date string.
     *
     * @param birthDateString The birth date as a String.
     * @param formatter The DateTimeFormatter to parse the birthDateString.
     *                  Defaults to ISO_LOCAL_DATE (yyyy-MM-dd).
     * @return The age in years, or null if the birthDateString is invalid or cannot be parsed.
     */
    fun calculateAgeInMonths(birthDateString: String, date: LocalDate = LocalDate.now(), formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")): Int? {
        return try {
            val birthDate = LocalDate.parse(birthDateString, formatter)
            val period = Period.between(birthDate, date)
            (period.years * 12) + period.months
        } catch (e: DateTimeParseException) {
            // Handle cases where the date string is not in the expected format
            println("Error parsing birth date '$birthDateString': ${e.message}")
            null
        } catch (e: Exception) {
            // Handle any other unexpected errors
            println("An unexpected error occurred while calculating age for '$birthDateString': ${e.message}")
            null
        }
    }

    fun calculateBirthDate(ageInMonths: Int, formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")): String? {
        if (ageInMonths < 0)
            return null
        val currentDate = LocalDate.now()
        val birthDate = currentDate.minusMonths(ageInMonths.toLong())
        return birthDate.format(formatter)
    }
}