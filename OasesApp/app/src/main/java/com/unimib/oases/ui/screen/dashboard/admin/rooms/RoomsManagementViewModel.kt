package com.unimib.oases.ui.screen.dashboard.admin.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.AgeSpecificity.Companion.fromAgeSpecificityDisplayName
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.SexSpecificity
import com.unimib.oases.domain.model.SexSpecificity.Companion.fromSexSpecificityDisplayName
import com.unimib.oases.domain.usecase.RoomUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomsManagementViewModel @Inject constructor(
    private val useCases: RoomUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {


    private var getRoomsJob: Job? = null

    private val _state = MutableStateFlow(RoomManagementState())
    val state: StateFlow<RoomManagementState> = _state

    private var undoRoom: Room? = null
    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message,
                isLoading = false
            )
        }
    }


    sealed class UiEvent {
        // all events that gonna happen when we need to screen to display something and pass data back to the screen
        data class showSnackbar(val message: String) : UiEvent()
        object SaveUser : UiEvent()
        object Back : UiEvent()
    }


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RoomsManagementEvent) {
        when (event) {
            is RoomsManagementEvent.Click -> {
                _state.update{
                    _state.value.copy(
                        room = event.value
                    )
                }
            }

            is RoomsManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteRoom(event.value)
                    getRooms()
                    undoRoom = event.value
                }
            }

            is RoomsManagementEvent.EnteredRoomName -> {
                _state.update{
                    _state.value.copy(
                        room = _state.value.room.copy(
                            name = event.value
                        )
                    )
                }
            }



            RoomsManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.addRoom(undoRoom ?: return@launch)
                    undoRoom = null
                    getRooms()
                }
            }



            RoomsManagementEvent.SaveRoom -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.update{
                            _state.value.copy(isLoading = true)
                        }

                        useCases.addRoom(_state.value.room)

                        _state.update{
                            _state.value.copy(isLoading = false,
                                room = Room(
                                    name = "",
                                )
                            )
                        }
                        // _eventFlow.emit(UiEvent.SaveUser) // I emit it into the screen then
                        // in the screen we handle it and we go back to the list

                    } catch (e: Exception) {
                        _state.update{
                            _state.value.copy(
                                isLoading = false
                            )
                        }
                        _eventFlow.emit(
                            UiEvent.showSnackbar(
                                message = "Error adding room " + e.message
                            )
                        )
                    }
                }
            }
        }
    }


    fun getRooms() {
        getRoomsJob?.cancel()

        getRoomsJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = useCases.getRooms()

            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            rooms = resource.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = resource.message,
                        )
                    }
                }
            }
        }
    }

}




