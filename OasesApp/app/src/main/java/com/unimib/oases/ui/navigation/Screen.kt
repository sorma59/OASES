package com.unimib.oases.ui.navigation

sealed class Screen(val route: String) {
    // screen pages
    data object LoginScreen: Screen("login_screen")
    data object HomeScreen: Screen("home_screen")
    data object RegistrationScreen: Screen("registration_screen")
    data object AdminScreen: Screen("admin_screen")
    data object SendPatient : Screen("send_patient")
    data object PairDevice : Screen("pair_device")
    data object MedicalVisitScreen: Screen("medical_visit_screen")


}