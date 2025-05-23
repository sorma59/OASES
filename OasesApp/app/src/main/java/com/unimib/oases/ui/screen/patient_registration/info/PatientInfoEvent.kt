package com.unimib.oases.ui.screen.patient_registration.info

sealed class PatientInfoEvent {

    data class NameChanged(val name: String): PatientInfoEvent()
    data class AgeChanged(val age: Int): PatientInfoEvent()
    data class SexChanged(val sex: String): PatientInfoEvent()
    data class VillageChanged(val village: String): PatientInfoEvent()
    data class ParishChanged(val parish: String): PatientInfoEvent()
    data class SubCountyChanged(val subCounty: String): PatientInfoEvent()
    data class DistrictChanged(val district: String): PatientInfoEvent()
    data class NextOfKinChanged(val nextOfKin: String): PatientInfoEvent()
    data class ContactChanged(val contact: String): PatientInfoEvent()

    object Submit: PatientInfoEvent()

}