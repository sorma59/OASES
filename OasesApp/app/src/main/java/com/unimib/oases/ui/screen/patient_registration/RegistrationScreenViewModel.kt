package com.unimib.oases.ui.screen.patient_registration

import androidx.lifecycle.ViewModel
import com.unimib.oases.data.local.model.PatientStatus
import com.unimib.oases.di.ApplicationScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.model.SexSpecificity.Companion.fromSexSpecificityDisplayName
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.model.VisitStatus
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.usecase.DiseaseUseCase
import com.unimib.oases.domain.usecase.PatientDiseaseUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.VisitUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryEvent
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryState
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PatientDiseaseState
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    private val visitUseCase: VisitUseCase,
    private val diseaseUseCase: DiseaseUseCase,
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
                    val age = event.patientInfoState.patient.ageInMonths / 12
                    val sex = fromSexSpecificityDisplayName(event.patientInfoState.patient.sex)
                    _state.update {
                        _state.value.copy(
                            patientInfoState = event.patientInfoState,
                            currentVisit = getCurrentVisit(event.patientInfoState.patient.id),
                            pastHistoryState = _state.value.pastHistoryState.copy(
                                age =
                                    if (age >= 12)
                                        AgeSpecificity.ADULTS
                                    else
                                        AgeSpecificity.CHILDREN,
                                sex = sex
                            )
                        )
                    }
                    refreshPastHistory(event.patientInfoState.patient.id)
                }
            }

//            is RegistrationEvent.PastMedicalHistoryNext -> {
//                _state.value = _state.value.copy(
//                    pastHistoryState = event.pastHistoryState
//                )
//            }

            is RegistrationEvent.VitalSignsSubmitted -> {
                _state.update {
                    _state.value.copy(
                        vitalSignsState = event.vitalSignsState
                    )
                }
            }

            is RegistrationEvent.TriageCodeSelected -> {
                _state.update {
                    _state.value.copy(
                        triageCode = event.triageCode
                    )
                }
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

    // --------------PastHistory--------------------

    fun onPastHistoryEvent(event: PastHistoryEvent) {
        when (event) {
            is PastHistoryEvent.CheckChanged -> {
                updatePastHistoryState {
                    it.copy(diseases = it.diseases.map { d ->
                        if (d.disease == event.disease) d.copy(isChecked = !d.isChecked) else d
                    })
                }
            }

            is PastHistoryEvent.AdditionalInfoChanged -> {
                updatePastHistoryState {
                    it.copy(diseases = it.diseases.map { d ->
                        if (d.disease == event.disease) d.copy(additionalInfo = event.additionalInfo) else d
                    })
                }
            }

            is PastHistoryEvent.DateChanged -> {
                updatePastHistoryState {
                    it.copy(diseases = it.diseases.map { d ->
                        if (d.disease == event.disease) d.copy(date = event.date) else d
                    })
                }
            }

            is PastHistoryEvent.Retry -> {
                refreshPastHistory(state.value.patientInfoState.patient.id)
            }
        }
    }


    fun refreshPastHistory(patientId: String?) {
        updatePastHistoryState { it.copy(error = null, isLoading = true) }

        applicationScope.launch(dispatcher + errorHandler) { // applicationScope?
            if (patientId != null) {
                loadDiseases()
                loadPatientDiseases(patientId)
            } else {
                loadDiseases()
            }
        }
    }

    private suspend fun loadDiseases() {
        updatePastHistoryState { it.copy(isLoading = true) }

        val sex = state.value.pastHistoryState.sex
        val age = state.value.pastHistoryState.age

        try {
            val diseasesResource = diseaseUseCase.getFilteredDiseases(sex.name, age.name).first {
                it is Resource.Success || it is Resource.Error
            }

            when (diseasesResource) {
                is Resource.Success -> {
                    val diseasesData = diseasesResource.data.orEmpty()
                    val newStates = diseasesData.map { PatientDiseaseState(it.name) }

                    updatePastHistoryState {
                        it.copy(diseases = newStates, error = null)
                    }
                }

                is Resource.Error -> {
                    updatePastHistoryState { it.copy(error = diseasesResource.message) }
                }

                else -> Unit
            }
        } catch (e: Exception) {
            updatePastHistoryState { it.copy(error = e.message ?: "Unexpected error") }
        } finally {
            updatePastHistoryState { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadPatientDiseases(patientId: String) {
        updatePastHistoryState { it.copy(isLoading = true) }

        try {
            val result = patientDiseaseUseCase.getPatientDiseases(patientId).first {
                it is Resource.Success || it is Resource.Error
            }

            if (result is Resource.Success) {
                val patientDiseases = result.data.orEmpty()
                val dbMap = patientDiseases.associateBy { it.diseaseName }

                updatePastHistoryState { currentState ->
                    val updatedDiseases = currentState.diseases.map { diseaseUi ->
                        dbMap[diseaseUi.disease]?.let { dbEntry ->
                            diseaseUi.copy(
                                isChecked = true,
                                additionalInfo = dbEntry.additionalInfo,
                                date = dbEntry.diagnosisDate
                            )
                        } ?: diseaseUi
                    }

                    currentState.copy(diseases = updatedDiseases, isLoading = false)
                }
            } else if (result is Resource.Error) {
                updatePastHistoryState { it.copy(error = result.message) }
            }
        } catch (e: Exception) {
            updatePastHistoryState { it.copy(error = e.message ?: "Unexpected error") }
        } finally {
            updatePastHistoryState { it.copy(isLoading = false) }
        }
    }

    private fun updatePastHistoryState(update: (PastHistoryState) -> PastHistoryState) {
        _state.update { it.copy(pastHistoryState = update(it.pastHistoryState)) }
    }
}