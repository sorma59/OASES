package com.unimib.oases.ui.screen.admin_screen.diseases_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.usecase.DiseaseUseCase
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
class DiseaseManagementViewModel @Inject constructor(
    private val useCases: DiseaseUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {


    private var getUsersJob: Job? = null

    private val _state = MutableStateFlow(DiseaseManagementState())
    val state: StateFlow<DiseaseManagementState> = _state

    private var undoDisease: Disease? = null
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
        object SaveUser : UiEvent()
        object Back : UiEvent()
    }


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: DiseaseManagementEvent) {
        when (event) {
            is DiseaseManagementEvent.Click -> {
                _state.value = _state.value.copy(
                    disease = event.value
                )
            }

            is DiseaseManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteDisease(event.value)
                    getDiseases()
                    undoDisease = event.value

                }
            }

            is DiseaseManagementEvent.EnteredDiseaseName -> {
                _state.value = _state.value.copy(
                    disease = _state.value.disease.copy(
                        name = event.value
                    )
                )
            }


            DiseaseManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.addDisease(undoDisease ?: return@launch)
                    undoDisease = null
                    getDiseases()
                }
            }



            DiseaseManagementEvent.SaveDisease -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.value = _state.value.copy(isLoading = true)

                        useCases.addDisease(_state.value.disease)

                        _state.value = _state.value.copy(isLoading = false,
                            disease = state.value.disease.copy(
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


    fun getDiseases() {
        getUsersJob?.cancel()



        getUsersJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = useCases.getDiseases()

            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            diseases = resource.data ?: emptyList(),
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




