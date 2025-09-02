package com.unimib.oases.ui.navigation

sealed class NavigationEvent {
    data class Navigate(val route: String) : NavigationEvent()
//    data class NavigateAfterLogin(val route: String) : NavigationEvent()
//    object NavigateToLogin : NavigationEvent()
    object NavigateBack : NavigationEvent()
}