package com.unimib.oases.ui.screen.dashboard.admin.vitalsigns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.VitalSign
import com.unimib.oases.domain.usecase.SaveVitalSignUseCase
import com.unimib.oases.domain.usecase.VitalSignUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalSignManagementViewModel @Inject constructor(
    private val useCases: VitalSignUseCase,
    private val saveVitalSignUseCase: SaveVitalSignUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {


    private var getVitalSignsJob: Job? = null

    private val _state = MutableStateFlow(VitalSignManagementState())
    val state: StateFlow<VitalSignManagementState> = _state

    private var undoVitalSign: VitalSign? = null
    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    init {
        getVitalSigns()
    }

    fun onEvent(event: VitalSignManagementEvent) {
        when (event) {
            is VitalSignManagementEvent.Click -> {
                _state.update{
                    _state.value.copy(
                        vitalSign = event.value
                    )
                }
            }

            is VitalSignManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteVitalSign(event.value)
                    undoVitalSign = event.value
                }
            }

            is VitalSignManagementEvent.EnteredVitalSignName -> {
                _state.update{
                    _state.value.copy(
                        vitalSign = _state.value.vitalSign.copy(
                            name = event.value
                        ),
                        nameError = null
                    )
                }
            }

            is VitalSignManagementEvent.EnteredVitalSignAcronym -> {
                _state.update{
                    _state.value.copy(
                        vitalSign = _state.value.vitalSign.copy(
                            acronym = event.value
                        ),
                        acronymError = null
                    )
                }
            }

            is VitalSignManagementEvent.EnteredVitalSignUnit -> {
                _state.update{
                    _state.value.copy(
                        vitalSign = _state.value.vitalSign.copy(
                            unit = event.value,
                        ),
                        unitError = null
                    )
                }
            }

            is VitalSignManagementEvent.SelectedPrecision -> {
                _state.update {
                    _state.value.copy(
                        vitalSign = _state.value.vitalSign.copy(
                            precision = event.value
                        )
                    )
                }
            }

            is VitalSignManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.addVitalSign(undoVitalSign ?: return@launch)
                    undoVitalSign = null
                }
            }

            is VitalSignManagementEvent.SaveVitalSign -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.update{
                            _state.value.copy(isLoading = true)
                        }

                        val result = saveVitalSignUseCase(
                            state.value.vitalSign.name,
                            state.value.vitalSign.acronym,
                            state.value.vitalSign.unit
                        )

                        when (result) {
                            is SaveVitalSignUseCase.SaveVitalSignUseCaseResult.Success -> {
                                _state.update{
                                    _state.value.copy(
                                        toastMessage = "Saved successfully",
                                        vitalSign = it.vitalSign.copy(
                                            name = "",
                                            acronym = "",
                                            unit = ""
                                        )
                                    )
                                }
                            }

                            is SaveVitalSignUseCase.SaveVitalSignUseCaseResult.ValidationFailure -> {
                                _state.update{
                                    _state.value.copy(
                                        nameError = result.nameError,
                                        acronymError = result.acronymError,
                                        unitError = result.unitError
                                    )
                                }
                            }

                            is SaveVitalSignUseCase.SaveVitalSignUseCaseResult.RepositoryFailure -> {
                                _state.update{
                                    _state.value.copy(
                                        toastMessage = "Failed: Repository error"
                                    )
                                }
                            }

                            is SaveVitalSignUseCase.SaveVitalSignUseCaseResult.UnknownError -> {
                                _state.update{
                                    _state.value.copy(
                                        toastMessage = "Failed: Unknown error"
                                    )
                                }
                            }
                        }
                        // _eventFlow.emit(UiEvent.SaveUser) // I emit it into the screen then
                        // in the screen we handle it and we go back to the list

                    } catch (e: Exception) {
                        _state.update{
                            _state.value.copy(
                                error = e.message
                            )
                        }
                    }
                    finally {
                        _state.update{
                            _state.value.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }

            is VitalSignManagementEvent.ToastShown -> {
                _state.update {
                    _state.value.copy(
                        toastMessage = null
                    )
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
                        _state.update{
                            _state.value.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update{
                            _state.value.copy(
                                vitalSigns = resource.data,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update{
                            _state.value.copy(
                                error = resource.message,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.NotFound -> {
                        _state.update {
                            it.copy(
                                vitalSigns = emptyList(),
                                error = resource.message,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}
