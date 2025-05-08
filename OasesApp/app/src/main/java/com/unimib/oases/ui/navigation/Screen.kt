package com.unimib.oases.ui.navigation

sealed class Screen(val route: String) {
    // screen pages
    object LoginScreen: Screen("login_screen")
    object HomeScreen: Screen("home_screen")
    object RegistrationScreen: Screen("registration_screen")
    object AdminScreen: Screen("admin_screen")
    object MedicalVisitScreen: Screen("medical_visit_screen")


}