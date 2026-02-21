package com.unimib.oases.ui.screen.medical_visit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalVisitViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val args = savedStateHandle.toRoute<Route.MedicalVisit>()

    private val _state = MutableStateFlow(
        MedicalVisitState(
            patientId = args.patientId,
        )
    )
    val state: StateFlow<MedicalVisitState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    fun onEvent(event: MedicalVisitEvent) {
        when (event) {
            is MedicalVisitEvent.ComplaintClicked -> {
                viewModelScope.launch {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.MainComplaint(
                                patientId = state.value.patientId,
                                mainComplaintId = event.complaintId
                            )
                        )
                    )
                }
            }
        }
    }
}