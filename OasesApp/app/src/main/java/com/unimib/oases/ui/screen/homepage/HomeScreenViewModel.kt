package com.unimib.oases.ui.screen.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    patientRepository: PatientRepository,
    private val useCases: PatientUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
): ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private var getPatientsJob: Job? = null
    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                errorMessage = e.message,
                isLoading = false
            )
        }
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
                            Screen.RegistrationScreen.route
                        )
                    )
                }
            }
            is HomeScreenEvent.PatientItemClicked -> {
                viewModelScope.launch {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Screen.PatientDashboardScreen.route + "/patientId=${event.patientId}"
                        )
                    )
                }
            }
        }
    }

    // ----------------------Patients-------------------------------

    private val receivedPatients: StateFlow<List<Patient>> =
        patientRepository
            .receivedPatients
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            receivedPatients.collect { patients ->
                _state.update{
                    _state.value.copy(receivedPatients = patients)
                }
            }
        }
    }

    init {
        getPatients()
    }

    fun getPatients() {
        getPatientsJob?.cancel()

        getPatientsJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = useCases.getPatients()

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
                                patients = resource.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update{
                            _state.value.copy(
                                errorMessage = resource.message,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.None -> {}
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