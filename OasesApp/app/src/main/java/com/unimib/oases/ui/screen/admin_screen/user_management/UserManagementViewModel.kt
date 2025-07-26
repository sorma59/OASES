package com.unimib.oases.ui.screen.admin_screen.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.local.model.User
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.SaveUserUseCase
import com.unimib.oases.domain.usecase.UserUseCase
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
class UserManagementViewModel @Inject constructor(
    private val useCases: UserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {


    private var getUsersJob: Job? = null

    private val _state = MutableStateFlow(UserManagementState())
    val state: StateFlow<UserManagementState> = _state

    private var undoUser: User? = null
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


//    private val _eventFlow = MutableSharedFlow<UiEvent>()
//    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: UserManagementEvent) {
        when (event) {
            is UserManagementEvent.UserClicked -> {
                _state.update{
                    _state.value.copy(
                        user = event.value
                    )
                }
            }

            is UserManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteUser(event.value)
                    getUsers()
                    undoUser = event.value
                }
            }

            is UserManagementEvent.EnteredUsername -> {
                _state.update{
                    _state.value.copy(
                        user = _state.value.user.copy(
                            username = event.value
                        ),
                        usernameError = null
                    )
                }
            }

            is UserManagementEvent.EnteredPassword -> {
                _state.update{
                    _state.value.copy(
                        user = _state.value.user.copy(
                            pwHash = event.value
                        ),
                        passwordError = null
                    )
                }
            }

            is UserManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.createUser(undoUser ?: return@launch)
                    undoUser = null
                    getUsers()
                }
            }


            is UserManagementEvent.SelectedRole -> {
                _state.update{
                    _state.value.copy(
                        user = _state.value.user.copy(
                            role = event.value
                        )
                    )
                }
            }

            is UserManagementEvent.SaveUser -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.update{
                            _state.value.copy(isLoading = true)
                        }

                        val result = saveUserUseCase(
                            _state.value.user.username,
                            _state.value.user.pwHash,
                            _state.value.user.role
                        )

                        when (result) {
                            is SaveUserUseCase.SaveUserUseCaseResult.Success -> {
                                _state.update {
                                    it.copy(
                                        toastMessage = "User created successfully",
                                        user = it.user.copy(
                                            username = "",
                                            pwHash = ""
                                        )
                                    )
                                }
                            }
                            is SaveUserUseCase.SaveUserUseCaseResult.RepositoryFailure -> {
                                _state.update { it.copy(toastMessage = result.errorMessage) }
                            }
                            is SaveUserUseCase.SaveUserUseCaseResult.UnknownError -> {
                                _state.update { it.copy(toastMessage = "Unknown error") }
                            }
                            is SaveUserUseCase.SaveUserUseCaseResult.ValidationFailure -> {
                                _state.update {
                                    it.copy(
                                        usernameError = result.usernameError,
                                        passwordError = result.passwordError,
                                    )
                                }
                            }
                        }
                        // _eventFlow.emit(UiEvent.SaveUser) // I emit it into the screen then
                        // in the screen we handle it and we go back to the list

                    } catch (e: Exception) {
                        _state.update{
                            _state.value.copy(
                                isLoading = false,
                                error = e.message
                            )
                        }
//                        _eventFlow.emit(
//                            UiEvent.showSnackbar(
//                                message = e.message ?: "ERROR"
//                            )
//                        )
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

            is UserManagementEvent.OnToastShown -> {
                _state.update {
                    _state.value.copy(
                        toastMessage = null
                    )
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
                        _state.update{
                            _state.value.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update{
                            _state.value.copy(
                                users = resource.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update{
                            _state.value.copy(
                                error = resource.message,
                            )
                        }
                    }

                    is Resource.None -> {}
                }
            }
        }
    }
}



