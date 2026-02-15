package com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.repository.VitalSignRepository
import com.unimib.oases.domain.usecase.GetVitalSignPrecisionUseCase
import com.unimib.oases.domain.usecase.SaveVisitVitalSignsUseCase
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.util.snackbar.SnackbarData
import com.unimib.oases.ui.util.snackbar.SnackbarType
import com.unimib.oases.util.Outcome
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
class VitalSignsFormViewModel @Inject constructor(
    private val vitalSignsRepository: VitalSignRepository,
    private val getVitalSignPrecisionUseCase: GetVitalSignPrecisionUseCase,
    private val saveVitalSignsUseCase: SaveVisitVitalSignsUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update {
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    val coroutineContext = ioDispatcher + errorHandler

    private val route: Route.VitalSigns = savedStateHandle.toRoute()

    private val _state = MutableStateFlow(
        VitalSignsFormState(
            route.visitId
        )
    )
    val state = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    init {
        refreshVitalSigns()
    }

    fun onEvent(event: VitalSignsFormEvent) {
        when (event) {
            VitalSignsFormEvent.Retry -> {
                refreshVitalSigns()
            }

            is VitalSignsFormEvent.ValueChanged -> {
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

            is VitalSignsFormEvent.Save -> {
                viewModelScope.launch(coroutineContext) {
                    saveVitalSigns()
                }
            }

            VitalSignsFormEvent.Cancel -> {
                viewModelScope.launch(coroutineContext) {
                    navigationEventsChannel.send(
                        NavigationEvent.NavigateBack
                    )
                }
            }

            is VitalSignsFormEvent.ReattemptSaving -> {
                onEvent(VitalSignsFormEvent.Save)
            }

        }
    }

    private fun refreshVitalSigns() {
        viewModelScope.launch(coroutineContext) {
            loadVitalSigns()
        }
    }

    fun getPrecisionFor(name: String) = getVitalSignPrecisionUseCase(name)

    private suspend fun loadVitalSigns() {

        _state.update {
            it.copy(
                isLoading = true
            )
        }

        val vitalSigns = vitalSignsRepository
            .getAllVitalSigns()
            .firstSuccess()

        val newStates = vitalSigns.map { vitalSign -> // More efficient mapping
            PatientVitalSignState(vitalSign.name, vitalSign.acronym, vitalSign.unit)
        }
        _state.update {
            it.copy(
                vitalSigns = newStates,
                isLoading = false
            )
        }
    }

    private suspend fun saveVitalSigns(){

        _state.update {
            it.copy(
                isLoading = true
            )
        }

        val result = saveVitalSignsUseCase(state.value)

        _state.update {
            it.copy(
                isLoading = false
            )
        }

        when (result) {
            is Outcome.Error -> uiEventsChannel.send(
                UiEvent.ShowSnackbar(
                    snackbarData = SnackbarData(
                        message = "Save was unsuccessful",
                        type = SnackbarType.ERROR,
                        actionLabel = "Try again",
                        withDismissAction = true,
                        onAction = { onEvent(VitalSignsFormEvent.ReattemptSaving) }
                    )
                )
            )
            is Outcome.Success -> navigationEventsChannel.send(NavigationEvent.NavigateBack)
        }
    }

    fun getVisitVitalSigns(): List<VisitVitalSign> {
        val timestamp = System.currentTimeMillis().toString()
        return state.value.vitalSigns
            .filter { it.value.isNotBlank() }
            .map {
                VisitVitalSign(
                    visitId = state.value.visitId,
                    vitalSignName = it.name,
                    timestamp = timestamp,
                    value = it.value.toDouble()
                )
            }
    }
}