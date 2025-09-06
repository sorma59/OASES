package com.unimib.oases.ui.screen.medical_visit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MedicalVisitViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(
        MedicalVisitState( receivedId = savedStateHandle["patientId"]!! )
    )
    val state: StateFlow<MedicalVisitState> = _state.asStateFlow()

}