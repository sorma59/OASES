package com.unimib.oases.ui.screen.nurse_assessment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.unimib.oases.domain.usecase.ComputeSymptomsUseCase
import com.unimib.oases.domain.usecase.ConfigTriageUseCase
import com.unimib.oases.domain.usecase.DiseaseUseCase
import com.unimib.oases.domain.usecase.EvaluateTriageCodeUseCase
import com.unimib.oases.domain.usecase.GetPatientCategoryUseCase
import com.unimib.oases.domain.usecase.PatientDiseaseUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.VisitUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import com.unimib.oases.domain.usecase.VitalSigns
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningEvent
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningState
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.toBmiOrNull
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.toMuacCategoryOrNull
import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PastHistoryEvent
import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PastHistoryState
import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PatientDiseaseState
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageEvent
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState
import com.unimib.oases.ui.screen.nurse_assessment.triage.mapToTriageEvaluation
import com.unimib.oases.ui.screen.nurse_assessment.triage.symptoms
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryEvent
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryState
import com.unimib.oases.util.AppConstants
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private fun <T> Set<T>.toggle(element: T): Set<T> {
    return if (this.contains(element)) {
        this.minus(element)
    } else {
        this.plus(element)
    }
}

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    private val visitUseCase: VisitUseCase,
    private val diseaseUseCase: DiseaseUseCase,
    private val patientDiseaseUseCase: PatientDiseaseUseCase,
    private val visitVitalSignsUseCase: VisitVitalSignsUseCase,
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val computeSymptomsUseCase: ComputeSymptomsUseCase,
    private val configTriageUseCase: ConfigTriageUseCase,
    private val evaluateTriageCodeUseCase: EvaluateTriageCodeUseCase,
    private val getPatientCategoryUseCase: GetPatientCategoryUseCase,
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

            is RegistrationEvent.PatientSubmitted -> handlePatientSubmission(event)

            is RegistrationEvent.VitalSignsSubmitted -> handleVitalSignsSubmission(event)

            is RegistrationEvent.Submit -> handleFinalSubmission()
        }
    }

    fun getCurrentVisit(patientId: String) = visitUseCase.getCurrentVisit(patientId)

    // -----------------Triage----------------------

    fun onTriageEvent(event: TriageEvent) {
        when(event){

            is TriageEvent.FieldToggled -> handleTriageFieldToggle(event.fieldId)

            is TriageEvent.ToastShown -> {
                updateTriageState {
                    it.copy(toastMessage = null)
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
                it.copy(
                    selectedReds = triageEvaluationResource.data!!.redSymptomIds.toSet(),
                    selectedYellows = triageEvaluationResource.data!!.yellowSymptomIds.toSet()
                )
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

    // ------------Malnutrition Screening---------------

    init {
        viewModelScope.launch(Dispatchers.Default) {
            _state
                .debounce(400L)
                .collect { state ->
                    val bmi = state.malnutritionScreeningState.toBmiOrNull()
                    val muacCategory = state.malnutritionScreeningState.toMuacCategoryOrNull()
                    _state.update { current ->
                        current.copy(
                            malnutritionScreeningState = current.malnutritionScreeningState.copy(
                                bmi = bmi,
                                muacState = current.malnutritionScreeningState.muacState.copy(
                                    category = muacCategory
                                )
                            )
                        )
                    }
                }
        }
    }

    fun onMalnutritionScreeningEvent(event: MalnutritionScreeningEvent) {
        when (event) {
            is MalnutritionScreeningEvent.WeightChanged -> {
                updateMalnutritionScreeningState { it.copy(weight = event.weight) }
            }
            is MalnutritionScreeningEvent.HeightChanged -> {
                updateMalnutritionScreeningState { it.copy(height = event.height) }
            }
            is MalnutritionScreeningEvent.MuacChanged -> {
                updateMalnutritionScreeningState {
                    it.copy(
                        muacState = it.muacState.copy(
                            value = event.muac
                        )
                    )
                }
            }
        }
    }

    private fun handlePatientSubmission(event: RegistrationEvent.PatientSubmitted){
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
                        if (age >= AppConstants.MATURITY_AGE)
                            AgeSpecificity.ADULTS
                        else
                            AgeSpecificity.CHILDREN,
                    sex = sex
                )
            }
            updateTriageState {
                it.copy(
                    ageInMonths = event.patientInfoState.patient.ageInMonths,
                    triageConfig = configTriageUseCase(event.patientInfoState.patient.ageInMonths),
                    patientCategory = getPatientCategoryUseCase(event.patientInfoState.patient.ageInMonths)
                )
            }
            refreshVisitHistory(event.patientInfoState.patient.id)
            refreshPastHistory(event.patientInfoState.patient.id)
            val currentVisit = _state.value.currentVisit
            if (currentVisit != null)
                refreshTriage(currentVisit.id)
        }
    }

    private fun handleVitalSignsSubmission(event: RegistrationEvent.VitalSignsSubmitted){
        fun getVitalSignValue(name: String) =
            event.vitalSignsState.vitalSigns.firstOrNull { it.name == name && it.value.isNotEmpty()}?.value

        _state.update {
            _state.value.copy(
                vitalSignsState = event.vitalSignsState,
            )
        }

        val vitalSigns = VitalSigns(
            sbp = getVitalSignValue("Systolic Blood Pressure")?.toInt(),
            dbp = getVitalSignValue("Diastolic Blood Pressure")?.toInt(),
            hr = getVitalSignValue("Heart Rate")?.toInt(),
            rr = getVitalSignValue("Respiratory Rate")?.toInt(),
            spo2 = getVitalSignValue("Oxygen Saturation")?.toInt(),
            temp = getVitalSignValue("Temperature")?.toDouble()
        )

        updateTriageState {
            val ageInMonths = _state.value.patientInfoState.patient.ageInMonths
            it.copy(
                selectedReds = computeSymptomsUseCase.computeRedSymptoms(
                    selectedReds = it.selectedReds,
                    ageInMonths = ageInMonths,
                    vitalSigns = vitalSigns
                ),
                selectedYellows =
                    computeSymptomsUseCase.computeYellowSymptoms(
                        selectedYellows = it.selectedYellows,
                        ageInMonths = ageInMonths,
                        vitalSigns = vitalSigns
                    )
            )
        }
        updateTriageCode()
    }

    private fun handleFinalSubmission(){
        applicationScope.launch(dispatcher + errorHandler) {
            val patient = _state.value.patientInfoState.patient
            val vitalSigns =
                _state.value.vitalSignsState.vitalSigns.filter { it.value.isNotEmpty() }
            val triageCode = _state.value.triageState.triageCode

            val currentVisit = _state.value.currentVisit

            val visit =
                currentVisit?.copy(triageCode = triageCode)
                    ?: Visit(
                        patientId = patient.id,
                        triageCode = triageCode,
                        date = LocalDate.now().toString(),
                        description = "",
                        status = VisitStatus.OPEN
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
                    value = it.value
                )
                visitVitalSignsUseCase.addVisitVitalSign(visitVitalSign)

            }

            patientUseCase.updateStatus(patient, PatientStatus.WAITING_FOR_VISIT.displayValue)

            triageEvaluationRepository.insertTriageEvaluation(_state.value.triageState.mapToTriageEvaluation(visit.id))
        }
    }

    private fun handleTriageFieldToggle(fieldId: String) {
        val symptom = symptoms[fieldId]

        if (symptom == null)
            Log.e("TriageScreenViewModel", "Field not found: bug")
        else if (symptom.isComputed)
            updateTriageState { it.copy(toastMessage = "This field is computed") }
        else {
            updateTriageState {
                if (it.triageConfig?.redOptions?.any { it.id == fieldId } == true){
                    it.copy(
                        selectedReds = it.selectedReds.toggle(fieldId)
                    )
                } else{
                    it.copy(
                        selectedYellows = it.selectedYellows.toggle(fieldId)
                    )
                }
            }
            updateTriageCode()
        }
    }

    private fun updateMalnutritionScreeningState(update: (MalnutritionScreeningState) -> MalnutritionScreeningState) {
        _state.update { it.copy(malnutritionScreeningState = update(it.malnutritionScreeningState)) }
    }

    private fun updateTriageState(update: (TriageState) -> TriageState) {
        _state.update { it.copy(triageState = update(it.triageState)) }
    }

    private fun updateTriageCode(){
        _state.update { it.copy(
            triageState =
                it.triageState.copy(
                    triageCode = evaluateTriageCodeUseCase(it.triageState.selectedReds, it.triageState.selectedYellows)
                )
        ) }
    }

    private fun updatePastHistoryState(update: (PastHistoryState) -> PastHistoryState) {
        _state.update { it.copy(pastHistoryState = update(it.pastHistoryState)) }
    }

    private fun updateVisitHistoryState(update: (VisitHistoryState) -> VisitHistoryState) {
        _state.update { it.copy(visitHistoryState = update(it.visitHistoryState)) }
    }
}