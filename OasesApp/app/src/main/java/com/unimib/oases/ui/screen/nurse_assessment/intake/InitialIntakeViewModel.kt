package com.unimib.oases.ui.screen.nurse_assessment.intake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.util.snackbar.SnackbarData
import com.unimib.oases.ui.util.snackbar.SnackbarType
import com.unimib.oases.util.DateAndTimeUtils
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstNullableSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialIntakeViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val visitRepository: VisitRepository,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow(InitialIntakeState())
    val state: StateFlow<InitialIntakeState> = _state.asStateFlow()

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val uiEventsChannel = Channel<UiEvent>()
    val uiEvents = uiEventsChannel.receiveAsFlow()

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update {
            _state.value.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    private val coroutineContext = dispatcher + errorHandler

    init {
        getPatients()
    }

    fun onEvent(event: InitialIntakeEvent) {
        when (event) {
            InitialIntakeEvent.NewButtonClicked -> {
                viewModelScope.launch {
                    navigationEventsChannel.send(
                        NavigationEvent.PopAndNavigate(
                            Route.PatientRegistration
                        )
                    )
                }
            }

            InitialIntakeEvent.ReturnButtonClicked -> {
                _state.update {
                    it.copy(isReturn = true)
                }
            }

            is InitialIntakeEvent.PatientClicked -> {

                viewModelScope.launch(coroutineContext) {

                    visitRepository.getCurrentVisit(event.patient.id).firstNullableSuccess()?.let {
                        uiEventsChannel.send(
                            UiEvent.ShowSnackbar(
                                snackbarData = SnackbarData(
                                    message = "This patient already has an open visit",
                                    type = SnackbarType.ERROR
                                )
                            )
                        )
                    } ?: run {

                        val visit = Visit(
                            patientId = event.patient.id,
                            arrivalTime = DateAndTimeUtils.getCurrentTime(),
                            date = DateAndTimeUtils.getCurrentDate()
                        )
                        patientRepository.addPatientAndCreateVisit(
                            event.patient, visit
                        )

                        uiEventsChannel.send(
                            UiEvent.ShowSnackbar(
                                snackbarData = SnackbarData(
                                    message = "New visit created",
                                    type = SnackbarType.SUCCESS
                                )
                            )
                        )

                        navigationEventsChannel.send(
                            NavigationEvent.PopAndNavigate(
                                Route.PatientDashboard(
                                    patientId = event.patient.id,
                                    visitId = visit.id
                                )
                            )
                        )

//                        when (val result = patientRepository.addPatient(event.patient)) {
//
//                            is Outcome.Error -> {
//                                _state.update {
//                                    it.copy(error = result.message)
//                                }
//                            }
//
//                            is Outcome.Success<String> -> {
//
//                                when (val visit = createReturnVisitUseCase(event.patient.id)) {
//                                    is Outcome.Error -> {
//                                        _state.update {
//                                            it.copy(error = visit.message)
//                                        }
//                                    }
//
//                                    is Outcome.Success<String> -> {
//
//                                        uiEventsChannel.send(
//                                            UiEvent.ShowSnackbar(
//                                                snackbarData = SnackbarData(
//                                                    message = "New visit created",
//                                                    type = SnackbarType.SUCCESS
//                                                )
//                                            )
//                                        )
//
//                                        navigationEventsChannel.send(
//                                            NavigationEvent.PopAndNavigate(
//                                                Route.PatientDashboard(
//                                                    patientId = event.patient.id,
//                                                    visitId = result.data
//                                                )
//                                            )
//                                        )
//                                    }
//                                }
//
//                            }
//                        }


                    }
                }
            }
        }
    }

    // ----------------------Patients-------------------------------

    fun getPatients() {
        viewModelScope.launch(coroutineContext) {

            patientRepository.getHistoryPatients()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _state.update {
                                _state.value.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Resource.Success -> {
                            _state.update {
                                _state.value.copy(
                                    patients = resource.data,
                                    isLoading = false
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update {
                                _state.value.copy(
                                    error = resource.message,
                                    isLoading = false
                                )
                            }
                        }

                        is Resource.NotFound -> {
                            _state.update {
                                it.copy(
                                    patients = emptyList(),
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