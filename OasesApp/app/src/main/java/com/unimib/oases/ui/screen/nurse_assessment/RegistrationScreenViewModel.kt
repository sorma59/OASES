package com.unimib.oases.ui.screen.nurse_assessment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.local.model.PatientStatus
import com.unimib.oases.di.ApplicationScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.model.symptom.triageSymptoms
import com.unimib.oases.domain.model.toState
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.usecase.ComputeSymptomsUseCase
import com.unimib.oases.domain.usecase.ConfigTriageUseCase
import com.unimib.oases.domain.usecase.EvaluateTriageCodeUseCase
import com.unimib.oases.domain.usecase.GetCurrentVisitUseCase
import com.unimib.oases.domain.usecase.GetPatientCategoryUseCase
import com.unimib.oases.domain.usecase.GetVitalSignPrecisionUseCase
import com.unimib.oases.domain.usecase.InsertPatientLocallyUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.RoomUseCase
import com.unimib.oases.domain.usecase.VisitUseCase
import com.unimib.oases.domain.usecase.VisitVitalSignsUseCase
import com.unimib.oases.domain.usecase.VitalSignUseCase
import com.unimib.oases.domain.usecase.VitalSigns
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.screen.nurse_assessment.handler.PatientInfoHandler
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningEvent
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningState
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.toBmiOrNull
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.toMalnutritionScreeningOrNull
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.toMuacCategoryOrNull
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoEffect
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoEvent
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoState
import com.unimib.oases.ui.screen.nurse_assessment.room_selection.RoomEvent
import com.unimib.oases.ui.screen.nurse_assessment.room_selection.RoomState
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageEvent
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState
import com.unimib.oases.ui.screen.nurse_assessment.triage.mapToTriageEvaluation
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryEvent
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.PatientVitalSignState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsEvent
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.toVisitVitalSigns
import com.unimib.oases.util.DateTimeFormatter
import com.unimib.oases.util.Resource
import com.unimib.oases.util.debounce
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
import com.unimib.oases.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

fun Array<Tab>.next(currentIndex: Int) = if (currentIndex != this.lastIndex) this[(currentIndex + 1)] else null

fun Array<Tab>.previous(currentIndex: Int) = if (currentIndex != 0) this[(currentIndex - 1)] else null

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    private val visitUseCase: VisitUseCase,
    private val visitVitalSignsUseCase: VisitVitalSignsUseCase,
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val malnutritionScreeningRepository: MalnutritionScreeningRepository,
    private val computeSymptomsUseCase: ComputeSymptomsUseCase,
    private val configTriageUseCase: ConfigTriageUseCase,
    private val evaluateTriageCodeUseCase: EvaluateTriageCodeUseCase,
    private val getPatientCategoryUseCase: GetPatientCategoryUseCase,
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val insertPatientLocallyUseCase: InsertPatientLocallyUseCase,
    private val vitalSignsUseCases: VitalSignUseCase,
    private val roomUseCase: RoomUseCase,
    private val visitVitalSignsUseCases: VisitVitalSignsUseCase,
    private val getVitalSignPrecisionUseCase: GetVitalSignPrecisionUseCase,
    private val patientInfoHandler: PatientInfoHandler,
    savedStateHandle: SavedStateHandle,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationState(
        patientId = savedStateHandle["patientId"]!!
    ))
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    val tabs = state.value.tabs

    private val navigationEventsChannel = Channel<NavigationEvent>()
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    private val validationEventsChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventsChannel.receiveAsFlow()

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("Prova", "errorHandler")
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message
            )
        }
    }

    private val patientErrorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("Prova", "patientErrorHandler")
        e.printStackTrace()
        updatePatientInfoState {
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    private val visitErrorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("Prova", "visitErrorHandler")
        e.printStackTrace()
        updateVisitHistoryState {
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    private val vitalSignsErrorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("Prova", "vitalSignsErrorHandler")
        e.printStackTrace()
        updateVitalSignsState {
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    private val roomsErrorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("Prova", "roomsErrorHandler")
        e.printStackTrace()
        updateRoomState {
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    private val triageErrorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        updateTriageState {
            it.copy(
                error = e.message,
                loaded = false
            )
        }
    }

    private val malnutritionErrorHandler = CoroutineExceptionHandler { _, e ->
        Log.e("Prova", "malnutritionErrorHandler")
        e.printStackTrace()
        updateMalnutritionScreeningState {
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    val mainContext = ioDispatcher + errorHandler

    val patientCoroutineContext = ioDispatcher + patientErrorHandler

    val visitContext = ioDispatcher + visitErrorHandler

    val roomsContext = ioDispatcher + roomsErrorHandler

    val triageContext = ioDispatcher + triageErrorHandler

    val vitalSignsContext = ioDispatcher + vitalSignsErrorHandler

    val malnutritionContext = ioDispatcher + malnutritionErrorHandler

    fun onEvent(event: RegistrationEvent) {
        when (event) {

            is RegistrationEvent.PatientSubmitted -> handlePatientSubmission()

            is RegistrationEvent.VitalSignsSubmitted -> handleVitalSignsSubmission()

            is RegistrationEvent.Submit -> handleFinalSubmission()
        }
    }

    suspend fun getCurrentVisit(patientId: String) = getCurrentVisitUseCase(patientId).firstNullableSuccess()

    // --------------Demographics-----------------------

    init {
        refreshPatientInfo()
        refreshVitalSigns()
        refreshRooms()
    }

    private fun refreshPatientInfo() {
        viewModelScope.launch(patientCoroutineContext) {
            updatePatientInfoState {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            val patient = patientUseCase
                .getPatient(_state.value.patientId)
                .firstSuccess()
            updatePatientInfoState {
                it.copy(
                    patient = patient,
                    isNew = false,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun onPatientInfoEvent(event: PatientInfoEvent) {
        val (newState, effect) = patientInfoHandler.handle(state.value.patientInfoState, event)
        _state.update { it.copy(patientInfoState = newState) }
        effect?.let{ handlePatientInfoEffect(it) }
    }

    fun handlePatientInfoEffect(effect: PatientInfoEffect) {
        when (effect) {
            PatientInfoEffect.SavePatientData -> {
                applicationScope.launch(mainContext) {
                    savePatientData()
                }
            }
            PatientInfoEffect.SendValidationResult -> {
                viewModelScope.launch(mainContext) {
                    validationEventsChannel.send(ValidationEvent.ValidationSuccess)
                }
            }
            PatientInfoEffect.ComputeAge -> {
                val newAge = DateTimeFormatter().calculateAgeInMonths(state.value.patientInfoState.patient.birthDate)
                newAge?.let{
                    onPatientInfoEvent(PatientInfoEvent.AgeComputed(it))
                }
            }
            PatientInfoEffect.ComputeBirthDate -> {
                val newBirthDate = DateTimeFormatter().calculateBirthDate(state.value.patientInfoState.patient.ageInMonths)
                newBirthDate?.let{
                    onPatientInfoEvent(PatientInfoEvent.BirthDateComputed(it))
                }
            }
            PatientInfoEffect.Retry -> {
                refreshPatientInfo()
            }
        }
    }

    private suspend fun savePatientData() {

        updatePatientInfoState {
            it.copy(isLoading = true)
        }
        try {
            val result = insertPatientLocallyUseCase(_state.value.patientInfoState.patient)
            if (result is Resource.Error)
                throw Exception(result.message)
        }
        catch (e: Exception){
            updatePatientInfoState {
                it.copy(error = e.message)
            }
        }
        finally {
            updatePatientInfoState {
                it.copy(isLoading = false)
            }
        }
    }

    // -----------------Vital Signs---------------------------

    fun onVitalSignsEvent(event: VitalSignsEvent) {
        when (event) {
            is VitalSignsEvent.Retry -> {
                refreshVitalSigns()
            }

            is VitalSignsEvent.ValueChanged -> {
                updateVitalSignsState {
                    it.copy(
                        vitalSigns = it.vitalSigns.map {
                            if (it.name == event.vitalSign) {
                                it.copy(value = event.value)
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        }
    }

    private fun refreshVitalSigns(){
        viewModelScope.launch(vitalSignsContext) {
            updateVitalSignsState { it.copy(error = null, isLoading = true) }
            loadVitalSigns()
            getCurrentVisit(_state.value.patientId)?.let {
                if (_state.value.vitalSignsState.error == null)
                    loadVisitVitalSigns(it.id)
            }
            updateVitalSignsState { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadVitalSigns() {
        val vitalSigns = vitalSignsUseCases.getVitalSigns().firstSuccess()
        val newStates = vitalSigns.map { vitalSign -> // More efficient mapping
            PatientVitalSignState(vitalSign.name, vitalSign.acronym, vitalSign.unit)
        }
        updateVitalSignsState {
            it.copy(
                vitalSigns = newStates, // Set the list, don't append if it's initial load
            )
        }
    }

    // Rooms

    private fun refreshRooms(){
        viewModelScope.launch(roomsContext) {
            updateRoomState { it.copy(error = null, isLoading = true) }
            loadRooms()
            updateRoomState { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadRooms(){
        val rooms = roomUseCase.getRooms().firstSuccess()
        updateRoomState {
            it.copy(
                rooms = rooms, // Set the list, don't append if it's initial load
                currentRoom = Room(state.value.patientInfoState.patient.room),
                currentTriageCode = state.value.triageState.triageCode.name,
            )
        }
    }

    private suspend fun loadVisitVitalSigns(visitId: String) {

        val vitalSigns = visitVitalSignsUseCases
            .getVisitVitalSigns(visitId)
            .firstSuccess()
        val visitVitalSignsDbMap = vitalSigns.associateBy { it.vitalSignName }

        updateVitalSignsState { currentState ->
            val updatedVitalSignsList =
                currentState.vitalSigns.map { uiState ->

                    val visitSpecificVitalSignData = visitVitalSignsDbMap[uiState.name]

                    if (visitSpecificVitalSignData != null) {
                        val precision = getPrecisionFor(uiState.name)

                        check(precision != null) {
                            "Precision for ${uiState.name} not found"
                        }

                        val value = when (precision) {
                            NumericPrecision.INTEGER -> visitSpecificVitalSignData.value.toInt().toString()
                            NumericPrecision.FLOAT -> visitSpecificVitalSignData.value.toString()
                        }

                        uiState.copy(value = value)
                    } else {
                        uiState
                    }
                }
            currentState.copy(vitalSigns = updatedVitalSignsList)
        }
    }

    fun getPrecisionFor(name: String) = getVitalSignPrecisionUseCase(name)

    // -----------------ROOM SELECTION----------------------

    fun onRoomEvent(event: RoomEvent){
        when(event){
            is RoomEvent.RoomSelected -> {
                updateRoomState { it.copy(currentRoom = event.room) }
            }
            RoomEvent.Retry -> {
                refreshRooms()
            }
            RoomEvent.ConfirmSelection -> onNext()
            RoomEvent.RoomDeselected -> {updateRoomState { it.copy(currentRoom = null) }}
        }
    }

    // -----------------Triage----------------------

    fun onTriageEvent(event: TriageEvent) {
        when(event){

            is TriageEvent.FieldToggled -> handleTriageFieldToggle(event.fieldId)

            is TriageEvent.ToastShown -> {
                updateTriageState {
                    it.copy(toastMessage = null)
                }
            }

            is TriageEvent.Retry -> {
                refreshTriage(state.value.currentVisit!!.id)
            }
        }
    }

    fun refreshTriage(visitId: String) {
        viewModelScope.launch(triageContext) {
            updateTriageState { it.copy(error = null, isLoading = true) }
            loadTriageEvaluation(visitId)
            updateTriageState { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadTriageEvaluation(visitId: String){
        val triageEvaluation = triageEvaluationRepository.getTriageEvaluation(visitId).firstSuccess()
        updateTriageState {
            it.copy(
                selectedReds = triageEvaluation.redSymptomIds.toSet(),
                selectedYellows = triageEvaluation.yellowSymptomIds.toSet(),
            )
        }
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
        viewModelScope.launch(visitContext) {
            loadVisits(patientId)
        }
    }

    private suspend fun loadVisits(patientId: String) {

        patientUseCase.getPatientVisits(patientId).collect { visits ->
            if (visits is Resource.Success)
                updateVisitHistoryState { it.copy(visits = visits.data ?: emptyList(), isLoading = false) }
            else if (visits is Resource.Error)
                updateVisitHistoryState { it.copy(error = visits.message, isLoading = false) }
        }

    }

    // ------------Malnutrition Screening---------------

    init {
        viewModelScope.launch(Dispatchers.Default) {
            _state
                .debounce(400L)
                .collect { state ->
                    val bmi = state.malnutritionScreeningState.toBmiOrNull()
                    val muacCategory =
                        state.malnutritionScreeningState.toMuacCategoryOrNull()
                    updateMalnutritionScreeningState {
                        it.copy(
                            bmi = bmi,
                            muacState = it.muacState.copy(
                                category = muacCategory
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
            MalnutritionScreeningEvent.Retry -> {
                refreshMalnutritionScreening(state.value.currentVisit!!.id)
            }
        }
    }

    fun refreshMalnutritionScreening(visitId: String) {
        updateMalnutritionScreeningState { it.copy(error = null, isLoading = true) }
        viewModelScope.launch(malnutritionContext) {
            loadMalnutritionScreening(visitId)
            updateMalnutritionScreeningState {
                it.copy(isLoading = false)
            }
        }
    }

    private suspend fun loadMalnutritionScreening(visitId: String) {
        val malnutritionScreening = malnutritionScreeningRepository
            .getMalnutritionScreening(visitId)
            .firstNullableSuccess()
        updateMalnutritionScreeningState {
            malnutritionScreening.toState()
        }
    }

    private fun handlePatientSubmission(){

        val ageInMonths = _state.value.patientInfoState.patient.ageInMonths
        val id = _state.value.patientInfoState.patient.id

        applicationScope.launch(mainContext) {
            val currentVisit = getCurrentVisit(id)
            _state.update {
                _state.value.copy(
                    currentVisit = currentVisit
                )
            }
            updateTriageState {
                it.copy(
                    ageInMonths = ageInMonths,
                    triageConfig = configTriageUseCase(ageInMonths),
                    patientCategory = getPatientCategoryUseCase(ageInMonths)
                )
            }
            refreshVisitHistory(id)
            if (currentVisit != null) {
                refreshTriage(currentVisit.id)
                refreshMalnutritionScreening(currentVisit.id)
            }
        }
    }

    private fun handleVitalSignsSubmission(){
        fun getVitalSignValue(name: String) =
            _state.value.vitalSignsState.vitalSigns.firstOrNull {
                it.name == name && it.value.isNotEmpty()
            }?.value?.toDouble()

        val vitalSigns = VitalSigns(
            sbp = getVitalSignValue("Systolic Blood Pressure")?.toInt(),
            dbp = getVitalSignValue("Diastolic Blood Pressure")?.toInt(),
            hr = getVitalSignValue("Heart Rate")?.toInt(),
            rr = getVitalSignValue("Respiratory Rate")?.toInt(),
            spo2 = getVitalSignValue("Oxygen Saturation")?.toInt(),
            temp = getVitalSignValue("Temperature")
        )

        updateTriageState {
            val ageInMonths = _state.value.patientInfoState.patient.ageInMonths
            it.copy(
                selectedReds = computeSymptomsUseCase.computeRedSymptoms(
                    selectedReds = it.selectedReds,
                    ageInMonths = ageInMonths,
                    vitalSigns = vitalSigns
                ),
                selectedYellows = computeSymptomsUseCase.computeYellowSymptoms(
                    selectedYellows = it.selectedYellows,
                    ageInMonths = ageInMonths,
                    vitalSigns = vitalSigns
                )
            )
        }
        updateTriageCode()
    }

    private fun handleFinalSubmission(){
        applicationScope.launch(mainContext) {
            val patient = _state.value.patientInfoState.patient
            val currentVisit = _state.value.currentVisit
            val triageCode =
                if (_state.value.triageState.loaded)
                    _state.value.triageState.triageCode
                else
                    null
            val assignedRoom = _state.value.roomState.currentRoom?.name ?: ""
            val visit =
                currentVisit?.copy(triageCode = triageCode ?: currentVisit.triageCode)
                    ?: Visit(
                        patientId = patient.id,
                        triageCode = triageCode!!,
                        date = LocalDate.now(),
                        description = ""
                    )
            visitUseCase.addVisit(visit)

            val vitalSigns = _state.value.vitalSignsState.toVisitVitalSigns(visit.id)

            vitalSigns.forEach {
                visitVitalSignsUseCase.addVisitVitalSign(it)
            }

            patientUseCase.updateStatus(patient, PatientStatus.WAITING_FOR_VISIT.displayValue, triageCode.toString(), assignedRoom.toString())

            if (state.value.triageState.loaded)
                triageEvaluationRepository.insertTriageEvaluation(_state.value.triageState.mapToTriageEvaluation(visit.id))

            val screening = _state.value.malnutritionScreeningState.toMalnutritionScreeningOrNull(visit.id)
            if (screening != null) {
                val result = malnutritionScreeningRepository.insertMalnutritionScreening(screening)
                if (result is Resource.Error)
                    Log.e("RegistrationScreenViewModel", result.message.toString())
            }
        }
    }

    private fun handleTriageFieldToggle(fieldId: String) {
        val symptom = triageSymptoms[fieldId]

        if (symptom == null)
            Log.e("TriageScreenViewModel", "Field not found: bug")
        else if (symptom.isComputed)
            updateTriageState { it.copy(toastMessage = "This field is computed") }
        else {
            updateTriageState {
                if (it.triageConfig?.redOptions?.any { it.symptom.symptom.id == fieldId } == true){
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
        _state.update {
            it.copy(
                triageState = it.triageState.copy(
                    triageCode = evaluateTriageCodeUseCase(it.triageState.selectedReds, it.triageState.selectedYellows)
                )
            )
        }
    }

    private fun updateVisitHistoryState(update: (VisitHistoryState) -> VisitHistoryState) {
        _state.update { it.copy(visitHistoryState = update(it.visitHistoryState)) }
    }

    private fun updatePatientInfoState(update: (PatientInfoState) -> PatientInfoState) {
        _state.update { it.copy(patientInfoState = update(it.patientInfoState)) }
    }

    private fun updateVitalSignsState(update: (VitalSignsState) -> VitalSignsState) {
        _state.update { it.copy(vitalSignsState = update(it.vitalSignsState)) }
    }

    private fun updateRoomState(update: (RoomState) -> RoomState){
        _state.update { it.copy(roomState = update(it.roomState)) }
    }

    // --------------Flow-----------------

    fun onNext() {
        executeSideEffect()
        goToNextStep()
    }

    private fun goToNextStep() {
        val currentIndex = _state.value.currentStep
        if (currentIndex != _state.value.tabs.lastIndex) {
            val nextIndex = calculateNextStep(currentIndex)
            _state.update { it.copy(currentStep = nextIndex) }
        }
    }

    fun onBack() {
        val currentIndex = _state.value.currentStep
        if (state.value.currentTab == Tab.CONTINUE_TO_TRIAGE ||
            currentIndex == 0) {
            viewModelScope.launch(mainContext){
                navigationEventsChannel.send(NavigationEvent.NavigateBack)
            }
            return
        }
        val previousStep = calculatePreviousStep(currentIndex)
        _state.update { it.copy(currentStep = previousStep) }
    }

    private fun calculateNextStep(currentIndex: Int): Int {
        val nextTab = tabs.next(currentIndex)
        if (nextTab == Tab.YELLOW_CODE && _state.value.triageState.isRedCode ||
            nextTab == Tab.CONTINUE_TO_TRIAGE && mustSkipContinueToTriagePage()){
            return currentIndex + 2
        }
        return (currentIndex + 1).coerceAtMost(tabs.lastIndex)
    }

    private fun calculatePreviousStep(currentIndex: Int): Int {
        val previousTab = tabs.previous(currentIndex)
        if (previousTab == Tab.YELLOW_CODE && _state.value.triageState.isRedCode ||
            previousTab == Tab.CONTINUE_TO_TRIAGE && mustSkipContinueToTriagePage()){
            return currentIndex - 2
        }
        return (currentIndex - 1).coerceAtLeast(0)
    }

    private fun executeSideEffect() {
        when (state.value.currentTab){
            Tab.DEMOGRAPHICS -> {
                onPatientInfoEvent(PatientInfoEvent.ConfirmSubmission)
                onEvent(RegistrationEvent.PatientSubmitted)
            }
            Tab.VITAL_SIGNS -> {
                onEvent(RegistrationEvent.VitalSignsSubmitted)
            }
            Tab.SUBMIT_ALL -> {
                onEvent(RegistrationEvent.Submit)
                viewModelScope.launch(mainContext) {
                    navigationEventsChannel.send(NavigationEvent.NavigateBack)
                }
            }
            else -> {}
        }
    }

    fun mustSkipContinueToTriagePage(): Boolean {
        return !_state.value.patientInfoState.isEdited && !_state.value.patientInfoState.isNew
    }

    sealed class ValidationEvent {
        data object ValidationSuccess : ValidationEvent() // Successful validation
    }
}