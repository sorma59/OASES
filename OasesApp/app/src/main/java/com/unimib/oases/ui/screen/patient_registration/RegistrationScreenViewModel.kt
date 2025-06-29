package com.unimib.oases.ui.screen.patient_registration

import androidx.lifecycle.ViewModel
import com.unimib.oases.data.local.model.PatientStatus
import com.unimib.oases.di.ApplicationScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.model.VisitStatus
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.usecase.PatientDiseaseUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.VisitUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    private val visitUseCase: VisitUseCase,
    private val patientDiseaseUseCase: PatientDiseaseUseCase,
    private val visitVitalSignsUseCase: VisitVitalSignsUseCase,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
//            isLoading = false
        )
    }


    fun onEvent(event: RegistrationEvent) {
        when (event) {

            is RegistrationEvent.PatientSubmitted -> {
                applicationScope.launch(dispatcher + errorHandler) {
                    _state.value = _state.value.copy(
                        patientInfoState = event.patientInfoState,
                        currentVisit = getCurrentVisit(event.patientInfoState.patient.id)
                    )
                }
            }

            is RegistrationEvent.PastMedicalHistoryNext -> {
                _state.value = _state.value.copy(
                    pastHistoryState = event.pastHistoryState
                )
            }

            is RegistrationEvent.VitalSignsSubmitted -> {
                _state.value = _state.value.copy(
                    vitalSignsState = event.vitalSignsState
                )
            }

            is RegistrationEvent.TriageCodeSelected -> {
                _state.value = _state.value.copy(
                    triageCode = event.triageCode
                )
            }

            is RegistrationEvent.Submit -> {
                applicationScope.launch(dispatcher + errorHandler) {
                    val patient = _state.value.patientInfoState.patient
                    val vitalSigns =
                        _state.value.vitalSignsState.vitalSigns.filter { it.value.isNotEmpty() }
                    val triageCode = _state.value.triageCode

                    var visit = getCurrentVisit(patient.id)

                    if (visit == null){
                        visit = Visit(
                            patientId = patient.id,
                            triageCode = triageCode,
                            date = LocalDate.now().toString(),
                            description = "",
                            status = VisitStatus.OPEN.name
                        )
                    }


                    if (event.reevaluateTriageCode)
                        visitUseCase.addVisit(visit.copy(triageCode = triageCode))

                    _state.value.pastHistoryState.diseases.forEach {
                        if (it.isChecked) {
                            val patientDisease = PatientDisease(
                                patientId = patient.id,
                                diseaseName = it.disease,
                                additionalInfo = it.additionalInfo,
                                diagnosisDate = it.date
                            )
                            patientDiseaseUseCase.addPatientDisease(patientDisease)
                        } else {
                            patientDiseaseUseCase.deletePatientDisease(it.disease, patient.id)
                        }
                    }

                    vitalSigns.forEach {

                        val visitVitalSign = VisitVitalSign(
                            visitId = visit.id,
                            vitalSignName = it.name,
                            timestamp = System.currentTimeMillis().toString(),
                            value = it.value.toDouble()
                        )
                        visitVitalSignsUseCase.addVisitVitalSign(visitVitalSign)

                    }

//                    patientUseCase.updateTriageState(patient, triageCode)

                    patientUseCase.updateStatus(patient, PatientStatus.WAITING_FOR_VISIT.displayValue)
                }
            }
        }
    }

    // Call in a coroutine
    fun getCurrentVisit(patientId: String) = visitUseCase.getCurrentVisit(patientId)
}