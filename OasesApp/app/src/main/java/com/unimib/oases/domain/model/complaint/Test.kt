package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.TestId.AbdominalXRayId
import com.unimib.oases.domain.model.complaint.TestId.AcidFastBacilliMicroscopyId
import com.unimib.oases.domain.model.complaint.TestId.BloodSmearForMalariaParasitesId
import com.unimib.oases.domain.model.complaint.TestId.ChestXRayId
import com.unimib.oases.domain.model.complaint.TestId.CompleteBloodCountId
import com.unimib.oases.domain.model.complaint.TestId.EchocardiographyId
import com.unimib.oases.domain.model.complaint.TestId.ElectrocardiogramId
import com.unimib.oases.domain.model.complaint.TestId.ElectrolytesTestsId
import com.unimib.oases.domain.model.complaint.TestId.HIVId
import com.unimib.oases.domain.model.complaint.TestId.LiverFunctionTestsId
import com.unimib.oases.domain.model.complaint.TestId.MalariaRapidDiagnosticTestId
import com.unimib.oases.domain.model.complaint.TestId.RapidBloodSugarId
import com.unimib.oases.domain.model.complaint.TestId.RenalFunctionTestsId
import com.unimib.oases.domain.model.complaint.TestId.StoolMicroscopyId
import com.unimib.oases.domain.model.complaint.TestId.TuberculosisGeneXpertId
import com.unimib.oases.domain.model.complaint.TestId.UrinalysisId
import com.unimib.oases.domain.model.complaint.TestId.VenousUltrasoundOfLowerLimbsId
import com.unimib.oases.util.StringFormatHelper.SnakeCaseString

private fun snakeCase(string: String) = SnakeCaseString.of(string)

sealed interface Test{
    val testId: TestId
    val acronym: String?
    val name: String
    val label: String

    val id: String
        get() = testId.value.string

    data class RapidBloodSugarTest(
        override val testId: TestId = RapidBloodSugarId,
        override val acronym: String = "RBS",
        override val name: String = "Rapid blood sugar",
        override val label: String = name
    ): Test

    data class CompleteBloodCountTest(
        override val testId: TestId = CompleteBloodCountId,
        override val acronym: String = "CBC",
        override val name: String = "Complete blood count",
        override val label: String = name
    ): Test

    data class MalariaRapidDiagnosticTest(
        override val testId: TestId = MalariaRapidDiagnosticTestId,
        override val acronym: String = "MRDT",
        override val name: String = "Malaria rapid diagnostic test",
        override val label: String = name
    ): Test

    data class BloodSmearForMalariaParasitesTest(
        override val testId: TestId = BloodSmearForMalariaParasitesId,
        override val acronym: String = "B/S",
        override val name: String = "Blood smear for malaria parasites",
        override val label: String = name
    ): Test

    data class ChestXRay(
        override val testId: TestId = ChestXRayId,
        override val acronym: String? = null,
        override val name: String = "Chest X-ray",
        override val label: String = name
    ): Test

    data class StoolMicroscopy(
        override val testId: TestId = StoolMicroscopyId,
        override val acronym: String = "STM",
        override val name: String = "Stool microscopy",
        override val label: String = name
    ): Test

    data class AbdominalXRay(
        override val testId: TestId = AbdominalXRayId,
        override val acronym: String = "AXR",
        override val name: String = "Abdominal X-ray",
        override val label: String = name
    ): Test

    data class HIVTest(
        override val testId: TestId = HIVId,
        override val acronym: String = "HIV",
        override val name: String = "HIV test",
        override val label: String = name
    ): Test

    data class TuberculosisGeneXpert(
        override val testId: TestId = TuberculosisGeneXpertId,
        override val acronym: String = "TB",
        override val name: String = "Tuberculosis GeneXpert",
        override val label: String = name
    ): Test

    data class AcidFastBacilliMicroscopy(
        override val testId: TestId = AcidFastBacilliMicroscopyId,
        override val acronym: String = "AFB",
        override val name: String = "Acid-fast bacilli microscopy",
        override val label: String = name
    ): Test

    data class Electrocardiogram(
        override val testId: TestId = ElectrocardiogramId,
        override val acronym: String = "ECG",
        override val name: String = "Electrocardiogram",
        override val label: String = name
    ): Test

    data class Echocardiography(
        override val testId: TestId = EchocardiographyId,
        override val acronym: String = "ECHO",
        override val name: String = "Echocardiography",
        override val label: String = name
    ): Test

    data class VenousUltrasoundOfLowerLimbs(
        override val testId: TestId = VenousUltrasoundOfLowerLimbsId,
        override val acronym: String = "CUS",
        override val name: String = "Venous ultrasound of lower limbs",
        override val label: String = name
    ): Test

    data class RenalFunctionTests(
        override val testId: TestId = RenalFunctionTestsId,
        override val acronym: String = "RFT",
        override val name: String = "Renal function tests",
        override val label: String = name
    ): Test

    data class ElectrolytesTests(
        override val testId: TestId = ElectrolytesTestsId,
        override val acronym: String? = null,
        override val name: String = "Electrolytes tests",
        override val label: String = name
    ): Test

    data class Urinalysis(
        override val testId: TestId = UrinalysisId,
        override val acronym: String = "UA",
        override val name: String = "Urinalysis",
        override val label: String = name
    ): Test

    data class LiverFunctionTests(
        override val testId: TestId = LiverFunctionTestsId,
        override val acronym: String = "LFT",
        override val name: String = "Liver function tests",
        override val label: String = name
    ): Test
}

sealed class TestId (
    val value: SnakeCaseString
){
    object RapidBloodSugarId: TestId(snakeCase("rapid_blood_sugar"))
    object CompleteBloodCountId: TestId(snakeCase("complete_blood_count"))
    object MalariaRapidDiagnosticTestId: TestId(snakeCase("malaria_rapid_diagnostic_test"))
    object BloodSmearForMalariaParasitesId: TestId(snakeCase("blood_smear_for_malaria_parasites"))
    object ChestXRayId: TestId(snakeCase("chest_x_ray"))
    object StoolMicroscopyId: TestId(snakeCase("stool_microscopy"))
    object AbdominalXRayId: TestId(snakeCase("abdominal_x_ray"))
    object HIVId: TestId(snakeCase("hiv"))
    object TuberculosisGeneXpertId: TestId(snakeCase("tuberculosis_genexpert"))
    object AcidFastBacilliMicroscopyId: TestId(snakeCase("acid_fast_bacilli_microscopy"))
    object ElectrocardiogramId: TestId(snakeCase("electrocardiogram"))
    object EchocardiographyId: TestId(snakeCase("echocardiography"))
    object VenousUltrasoundOfLowerLimbsId: TestId(snakeCase("venous_ultrasound_of_lower_limbs"))
    object RenalFunctionTestsId: TestId(snakeCase("renal_function_tests"))
    object ElectrolytesTestsId: TestId(snakeCase("electrolytes_tests"))
    object UrinalysisId: TestId(snakeCase("urinalysis"))
    object LiverFunctionTestsId: TestId(snakeCase("liver_function_tests"))
}