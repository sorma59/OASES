package com.unimib.oases.ui.screen.dashboard.patient.view

//@HiltViewModel
//class PatientDetailsViewModel @Inject constructor(
//    private val patientRepository: PatientRepository,
//    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
//    private val getCurrentVisitMainComplaintUseCase: GetCurrentVisitMainComplaintUseCase,
//    private val getPatientChronicDiseasesUseCase: GetPatientChronicDiseasesUseCase,
//    savedStateHandle: SavedStateHandle,
//    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
//): ViewModel() {
//
//    private val errorHandler = CoroutineExceptionHandler { _, e ->
//        e.printStackTrace()
//        _state.update{
//            _state.value.copy(
//                currentVisitRelatedError = e.message
//            )
//        }
//    }
//
//    private val args = savedStateHandle.toRoute< Route.ViewPatientDetails>()
//
//    private val _state = MutableStateFlow(
//        PatientDetailsState(patientId = args.patientId)
//    )
//    val state: StateFlow<PatientDetailsState> = _state.asStateFlow()
//
//    private val navigationEventsChannel = Channel<NavigationEvent>()
//    val navigationEvents = navigationEventsChannel.receiveAsFlow()
//
//    init {
//        getPatientData()
//    }
//
//    private fun getPatientData() {
//        _state.update { it.copy(isLoading = true, currentVisitRelatedError = null, demographicsError = null) }
//        viewModelScope.launch(dispatcher + errorHandler) {
//            getPatientDemographics()
//            getPastMedicalHistory()
//            getCurrentVisit()
//            getComplaintsSummaries()
//        }
//        _state.update { it.copy(isLoading = false) }
//    }
//
//    private suspend fun getPastMedicalHistory() {
//        try {
//            val patientDiseases = getPatientChronicDiseasesUseCase(
//                _state.value.patientId
//            )
//            _state.update { it.copy(chronicDiseases = patientDiseases) }
//        } catch (e: Exception) {
//            _state.update { it.copy(currentVisitRelatedError = e.message) }
//        }
//    }
//
//    private suspend fun getComplaintsSummaries() {
//        try {
//            val resource = getCurrentVisitMainComplaintUseCase(
//                _state.value.patientId,
//                _state.value.currentVisit
//            )
//            when (resource) {
//                is Resource.Success -> {
//                    _state.update { it.copy(mainComplaintsSummaries = resource.data) }
//                }
//
//                is Resource.Error -> {
//                    _state.update { it.copy(currentVisitRelatedError = resource.message) }
//                }
//
//                else -> Unit
//            }
//        } catch (e: Exception) {
//            _state.update { it.copy(currentVisitRelatedError = e.message) }
//        }
//
//    }
//
//    private suspend fun getPatientDemographics() {
//        try {
//            val resource = patientRepository.getPatientById(_state.value.patientId).first {
//                it is Resource.Success || it is Resource.Error
//            }
//
//            when (resource) {
//                is Resource.Success -> {
//                    _state.update { it.copy(patient = resource.data) }
//                }
//
//                is Resource.Error -> {
//                    _state.update { it.copy(currentVisitRelatedError = resource.message) }
//                }
//
//                else -> Unit
//            }
//        } catch (e: Exception) {
//            _state.update { it.copy(currentVisitRelatedError = e.message) }
//        }
//    }
//
//    private suspend fun getCurrentVisit() {
//        try {
//            val visit = getCurrentVisitUseCase(_state.value.patientId)
//            _state.update { it.copy(currentVisit = visit) }
//        } catch (e: Exception) {
//            _state.update { it.copy(currentVisitRelatedError = e.message) }
//        }
//    }
//
//    fun onEvent(event: PatientDetailsEvent){
//
//        viewModelScope.launch(dispatcher + errorHandler){
//            when (event) {
//                PatientDetailsEvent.OnRetryPatientDetails -> getPatientDemographics()
//                PatientDetailsEvent.OnRetryCurrentVisitRelated -> getComplaintsSummaries()
//            }
//        }
//
//    }
//}