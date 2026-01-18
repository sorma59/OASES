package com.unimib.oases.ui.screen.bluetooth.sending

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.unimib.oases.bluetooth.BluetoothCustomManager
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.usecase.SendPatientViaBluetoothUseCase
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.firstSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendPatientViaBluetoothViewModel @Inject constructor(
    private val sendPatientViaBluetoothUseCase: SendPatientViaBluetoothUseCase,
    private val bluetoothCustomManager: BluetoothCustomManager,
    private val visitRepository: VisitRepository,
    savedStateHandle: SavedStateHandle,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel(){

    private val args = savedStateHandle.toRoute<Route.SendPatient>()

    private val _state = MutableStateFlow(
        SendPatientViaBluetoothState(
            patientId = args.patientId,
            visitId = args.visitId
        )
    )
    val state: StateFlow<SendPatientViaBluetoothState> = _state.asStateFlow()

    init {
        bluetoothCustomManager.fetchPairedDevices()
        observePairedDevices()
        getPatientAndVisitData()
    }

    private fun observePairedDevices() {
        viewModelScope.launch {
            bluetoothCustomManager.pairedDevices.collect { devices ->
                _state.update { it.copy(pairedDevices = devices) }
            }
        }
    }

    private fun getPatientAndVisitData() {

        viewModelScope.launch(dispatcher) {
            try {

                updatePatientRetrievalState { it.copy(isLoading = true, error = null) }

                val patientWithVisitInfo = visitRepository
                    .getVisitWithPatientInfo(state.value.visitId)
                    .firstSuccess()
                _state.update{
                    it.copy(patientWithVisitInfo = patientWithVisitInfo)
                }

            }
            catch (e: Exception) {
                updatePatientRetrievalState { it.copy(error = e.message) }
            } finally {
                updatePatientRetrievalState { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: SendPatientViaBluetoothEvent) {
        when (event) {
            is SendPatientViaBluetoothEvent.SendPatient -> {
                val patient = state.value.patientWithVisitInfo?.patient
                check(patient != null) {
                    "Patient and Visit cannot be null here"
                }
                sendPatient(patient, event.device)
            }
            SendPatientViaBluetoothEvent.SendResultShown -> resetSendPatientResult()
            SendPatientViaBluetoothEvent.OnToastShown -> _state.update { it.copy(toastMessage = null) }
            SendPatientViaBluetoothEvent.PatientItemClicked -> {
                if (_state.value.patientWithVisitInfo == null)
                    getPatientAndVisitData()
            }
        }
    }

    private fun sendPatient(patient: Patient, device: BluetoothDevice) {
        viewModelScope.launch {
            updatePatientSendingState { it.copy(isLoading = true) }
            when(val outcome = sendPatientViaBluetoothUseCase(patient.id, device)){
                is Outcome.Success -> {
                    updatePatientSendingState { it.copy(result = "Patient sent successfully, tap another device to send ${patient.name} again") }
                }
                is Outcome.Error -> {
                    updatePatientSendingState { it.copy(result = "Failed to send patient: ${outcome.message}") }
                }
            }
            updatePatientSendingState { it.copy(isLoading = false) }
        }
    }

    private fun resetSendPatientResult(){
        updatePatientSendingState { it.copy(result = null) }
    }

    private fun updatePatientRetrievalState(update: (PatientRetrievalState) -> PatientRetrievalState){
        _state.update { it.copy(patientRetrievalState = update(it.patientRetrievalState)) }
    }

    private fun updatePatientSendingState(update: (PatientSendingState) -> PatientSendingState){
        _state.update { it.copy(patientSendingState = update(it.patientSendingState)) }
    }
}