package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.usecase.ComputeSymptomsUseCase
import com.unimib.oases.domain.usecase.GetVitalSignPrecisionUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import com.unimib.oases.domain.usecase.VitalSignUseCase
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

//private fun VitalSignsViewModel.evaluateSymptomColor(
//    vitalLimits: Map<ComputeSymptomsUseCase.VitalKey, ComputeSymptomsUseCase.VitalRange<*>>?,
//    acronym: String?
//): Color {
//    if(vitalLimits == null || acronym == null){
//        val limits = vitalLimits.get(ComputeSymptomsUseCase.VitalKey.valueOf(acronym))
//    }
//}

@HiltViewModel
class VitalSignsViewModel @Inject constructor(
    private val visitRepository: VisitRepository,
    private val getVitalSignPrecisionUseCase: GetVitalSignPrecisionUseCase,
    private val visitVitalSignsUseCases: VisitVitalSignsUseCase,
    private val getPatientData: PatientRepository,
    private val computeSymptomsUseCase: ComputeSymptomsUseCase,
    private val vitalSignsUseCases: VitalSignUseCase,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }
    
    val coroutineContext = ioDispatcher + errorHandler

    private val route: Route.VitalSigns = savedStateHandle.toRoute()

    private val _state = MutableStateFlow(
        VitalSignsState(
            route.patientId,
            route.visitId
        )
    )
    val state = _state.asStateFlow()
    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    init {
        refreshVitalSigns()
    }

    fun onEvent(event: VitalSignsEvent) {
        when (event) {
            VitalSignsEvent.Retry -> {
                refreshVitalSigns()
            }

            is VitalSignsEvent.ValueChanged -> {
                _state.update {
                    it.copy(
                        vitalSigns = it.vitalSigns.map { vitalSign ->
                            if (vitalSign.name == event.vitalSign) {
                                vitalSign.copy(value = event.value)
                            } else {
                                vitalSign
                            }
                        }
                    )
                }
            }

            is VitalSignsEvent.Save -> {
                viewModelScope.launch(coroutineContext) {
                    addVitalSigns()
                    refreshVitalSigns()
                }
            }

            VitalSignsEvent.AddButtonClicked -> {
                viewModelScope.launch(coroutineContext) {
                    navigationEventsChannel.send(
                        NavigationEvent.Navigate(
                            Route.VitalSignsForm(
                                state.value.patientId,
                                state.value.visitId
                            )
                        )
                    )
                }
            }
        }
    }

    private fun refreshVitalSigns() {
        viewModelScope.launch(coroutineContext) {
            loadVitalSigns()
            val visit = visitRepository.getVisitById(state.value.visitId).firstSuccess()
            _state.update {
                it.copy(visitDate = visit.date.format(DateTimeFormatter.ISO_DATE))
            }
            if (state.value.error == null)
                loadVisitVitalSigns(state.value.visitId)
        }
    }

    private suspend fun loadVitalSigns() {
        val vitalSigns = vitalSignsUseCases
            .getVitalSigns()
            .firstSuccess()

        val newStates = vitalSigns.map { vitalSign -> // More efficient mapping
            PatientVitalSignState(vitalSign.name, vitalSign.acronym, vitalSign.unit)
        }
        _state.update {
            it.copy(
                vitalSigns = newStates
            )
        }
    }

    private suspend fun addVitalSigns(){
        visitVitalSignsUseCases.addVisitVitalSigns(state.value.toVisitVitalSigns(state.value.visitId))

        navigationEventsChannel.send(NavigationEvent.NavigateBack)
    }

    private suspend fun loadVisitVitalSigns(visitId: String) {

        val result = visitVitalSignsUseCases.getVisitVitalSigns(visitId)

        val patientData = getPatientData.getPatientById(state.value.patientId).firstSuccess()

        val vitalLimits = computeSymptomsUseCase.getVitalLimits(patientData.category, patientData.ageInMonths)

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
                            visitVitalSigns = resource.data.map { visitVitalSign ->

                                val acronym =
                                    state.value.vitalSigns.firstOrNull { it.name == visitVitalSign.vitalSignName }?.acronym

                                val vitalLimit = vitalLimits?.filter { it -> it.key.name.equals(acronym, true)}?.values?.firstOrNull()


                                val precision = getPrecisionFor(visitVitalSign.vitalSignName)

                                check(precision != null) {
                                    "Precision for ${visitVitalSign.vitalSignName} not found"
                                }

                                var value: Number

                                var symptomColor: Color? = null


                               when (precision) {
                                    NumericPrecision.INTEGER -> {
                                        value = visitVitalSign.value.toInt()
                                        symptomColor = evaluateSymptomColor(value, vitalLimit)
                                    }

                                    NumericPrecision.FLOAT -> {
                                        value = visitVitalSign.value
                                        symptomColor = evaluateSymptomColorDouble(value, vitalLimit)
                                    }
                               }



                                VisitVitalSignUI(
                                    name = visitVitalSign.vitalSignName,
                                    value = value.toString(),
                                    timestamp = visitVitalSign.timestamp,
                                    color = symptomColor

                                )
                            },
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
                            visitVitalSigns = emptyList(),
                            isLoading = false
                        )
                    }
                }
            }
        }


//        val visitVitalSignsDbMap = vitalSigns.associateBy { it.timestamp }


//        _state.update { currentState ->
//
//
//            val updatedVitalSignsList =
//                currentState.vitalSigns.map { uiState ->
//
//                    val visitSpecificVitalSignData = visitVitalSignsDbMap[uiState.name]
//
//                    if (visitSpecificVitalSignData != null) {
//                        val precision = getPrecisionFor(uiState.name)
//
//                        check(precision != null) {
//                            "Precision for ${uiState.name} not found"
//                        }
//
//                        val value = when (precision) {
//                            NumericPrecision.INTEGER -> visitSpecificVitalSignData.value.toInt().toString()
//                            NumericPrecision.FLOAT -> visitSpecificVitalSignData.value.toString()
//                        }
//
//                        uiState.copy(value = value)
//                    } else {
//                        uiState
//                    }
//                }
//
//
//            currentState.copy(vitalSigns = updatedVitalSignsList)
//        }




    }

    fun getPrecisionFor(name: String) = getVitalSignPrecisionUseCase(name)

    fun evaluateSymptomColorDouble(
        value: Double,
        vitalLimit: ComputeSymptomsUseCase.VitalRange<*>?
    ) : Color {

        if (vitalLimit == null) {
            return Color.Transparent
        }

        val max = vitalLimit.high?.toDouble()
        val min = vitalLimit.low?.toDouble()


        if (min != null && value < min) return Color.Yellow.copy(alpha= 0.3f)

        if (max != null && value > max) return Color.Yellow.copy(alpha = 0.3f)

        return Color.Transparent
    }


    fun evaluateSymptomColor(
        value: Int,
        vitalLimit: ComputeSymptomsUseCase.VitalRange<*>?
    ): Color {
        if (vitalLimit == null) {
            return Color.Transparent
        }

        val max = vitalLimit.high?.toInt()
        val min = vitalLimit.low?.toInt()


        if (min != null && value < min) return Color.Yellow.copy(alpha= 0.3f)

        if (max != null && value > max) return Color.Yellow.copy(alpha = 0.3f)

        return Color.Transparent
    }


}