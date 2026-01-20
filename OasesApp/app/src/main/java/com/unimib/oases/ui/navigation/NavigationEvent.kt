package com.unimib.oases.ui.navigation

sealed class NavigationEvent {
    data class Navigate(val route: Route) : NavigationEvent()
    data class PopAndNavigate(val route: Route): NavigationEvent()
    object NavigateBack : NavigationEvent()
    data class NavigateBackWithResult<T>(val key: String, val result: T): NavigationEvent()
}