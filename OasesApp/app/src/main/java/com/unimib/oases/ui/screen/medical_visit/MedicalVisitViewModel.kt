package com.unimib.oases.ui.screen.medical_visit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.domain.usecase.TranslateLatestVitalSignsToSymptomsUseCase
import com.unimib.oases.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalVisitViewModel @Inject constructor(
    private val translateLatestVitalSignsToSymptomsUseCase: TranslateLatestVitalSignsToSymptomsUseCase,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val args = savedStateHandle.toRoute<Route.MedicalVisit>()

    private val _state = MutableStateFlow(
        MedicalVisitState(
            patientId = args.patientId,
        )
    )
    val state: StateFlow<MedicalVisitState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    symptoms = translateLatestVitalSignsToSymptomsUseCase(_state.value.patientId)
                )
            }
        }
    }

}