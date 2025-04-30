package com.unimib.oases.ui.util

sealed class Screen(val route: String) {
    // screen pages
    object LoginScreen: Screen("login_screen")
    object HomeScreen: Screen("home_screen")
    object RegistrationScreen: Screen("registration_screen")


}