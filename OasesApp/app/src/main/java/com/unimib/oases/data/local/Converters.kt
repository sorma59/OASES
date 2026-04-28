package com.unimib.oases.data.local

import android.util.Base64
import androidx.room.TypeConverter
import com.unimib.oases.data.local.model.DetailQuestionAnswer
import com.unimib.oases.data.local.model.FindingSnapshot
import com.unimib.oases.data.local.model.TreeAnswers
import com.unimib.oases.domain.model.QuestionAndAnswer
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.ui.screen.medical_visit.disposition.HomeTreatment
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
    fun fromSupportiveTherapyTextListToString(list: List<TherapyText>): String {
        return list.joinToString(separator = "_")
    }

    @TypeConverter
    fun fromStringToSupportiveTherapyTextList(string: String): List<TherapyText> {
        return if (string.isBlank()) emptyList() else string.split("_").map{ TherapyText(it) }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? =
        date?.toString() // ISO-8601 (yyyy-MM-dd)

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromTreeAnswersListToString(list: List<TreeAnswers>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToTreeAnswersList(value: String): List<TreeAnswers> {
        return if (value.isEmpty()) emptyList() else Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromDetailQuestionAnswerListToString(list: List<DetailQuestionAnswer>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToDetailQuestionAnswerList(value: String): List<DetailQuestionAnswer> {
        return if (value.isEmpty()) emptyList() else Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFindingSnapshotListToString(list: List<FindingSnapshot>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringToFindingSnapshotList(value: String): List<FindingSnapshot> {
        return if (value.isEmpty()) emptyList() else Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromHomeTreatments(homeTreatments: List<HomeTreatment>): String =
        homeTreatments.joinToString(",") { it.complaintId.id }

    @TypeConverter
    fun toHomeTreatments(value: String): List<HomeTreatment> =
        value.split(",")
            .filter { it.isNotBlank() }
            .mapNotNull { id ->
                when (id) {
                    ComplaintId.DIARRHEA.id -> HomeTreatment.Diarrhea
                    ComplaintId.DYSPNEA.id -> HomeTreatment.Dyspnea
                    ComplaintId.SEIZURES_OR_COMA.id -> HomeTreatment.SeizuresOrComa
                    ComplaintId.OTHER.id -> HomeTreatment.OtherComplaints
                    else -> null //TODO add other
                }
            }
}