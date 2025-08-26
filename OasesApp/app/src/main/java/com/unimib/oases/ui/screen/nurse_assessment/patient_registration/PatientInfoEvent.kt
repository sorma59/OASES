package com.unimib.oases.ui.screen.nurse_assessment.patient_registration

sealed class PatientInfoEvent {

    data class NameChanged(val name: String): PatientInfoEvent()
    data class BirthDateChanged(val birthDate: String): PatientInfoEvent()
    data class BirthDateComputed(val birthDate: String): PatientInfoEvent()
    data class AgeChanged(val ageInMonths: Int): PatientInfoEvent()
    data class AgeComputed(val ageInMonths: Int): PatientInfoEvent()
    data class SexChanged(val sex: String): PatientInfoEvent()
    data class VillageChanged(val village: String): PatientInfoEvent()
    data class ParishChanged(val parish: String): PatientInfoEvent()
    data class SubCountyChanged(val subCounty: String): PatientInfoEvent()
    data class DistrictChanged(val district: String): PatientInfoEvent()
    data class NextOfKinChanged(val nextOfKin: String): PatientInfoEvent()
    data class ContactChanged(val contact: String): PatientInfoEvent()

    data object Retry : PatientInfoEvent()
    data object NextButtonPressed : PatientInfoEvent()
    data object ConfirmSubmission : PatientInfoEvent()
}