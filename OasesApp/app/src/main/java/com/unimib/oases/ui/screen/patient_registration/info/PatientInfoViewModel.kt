package com.unimib.oases.ui.screen.patient_registration.info

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.InsertPatientLocallyUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientInfoViewModel @Inject constructor(
    private val validatePatientInfoFormUseCase: ValidatePatientInfoFormUseCase,
    private val useCases: PatientUseCase,
    private val insertPatientLocallyUseCase: InsertPatientLocallyUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(PatientInfoState())
    val state: StateFlow<PatientInfoState> = _state.asStateFlow()

    // Abbiamo bisogno di due tipi di eventi di validazione per distinguere
    // tra validazione del form e successo della sottomissione finale.
    private val validationEventsChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventsChannel.receiveAsFlow()

    private var currentPatientId: String? = null

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
        // In caso di errore nel salvataggio finale, potresti voler mostrare una Snackbar
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.showSnackbar(
                    message = e.message ?: "An unexpected error occurred during submission."
                )
            )
        }
    }

    init {
        savedStateHandle.get<String>("patientId")?.let { id ->
            if(id.isNotBlank()) {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.getPatient(id).also { result ->
                        result.collect { resource ->
                            when (resource) {
                                is Resource.Loading -> {
                                    _state.value = _state.value.copy(isLoading = true)
                                }
                                is Resource.Success -> {
                                    _state.value = _state.value.copy(
                                        patient = resource.data!!,
                                        isLoading = false
                                    )
                                    currentPatientId = id
                                }
                                is Resource.Error -> {
                                    _state.value = _state.value.copy(
                                        error = resource.message,
                                        isLoading = false
                                    )
                                }
                                is Resource.None -> {}
                            }
                        }
                    }
                }
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    patient = _state.value.patient
                )
            }
        }
    }

    sealed class UiEvent {
        data class showSnackbar(val message: String) : UiEvent()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()

    fun onEvent(event: PatientInfoEvent) {
        when(event) {
            is PatientInfoEvent.NameChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(name = event.name), nameError = null)
            }
            is PatientInfoEvent.AgeChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(age = event.age), ageError = null)
            }
            is PatientInfoEvent.SexChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(sex = event.sex))
            }
            is PatientInfoEvent.VillageChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(village = event.village))
            }
            is PatientInfoEvent.ParishChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(parish = event.parish))
            }
            is PatientInfoEvent.SubCountyChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(subCounty = event.subCounty))
            }
            is PatientInfoEvent.DistrictChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(district = event.district))
            }
            is PatientInfoEvent.NextOfKinChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(nextOfKin = event.nextOfKin))
            }
            is PatientInfoEvent.ContactChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(contact = event.contact))
            }

            // Nuovo evento: solo per validare il form
            is PatientInfoEvent.ValidateForm -> {
                Log.d("PatientInfoViewModel", "ValidateForm event received")
                validateAndPrepareForSubmission()
            }

            // Nuovo evento: per la sottomissione finale dopo la conferma dell'alert
            is PatientInfoEvent.ConfirmSubmission -> {
                Log.d("PatientInfoViewModel", "ConfirmSubmission event received, proceeding to save data.")
                savePatientData()
            }

        }
    }

    // Funzione per validare i dati e, se validi, chiedere conferma all'utente
    private fun validateAndPrepareForSubmission() {
        viewModelScope.launch(dispatcher + errorHandler) {
            Log.d("PatientInfoViewModel", "validateAndPrepareForSubmission called")

            // Esegui qui la validazione del form
            val result = validatePatientInfoFormUseCase.invoke(
                name = _state.value.patient.name,
                age = _state.value.patient.age
                // Aggiungi qui tutti gli altri campi da validare
            )

            // Aggiorna lo stato con gli errori di validazione
            _state.value = _state.value.copy(
                nameError = result.nameErrorMessage,
                ageError = result.ageErrorMessage
                // Aggiorna anche gli errori per gli altri campi
            )

            // Se la validazione non ha avuto successo, non procedere oltre
            if (!result.successful) {
                Log.d("PatientInfoViewModel", "Validation failed, showing errors.")
                return@launch // Esci dalla coroutine
            }

            // Se la validazione ha successo, notifica la UI di mostrare l'AlertDialog
            Log.d("PatientInfoViewModel", "Validation successful, sending ValidationSuccess event.")
            validationEventsChannel.send(ValidationEvent.ValidationSuccess)
        }
    }

    // Funzione per salvare i dati del paziente nel database
    private fun savePatientData() {
        viewModelScope.launch(dispatcher + errorHandler) {
            _state.value = _state.value.copy(isLoading = true)
            Log.d("PatientInfoViewModel", "savePatientData called, saving patient: ${_state.value.patient.name}")

            try {
                // Esegui l'inserimento/aggiornamento del paziente nel database
                insertPatientLocallyUseCase(_state.value.patient)

                // Notifica alla UI che il salvataggio finale è avvenuto con successo
                Log.d("PatientInfoViewModel", "Patient data saved successfully, sending SubmissionSuccess event.")
                validationEventsChannel.send(ValidationEvent.SubmissionSuccess)

            } catch (e: Exception) {
                Log.e("PatientInfoViewModel", "Error saving patient data: ${e.message}", e)
                // Gestisci l'errore, magari inviando un evento snackbar
                _eventFlow.emit(UiEvent.showSnackbar(message = "Failed to save patient data: ${e.message}"))
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    // Modifica la classe ValidationEvent per distinguere i due tipi di successo
    sealed class ValidationEvent {
        data object ValidationSuccess : ValidationEvent() // Validazione del form completata con successo
        data object SubmissionSuccess : ValidationEvent() // Salvataggio finale sul DB completato con successo
    }
}