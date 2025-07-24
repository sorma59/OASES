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
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.usecase.DiseaseUseCase
import com.unimib.oases.domain.usecase.PatientDiseaseUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.VisitUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryEvent
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryState
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PatientDiseaseState
import com.unimib.oases.ui.screen.patient_registration.triage.TriageEvent
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState
import com.unimib.oases.ui.screen.patient_registration.triage.mapToTriageEvaluation
import com.unimib.oases.ui.screen.patient_registration.triage.mapToTriageState
import com.unimib.oases.ui.screen.patient_registration.visit_history.VisitHistoryEvent
import com.unimib.oases.ui.screen.patient_registration.visit_history.VisitHistoryState
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
    private val triageEvaluationRepository: TriageEvaluationRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message
            )
        }
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
                            currentVisit = getCurrentVisit(event.patientInfoState.patient.id)
                        )
                    }
                    updatePastHistoryState {
                        it.copy(
                            age =
                                if (age >= 12)
                                    AgeSpecificity.ADULTS
                                else
                                    AgeSpecificity.CHILDREN,
                            sex = sex
                        )
                    }
                    updateTriageState {
                        it.copy( age = age )
                    }
                    refreshVisitHistory(event.patientInfoState.patient.id)
                    refreshPastHistory(event.patientInfoState.patient.id)
                    val currentVisit = _state.value.currentVisit
                    if (currentVisit != null)
                        refreshTriage(currentVisit.id)
                }
            }



            is RegistrationEvent.VitalSignsSubmitted -> {
                _state.update {
                    _state.value.copy(
                        vitalSignsState = event.vitalSignsState,
                    )
                }
                updateTriageState {
                    it.copy(
                        sbp = event.vitalSignsState.vitalSigns.firstOrNull { it.name == "Systolic Blood Pressure" && it.value.isNotEmpty() }?.value?.toDouble()?.toInt(),
                        dbp = event.vitalSignsState.vitalSigns.firstOrNull { it.name == "Diastolic Blood Pressure" && it.value.isNotEmpty() }?.value?.toDouble()?.toInt(),
                        hr = event.vitalSignsState.vitalSigns.firstOrNull { it.name == "Heart Rate" && it.value.isNotEmpty() }?.value?.toDouble()?.toInt(),
                        rr = event.vitalSignsState.vitalSigns.firstOrNull { it.name == "Respiratory Rate" && it.value.isNotEmpty() }?.value?.toDouble()?.toInt(),
                        spo2 = event.vitalSignsState.vitalSigns.firstOrNull { it.name == "Oxygen Saturation" && it.value.isNotEmpty() }?.value?.toDouble()?.toInt(),
                        temp = event.vitalSignsState.vitalSigns.firstOrNull { it.name == "Temperature" && it.value.isNotEmpty() }?.value?.toDouble()
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

                    val currentVisit = _state.value.currentVisit

                    val visit =
                        currentVisit?.copy(triageCode = triageCode)
                            ?: Visit(
                                patientId = patient.id,
                                triageCode = triageCode,
                                date = LocalDate.now().toString(),
                                description = "",
                                status = VisitStatus.OPEN.name
                            )
                    visitUseCase.addVisit(visit)

                    _state.value.pastHistoryState.diseases.forEach {
                        if (it.isDiagnosed != null) {
                            val patientDisease = PatientDisease(
                                patientId = patient.id,
                                diseaseName = it.disease,
                                isDiagnosed = it.isDiagnosed == true,
                                additionalInfo = it.additionalInfo,
                                diagnosisDate = it.date
                            )
                            patientDiseaseUseCase.addPatientDisease(patientDisease)
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


                    triageEvaluationRepository.insertTriageEvaluation(_state.value.triageState.mapToTriageEvaluation(visit.id))
                }
            }
        }
    }

    // Call in a coroutine
    fun getCurrentVisit(patientId: String) = visitUseCase.getCurrentVisit(patientId)

    // -----------------Triage----------------------

    fun onTriageEvent(event: TriageEvent) {
        when(event){
            // Red Code
            is TriageEvent.UnconsciousnessChanged -> {
                updateTriageState {
                    it.copy(unconsciousness = event.value)
                }
            }
            is TriageEvent.ActiveConvulsionsChanged -> {
                updateTriageState {
                    it.copy(activeConvulsions = event.value)
                }
            }
            is TriageEvent.RespiratoryDistressChanged -> {
                updateTriageState {
                    it.copy(respiratoryDistress = event.value)
                }
            }
            is TriageEvent.HeavyBleedingChanged -> {
                updateTriageState {
                    it.copy(heavyBleeding = event.value)
                }
            }
            is TriageEvent.HighRiskTraumaBurnsChanged -> {
                updateTriageState {
                    it.copy(highRiskTraumaBurns = event.value)
                }
            }
            is TriageEvent.ThreatenedLimbChanged -> {
                updateTriageState {
                    it.copy(threatenedLimb = event.value)
                }
            }
            is TriageEvent.PoisoningIntoxicationChanged -> {
                updateTriageState {
                    it.copy(poisoningIntoxication = event.value)
                }
            }
            is TriageEvent.SnakeBiteChanged -> {
                updateTriageState {
                    it.copy(snakeBite = event.value)
                }
            }
            is TriageEvent.AggressiveBehaviorChanged -> {
                updateTriageState {
                    it.copy(aggressiveBehavior = event.value)
                }
            }
            is TriageEvent.PregnancyChanged -> {
                updateTriageState {
                    if (event.value == true)
                        it.copy(pregnancy = true)
                    else
                        it.copy(
                            pregnancy = false,
                            pregnancyWithHeavyBleeding = false,
                            pregnancyWithSevereAbdominalPain = false,
                            pregnancyWithSeizures = false,
                            pregnancyWithAlteredMentalStatus = false,
                            pregnancyWithSevereHeadache = false,
                            pregnancyWithVisualChanges = false,
                            pregnancyWithTrauma = false,
                            pregnancyWithActiveLabor = false,
                        )
                }
            }
            is TriageEvent.PregnancyWithHeavyBleedingChanged -> {
                updateTriageState {
                    it.copy(pregnancyWithHeavyBleeding = event.value)
                }
            }
            is TriageEvent.PregnancyWithSevereAbdominalPainChanged -> {
                updateTriageState {
                    it.copy(pregnancyWithSevereAbdominalPain = event.value)
                }
            }
            is TriageEvent.PregnancyWithSeizuresChanged -> {
                updateTriageState {
                    it.copy(pregnancyWithSeizures = event.value)
                }
            }
            is TriageEvent.PregnancyWithAlteredMentalStatusChanged -> {
                updateTriageState {
                     it.copy(pregnancyWithAlteredMentalStatus = event.value)
                }
            }
            is TriageEvent.PregnancyWithSevereHeadacheChanged -> {
                updateTriageState {
                    it.copy(pregnancyWithSevereHeadache = event.value)
                }
            }
            is TriageEvent.PregnancyWithVisualChangesChanged -> {
                updateTriageState {
                    it.copy(pregnancyWithVisualChanges = event.value)
                }
            }
            is TriageEvent.PregnancyWithTraumaChanged -> {
                updateTriageState {
                    it.copy(pregnancyWithTrauma = event.value)
                }
            }
            is TriageEvent.PregnancyWithActiveLaborChanged -> {
                updateTriageState {
                    it.copy(pregnancyWithActiveLabor = event.value)
                }
            }

            // Yellow Code

            is TriageEvent.AcuteLimbDeformityOpenFractureChanged -> {
                updateTriageState {
                    it.copy(acuteLimbDeformityOpenFracture = event.value)
                }
            }
            is TriageEvent.AcuteTesticularScrotalPainPriapismChanged -> {
                updateTriageState {
                    it.copy(acuteTesticularScrotalPainPriapism = event.value)
                }
            }
            is TriageEvent.AirwaySwellingMassChanged -> {
                updateTriageState {
                    it.copy(airwaySwellingMass = event.value)
                }
            }
            is TriageEvent.AnimalBiteNeedlestickPunctureChanged -> {
                updateTriageState {
                    it.copy(animalBiteNeedlestickPuncture = event.value)
                }
            }
            is TriageEvent.FocalNeurologicVisualDeficitChanged -> {
                updateTriageState {
                    it.copy(focalNeurologicVisualDeficit = event.value)
                }
            }
            is TriageEvent.HeadacheWithStiffNeckChanged -> {
                updateTriageState {
                    it.copy(headacheWithStiffNeck = event.value)
                }
            }
            is TriageEvent.LethargyConfusionAgitationChanged -> {
                updateTriageState {
                    it.copy(lethargyConfusionAgitation = event.value)
                }
            }
            is TriageEvent.OngoingBleedingChanged -> {
                updateTriageState {
                    it.copy(ongoingBleeding = event.value)
                }
            }
            is TriageEvent.OngoingSevereVomitingDiarrheaChanged -> {
                updateTriageState {
                    it.copy(ongoingSevereVomitingDiarrhea = event.value)
                }
            }
            is TriageEvent.OtherPregnancyRelatedComplaintsChanged -> {
                updateTriageState {
                    it.copy(otherPregnancyRelatedComplaints = event.value)
                }
            }
            is TriageEvent.OtherTraumaBurnsChanged -> {
                updateTriageState {
                    it.copy(otherTraumaBurns = event.value)
                }
            }
            is TriageEvent.RecentFaintingChanged -> {
                updateTriageState {
                    it.copy(recentFainting = event.value)
                }
            }
            is TriageEvent.SeverePainChanged -> {
                updateTriageState {
                    it.copy(severePain = event.value)
                }
            }
            is TriageEvent.SeverePallorChanged -> {
                updateTriageState {
                    it.copy(severePallor = event.value)
                }
            }
            is TriageEvent.SexualAssaultChanged -> {
                updateTriageState {
                    it.copy(sexualAssault = event.value)
                }
            }
            is TriageEvent.UnableToFeedOrDrinkChanged -> {
                updateTriageState {
                    it.copy(unableToFeedOrDrink = event.value)
                }
            }
            is TriageEvent.UnableToPassUrineChanged -> {
                updateTriageState {
                    it.copy(unableToPassUrine = event.value)
                }
            }
        }
    }

    fun refreshTriage(visitId: String) {
        updateTriageState { it.copy(error = null, isLoading = true) }

        applicationScope.launch(dispatcher + errorHandler) {
            loadPatientSymptoms(visitId)
        }
    }

    private fun loadPatientSymptoms(visitId: String){
        val triageEvaluationResource = triageEvaluationRepository.getTriageEvaluation(visitId)
        if (triageEvaluationResource is Resource.Success)
            updateTriageState {
                return@updateTriageState triageEvaluationResource.data!!.mapToTriageState()
            }
        else
            updateTriageState { it.copy(error = triageEvaluationResource.message) }
    }

    // --------------Visit History------------------

    fun onVisitHistoryEvent(event: VisitHistoryEvent) {
        when (event) {
            is VisitHistoryEvent.Retry -> {
                refreshVisitHistory(state.value.patientInfoState.patient.id)
            }
        }
    }

    fun refreshVisitHistory(patientId: String) {
        updateVisitHistoryState { it.copy(error = null, isLoading = true) }

        applicationScope.launch(dispatcher + errorHandler) {
            loadVisits(patientId)
        }
    }

    private suspend fun loadVisits(patientId: String) {

        patientUseCase.getPatientVisits(patientId).collect { visits ->
            updateVisitHistoryState { it.copy(visits = visits.data ?: emptyList(), isLoading = false) }
        }

    }

    // --------------PastHistory--------------------

    fun onPastHistoryEvent(event: PastHistoryEvent) {
        when (event) {

            is PastHistoryEvent.RadioButtonClicked -> {
                updatePastHistoryState {
                    it.copy(diseases = it.diseases.map { d ->
                        if (d.disease == event.disease) d.copy(isDiagnosed = event.isDiagnosed) else d
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

            is PastHistoryEvent.NurseClicked -> {
                updatePastHistoryState {
                    it.copy(toastMessage = "Only doctors can edit PMH")
                }
            }

            is PastHistoryEvent.ToastShown -> {
                updatePastHistoryState { it.copy(toastMessage = null) }
            }
        }
    }


    fun refreshPastHistory(patientId: String) {
        updatePastHistoryState { it.copy(error = null, isLoading = true) }

        applicationScope.launch(dispatcher + errorHandler) { // applicationScope?
            loadDiseases()
            loadPatientDiseases(patientId)
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
                                isDiagnosed = dbEntry.isDiagnosed,
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

    private fun updateTriageState(update: (TriageState) -> TriageState) {
        _state.update { it.copy(triageState = update(it.triageState)) }
    }

//    private fun updateTriageState(triageState: TriageState) {
//        _state.update { it.copy(triageState = triageState) }
//    }

    private fun updatePastHistoryState(update: (PastHistoryState) -> PastHistoryState) {
        _state.update { it.copy(pastHistoryState = update(it.pastHistoryState)) }
    }

    private fun updateVisitHistoryState(update: (VisitHistoryState) -> VisitHistoryState) {
        _state.update { it.copy(visitHistoryState = update(it.visitHistoryState)) }
    }
}