package com.unimib.oases.ui.screen.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.ui.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
): ViewModel() {

    private val _navEvents = MutableSharedFlow<NavigationEvent>()
    val navEvents: SharedFlow<NavigationEvent> = _navEvents.asSharedFlow()

    fun navigateTo(route: String) {
        viewModelScope.launch {
            _navEvents.emit(NavigationEvent.Navigate(route))
        }
    }

    fun onNavEvent(event: NavigationEvent){
        viewModelScope.launch { _navEvents.emit(event) }
    }

}