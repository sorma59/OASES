package com.unimib.oases.data.local

import android.util.Base64
import androidx.room.TypeConverter
import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.SupportiveTherapyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

object Converters {

    @TypeConverter
    fun fromByteArray(value: ByteArray?): String? {
        return value?.let { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    @TypeConverter
    fun toByteArray(value: String?): ByteArray? {
        return value?.let { Base64.decode(it, Base64.DEFAULT) }
    }

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        return if (value.isBlank()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromLabelledTestListToString(list: List<LabelledTest>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToLabelledTestList(value: String): List<LabelledTest> {
        return if (value.isEmpty()) emptyList() else Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromQuestionsAndAnswersListListToString(list: List<List<QuestionAndAnswer>>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToQuestionsAndAnswersListList(value: String): List<List<QuestionAndAnswer>> {
        return if (value.isEmpty()) emptyList() else Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromImmediateTreatmentListToString(list: List<ImmediateTreatment>): String {
        return list.joinToString(separator = "_")
    }

    @TypeConverter
    fun fromStringToImmediateTreatmentList(string: String): List<ImmediateTreatment> {
        return if (string.isBlank()) emptyList() else string.split("_").map{ ImmediateTreatment(it) }
    }

    @TypeConverter
    fun fromSupportiveTherapyTextListToString(list: List<SupportiveTherapyText>): String {
        return list.joinToString(separator = "_")
    }

    @TypeConverter
    fun fromStringToSupportiveTherapyTextList(string: String): List<SupportiveTherapyText> {
        return if (string.isBlank()) emptyList() else string.split("_").map{ SupportiveTherapyText("G$it") }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? =
        date?.toString() // ISO-8601 (yyyy-MM-dd)

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it) }


}