package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.TestId.AbdominalXRayId
import com.unimib.oases.domain.model.complaint.TestId.BloodSmearForMalariaParasitesId
import com.unimib.oases.domain.model.complaint.TestId.CompleteBloodCountId
import com.unimib.oases.domain.model.complaint.TestId.HIVId
import com.unimib.oases.domain.model.complaint.TestId.MalariaRapidDiagnosticTestId
import com.unimib.oases.domain.model.complaint.TestId.RapidBloodSugarId
import com.unimib.oases.domain.model.complaint.TestId.StoolMicroscopyId
import com.unimib.oases.util.StringFormatHelper.SnakeCaseString

private fun snakeCase(string: String) = SnakeCaseString.of(string)

sealed interface Test{
    val testId: TestId
    val acronym: String?
    val name: String
    val label: String

    val id: String
        get() = testId.value.string

    data class RapidBloodSugar(
        override val testId: TestId = RapidBloodSugarId,
        override val acronym: String = "RBS",
        override val name: String = "Rapid blood sugar",
        override val label: String = "Rapid blood sugar"
    ): Test

    data class CompleteBloodCount(
        override val testId: TestId = CompleteBloodCountId,
        override val acronym: String = "CBC",
        override val name: String = "Complete blood count",
        override val label: String = "Complete blood count"
    ): Test

    data class MalariaRapidDiagnosticTest(
        override val testId: TestId = MalariaRapidDiagnosticTestId,
        override val acronym: String = "MRDT",
        override val name: String = "Malaria rapid diagnostic test",
        override val label: String = "Malaria rapid diagnostic test"
    ): Test

    data class BloodSmearForMalariaParasites(
        override val testId: TestId = BloodSmearForMalariaParasitesId,
        override val acronym: String = "B/S",
        override val name: String = "Blood smear for malaria parasites",
        override val label: String = "Blood smear for malaria parasites"
    ): Test

    data class StoolMicroscopy(
        override val testId: TestId = StoolMicroscopyId,
        override val acronym: String = "STM",
        override val name: String = "Stool microscopy",
        override val label: String = "Stool microscopy"
    ): Test

    data class AbdominalXRay(
        override val testId: TestId = AbdominalXRayId,
        override val acronym: String = "AXR",
        override val name: String = "Abdominal X-ray",
        override val label: String = "Abdominal X-ray"
    ): Test

    data class HIV(
        override val testId: TestId = HIVId,
        override val acronym: String = "HIV",
        override val name: String = "HIV test",
        override val label: String = "HIV test"
    ): Test
}

sealed class TestId (
    val value: SnakeCaseString
){
    object RapidBloodSugarId: TestId(snakeCase("rapid_blood_sugar"))
    object CompleteBloodCountId: TestId(snakeCase("complete_blood_count"))
    object MalariaRapidDiagnosticTestId: TestId(snakeCase("malaria_rapid_diagnostic_test"))
    object BloodSmearForMalariaParasitesId: TestId(snakeCase("blood_smear_for_malaria_parasites"))
    object StoolMicroscopyId: TestId(snakeCase("stool_microscopy"))
    object AbdominalXRayId: TestId(snakeCase("abdominal_x_ray"))
    object HIVId: TestId(snakeCase("hiv"))
}