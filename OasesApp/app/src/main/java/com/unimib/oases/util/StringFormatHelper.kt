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

    fun formatDate(date: LocalDate, formatter: java.time.format.DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")): String {
        return date.format(formatter)
    }
}