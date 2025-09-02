package com.unimib.oases.ui.navigation

sealed class Screen(val route: String) {
    // screen pages
//    data object LoginScreen: Screen("login_screen")
    data object HomeScreen: Screen("home_screen")
    data object RegistrationScreen: Screen("registration_screen")
    data object AdminScreen: Screen("admin_screen")
    data object UserManagementScreen: Screen("user_management_screen")
    data object VitalSignsManagementScreen: Screen("vital_signs_management_screen")
    data object DiseaseManagementScreen: Screen("disease_management_screen")
    data object SendPatient : Screen("send_patient")
    data object PairDevice : Screen("pair_device")
    data object MedicalVisitScreen: Screen("medical_visit_screen")
    data object PatientDashboardScreen: Screen("patient_dashboard_screen")
    data object ViewPatientDetailsScreen: Screen("view_patient_details_screen")
}