package com.unimib.oases.ui.screen.patient_registration.info

data class PatientInfoState(
    var name: String = "",
    var age: Int = 0,
    var sex: String = Sex.Unspecified.displayName,
    var village: String = "",
    var parish: String = "",
    var subCounty: String = "",
    var district: String = "",
    var nextOfKin: String = "",
    var contact: String = "",

    var nameError: String? = null,
    var ageError: String? = null,

    var isLoading: Boolean = false
)