package com.unimib.oases.ui.screen.bluetooth.sending

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.bluetooth.BluetoothCustomManager
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.SendPatientViaBluetoothUseCase
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendPatientViaBluetoothViewModel @Inject constructor(
    private val sendPatientViaBluetoothUseCase: SendPatientViaBluetoothUseCase,
    private val bluetoothCustomManager: BluetoothCustomManager,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val patientRepository: PatientRepository,
) : ViewModel(){

    private val _state = MutableStateFlow(
        SendPatientViaBluetoothState(patientId = savedStateHandle["patientId"]!!)
    )
    val state: StateFlow<SendPatientViaBluetoothState> = _state.asStateFlow()

    init {
        bluetoothCustomManager.fetchPairedDevices()
        observePairedDevices()
        getPatientData()
    }

    private fun observePairedDevices() {
        viewModelScope.launch {
            bluetoothCustomManager.pairedDevices.collect { devices ->
                _state.update { it.copy(pairedDevices = devices) }
            }
        }
    }

    private fun getPatientData() {

        viewModelScope.launch(dispatcher) {
            try {

                updatePatientRetrievalState { it.copy(isLoading = true, error = null) }

                val resource = patientRepository.getPatientById(_state.value.patientId).first {
                    it is Resource.Success || it is Resource.Error
                }

                when (resource) {
                    is Resource.Success -> {
                        _state.update{
                            it.copy(patient = resource.data)
                        }
                    }
                    is Resource.Error -> {
                        updatePatientRetrievalState {
                            it.copy(error = resource.message)
                        }
                    }
                    else -> Unit
                }
            } catch (e: Exception) {
                updatePatientRetrievalState { it.copy(error = e.message) }
            } finally {
                updatePatientRetrievalState { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: SendPatientViaBluetoothEvent) {
        when (event) {
            is SendPatientViaBluetoothEvent.SendPatient -> {
                val patient = _state.value.patient
                patient?.let { sendPatient(patient, event.device) }
                    ?: run {
                        _state.update { it.copy(toastMessage = "Patient data is missing") }
                    }
            }
            SendPatientViaBluetoothEvent.SendResultShown -> resetSendPatientResult()
            SendPatientViaBluetoothEvent.OnToastShown -> _state.update { it.copy(toastMessage = null) }
            SendPatientViaBluetoothEvent.PatientItemClicked -> {
                if (_state.value.patient == null)
                    getPatientData()
            }
        }
    }

    private fun sendPatient(patient: Patient, device: BluetoothDevice) {
        viewModelScope.launch {
            updatePatientSendingState { it.copy(isLoading = true) }
            val resource = sendPatientViaBluetoothUseCase(patient.id, device)
            when(resource){
                is Outcome.Success -> {
                    updatePatientSendingState { it.copy(result = "Patient sent successfully, tap another device to send ${patient.name} again") }
                }
                is Outcome.Error -> {
                    updatePatientSendingState { it.copy(result = "Failed to send patient: ${resource.message}") }
                }
                else -> Unit
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