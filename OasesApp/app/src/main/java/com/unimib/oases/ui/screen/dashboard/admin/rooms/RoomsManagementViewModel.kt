package com.unimib.oases.ui.screen.dashboard.admin.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.usecase.RoomUseCase
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
class RoomsManagementViewModel @Inject constructor(
    private val useCases: RoomUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var getRoomsJob: Job? = null

    private val _state = MutableStateFlow(RoomManagementState())
    val state: StateFlow<RoomManagementState> = _state

    private var undoRoom: Room? = null
    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    init {
        getRooms()
    }

    fun onEvent(event: RoomsManagementEvent) {
        when (event) {
            is RoomsManagementEvent.Click -> {
                _state.update {
                    it.copy(
                        room = event.value
                    )
                }
            }

            is RoomsManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteRoom(event.value)

                    undoRoom = event.value
                }
            }

            is RoomsManagementEvent.EnteredRoomName -> {
                _state.update {
                    it.copy(
                        room = it.room.copy(
                            name = event.value
                        )
                    )
                }
            }

            RoomsManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.addRoom(undoRoom ?: return@launch)
                    undoRoom = null
                }
            }

            RoomsManagementEvent.SaveRoom -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.update {
                            it.copy(isLoading = true)
                        }

                        useCases.addRoom(_state.value.room)

                        _state.update{
                            it.copy(
                                isLoading = false,
                                room = Room(
                                    name = "",
                                )
                            )
                        }
                        // _eventFlow.emit(UiEvent.SaveUser) // I emit it into the screen then
                        // in the screen we handle it and we go back to the list

                    } catch (_: Exception) {
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
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
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                rooms = resource.data,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = resource.message,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.NotFound -> {
                        _state.update {
                            it.copy(
                                rooms = emptyList(),
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




