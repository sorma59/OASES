package com.unimib.oases.ui.screen.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.GetPatientsWithVisitInfoUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPatientsWithVisitInfoUseCase: GetPatientsWithVisitInfoUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
): ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    private val coroutineContext = dispatcher + errorHandler

    init {
        getPatientsWithVisitInfo()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.ToastShown -> {
                _state.update{
                    _state.value.copy(
                        toastMessage = null
                    )
                }
            }

            HomeScreenEvent.AddButtonClicked -> {
                viewModelScope.launch {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.PatientRegistration
                        )
                    )
                }
            }
            is HomeScreenEvent.PatientItemClicked -> {
                viewModelScope.launch {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.PatientDashboard(event.ids.patientId, event.ids.visitId)
                        )
                    )
                }
            }
        }
    }

    // ----------------------Patients-------------------------------

    fun getPatientsWithVisitInfo() {
        viewModelScope.launch(coroutineContext) {

            getPatientsWithVisitInfoUseCase()
                .collect { resource ->
                    Log.d("Prova", "${resource.javaClass}")
                    when (resource) {
                        is Resource.Loading -> {
                            _state.update{
                                _state.value.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Resource.Success -> {
                            Log.d("Prova", "${resource.data}")
                            _state.update{
                                _state.value.copy(
                                    patientsWithVisitInfo = resource.data,
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
                                    patientsWithVisitInfo = emptyList(),
                                    error = resource.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    // ----------------Toasts----------------
    fun onToastMessageShown() {
        _state.update{
            _state.value.copy(toastMessage = null)
        }
    }
}