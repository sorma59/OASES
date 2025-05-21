package com.unimib.oases.ui.screen.admin_screen.vital_signs_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.VitalSign
import com.unimib.oases.domain.usecase.DiseaseUseCase
import com.unimib.oases.domain.usecase.VitalSignUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalSignManagementViewModel @Inject constructor(
    private val useCases: VitalSignUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {


    private var getVitalSignsJob: Job? = null

    private val _state = MutableStateFlow(VitalSignManagementState())
    val state: StateFlow<VitalSignManagementState> = _state

    private var undoVitalSign: VitalSign? = null
    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }


    sealed class UiEvent {
        // all events that gonna happen when we need to screen to display something and pass data back to the screen
        data class showSnackbar(val message: String) : UiEvent()
    }


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: VitalSignManagementEvent) {
        when (event) {
            is VitalSignManagementEvent.Click -> {
                _state.value = _state.value.copy(
                    vitalSign = event.value
                )
            }

            is VitalSignManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteVitalSign(event.value)
                    getVitalSigns()
                    undoVitalSign = event.value
                }
            }

            is VitalSignManagementEvent.EnteredVitalSignName -> {
                _state.value = _state.value.copy(
                    vitalSign = _state.value.vitalSign.copy(
                        name = event.value
                    )
                )
            }


            VitalSignManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.addVitalSign(undoVitalSign ?: return@launch)
                    undoVitalSign = null
                    getVitalSigns()
                }
            }



            VitalSignManagementEvent.SaveVitalSign -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.value = _state.value.copy(isLoading = true)

                        useCases.addVitalSign(_state.value.vitalSign)

                        _state.value = _state.value.copy(isLoading = false,
                            vitalSign = state.value.vitalSign.copy(
                                name = ""
                            ))
                        // _eventFlow.emit(UiEvent.SaveUser) // I emit it into the screen then
                        // in the screen we handle it and we go back to the list

                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            isLoading = false
                        )
                        _eventFlow.emit(
                            UiEvent.showSnackbar(
                                message = e.message ?: "ERROR"
                            )
                        )
                    }
                }
            }


        }

    }


    fun getVitalSigns() {
        getVitalSignsJob?.cancel()



        getVitalSignsJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = useCases.getVitalSigns()

            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            vitalSigns = resource.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = resource.message,
                        )
                    }

                    is Resource.None -> {}
                }
            }
        }
    }

}




