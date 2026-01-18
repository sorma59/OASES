package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.domain.usecase.GetCurrentVisitUseCase
import com.unimib.oases.domain.usecase.GetVitalSignPrecisionUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import com.unimib.oases.domain.usecase.VitalSignUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.firstSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalSignsViewModel @Inject constructor(
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val getVitalSignPrecisionUseCase: GetVitalSignPrecisionUseCase,
    private val visitVitalSignsUseCases: VisitVitalSignsUseCase,
    private val vitalSignsUseCases: VitalSignUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message
            )
        }
    }
    
    val coroutineContext = ioDispatcher + errorHandler

    private val route: Route.VitalSigns = savedStateHandle.toRoute()
    private val _state = MutableStateFlow(
        VitalSignsState(
            route.patientId
        )
    )
    val state = _state.asStateFlow()
    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    init {
        refreshVitalSigns()
    }

    fun onEvent(event: VitalSignsEvent) {
        when (event) {
            VitalSignsEvent.Retry -> {
                refreshVitalSigns()
            }
            is VitalSignsEvent.ValueChanged -> {
                _state.update {
                    it.copy(
                        vitalSigns = it.vitalSigns.map { vitalSign ->
                            if (vitalSign.name == event.vitalSign) {
                                vitalSign.copy(value = event.value)
                            } else {
                                vitalSign
                            }
                        }
                    )
                }
            }
        }
    }

    private fun refreshVitalSigns(){
        viewModelScope.launch(coroutineContext) {
            _state.update { it.copy(error = null, isLoading = true) }
            loadVitalSigns()
            val visit = getCurrentVisitUseCase(state.value.patientId)
            if (state.value.error == null)
                loadVisitVitalSigns(visit.id)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadVitalSigns() {
        val vitalSigns = vitalSignsUseCases
            .getVitalSigns()
            .firstSuccess()
        val newStates = vitalSigns.map { vitalSign -> // More efficient mapping
            PatientVitalSignState(vitalSign.name, vitalSign.acronym, vitalSign.unit)
        }
        _state.update {
            it.copy(
                vitalSigns = newStates
            )
        }
    }

    private suspend fun loadVisitVitalSigns(visitId: String) {

        val vitalSigns = visitVitalSignsUseCases
            .getVisitVitalSigns(visitId)
            .firstSuccess()
        val visitVitalSignsDbMap = vitalSigns.associateBy { it.vitalSignName }

        _state.update { currentState ->
            val updatedVitalSignsList =
                currentState.vitalSigns.map { uiState ->

                    val visitSpecificVitalSignData = visitVitalSignsDbMap[uiState.name]

                    if (visitSpecificVitalSignData != null) {
                        val precision = getPrecisionFor(uiState.name)

                        check(precision != null) {
                            "Precision for ${uiState.name} not found"
                        }

                        val value = when (precision) {
                            NumericPrecision.INTEGER -> visitSpecificVitalSignData.value.toInt().toString()
                            NumericPrecision.FLOAT -> visitSpecificVitalSignData.value.toString()
                        }

                        uiState.copy(value = value)
                    } else {
                        uiState
                    }
                }
            currentState.copy(vitalSigns = updatedVitalSignsList)
        }
    }

    fun getPrecisionFor(name: String) = getVitalSignPrecisionUseCase(name)
}