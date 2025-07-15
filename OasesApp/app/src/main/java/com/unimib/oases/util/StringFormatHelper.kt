package com.unimib.oases.util

object StringFormatHelper {

    fun getAgeWithSuffix(ageInMonths: Int): String {
        return if (ageInMonths < 12) {
            "$ageInMonths mo"
        } else {
            "" + (ageInMonths / 12) + " yo"
        }
    }

}