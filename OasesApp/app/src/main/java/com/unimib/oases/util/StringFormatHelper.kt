package com.unimib.oases.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object StringFormatHelper {

    fun getAgeWithSuffix(ageInMonths: Int): String {
        return if (ageInMonths < 12) {
            "$ageInMonths mo"
        } else {
            "" + (ageInMonths / 12) + " yo"
        }
    }

    fun formatDate(date: LocalDate, formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")): String {
        return date.format(formatter)
    }

    private val snakeCaseRegex = Regex("^[a-z]+(_[a-z0-9]+)*$")

    /**
     * A value class representing a string that is guaranteed to be in snake_case format.
     *
     * Snake_case is a naming convention where words are separated by underscores,
     * and all letters are lowercase. For example, `my_variable_name`.
     *
     * Instances of this class can only be created through the `of` factory method,
     * which validates the input string against a regular expression for snake_case.
     *
     * @property string The underlying snake_case string.
     */
    @JvmInline
    value class SnakeCaseString private constructor(val string: String) {
        companion object {
            fun of(input: String): SnakeCaseString {
                require(snakeCaseRegex.matches(input)) {
                    "Invalid snake_case string: $input"
                }
                return SnakeCaseString(input)
            }
        }
    }
}