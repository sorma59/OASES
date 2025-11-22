package com.unimib.oases.ui.screen.nurse_assessment.demographics

sealed class DemographicsEvent {

    data class NameChanged(val name: String): DemographicsEvent()
    data class BirthDateChanged(val birthDate: String): DemographicsEvent()
//    data class BirthDateComputed(val birthDate: String): DemographicsEvent()
    data class AgeChanged(val ageInMonths: Int): DemographicsEvent()
//    data class AgeComputed(val ageInMonths: Int): DemographicsEvent()
    data class SexChanged(val sex: Sex): DemographicsEvent()
    data class VillageChanged(val village: String): DemographicsEvent()
    data class ParishChanged(val parish: String): DemographicsEvent()
    data class SubCountyChanged(val subCounty: String): DemographicsEvent()
    data class DistrictChanged(val district: String): DemographicsEvent()
    data class NextOfKinChanged(val nextOfKin: String): DemographicsEvent()
    data class ContactChanged(val contact: String): DemographicsEvent()
    data object EditButtonPressed: DemographicsEvent()
    data object DismissDialog: DemographicsEvent()
    data object ConfirmDialog: DemographicsEvent()
    data object Retry : DemographicsEvent()
    data object CancelButtonPressed : DemographicsEvent()
    data object NextButtonPressed : DemographicsEvent()
}