package com.unimib.oases.ui.screen.admin_screen.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.local.model.User
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.UserUseCase
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
class UserManagementViewModel @Inject constructor(
    private val useCases: UserUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {


    private var getUsersJob: Job? = null

    private val _state = MutableStateFlow(UserManagementState())
    val state: StateFlow<UserManagementState> = _state

    private var undoUser: User? = null
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

    fun onEvent(event: UserManagementEvent) {
        when (event) {
            is UserManagementEvent.Click -> {
                _state.value = _state.value.copy(
                    user = event.value
                )
            }

            is UserManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteUser(event.value)
                    getUsers()
                    undoUser = event.value

                }
            }

            is UserManagementEvent.EnteredUsername -> {
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        username = event.value
                    )
                )
            }

            is UserManagementEvent.EnteredPassword -> {
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        pwHash = event.value
                    )
                )
            }

            UserManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.createUser(undoUser ?: return@launch)
                    undoUser = null
                    getUsers()
                }
            }


            is UserManagementEvent.SelectedRole -> {
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        role = event.value
                    )
                )
            }

            UserManagementEvent.SaveUser -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.value = _state.value.copy(isLoading = true)

                        useCases.createUser(_state.value.user)

                        _state.value = _state.value.copy(isLoading = false, user = state.value.user.copy(
                            username = "",
                            pwHash = ""
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


    fun getUsers() {
        getUsersJob?.cancel()



        getUsersJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = useCases.getUsers()

            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            users = resource.data ?: emptyList(),
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




