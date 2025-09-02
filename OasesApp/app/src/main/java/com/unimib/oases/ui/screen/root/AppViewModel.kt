package com.unimib.oases.ui.screen.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.ui.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
): ViewModel() {

    private val _navEvents = MutableStateFlow<NavigationEvent?>(null)
    val navEvents: StateFlow<NavigationEvent?> = _navEvents.asStateFlow()

    fun navigateTo(route: String) {
        viewModelScope.launch {
            _navEvents.emit(NavigationEvent.Navigate(route))
        }
    }

    fun onNavEvent(event: NavigationEvent){
        _navEvents.value = event
    }

}