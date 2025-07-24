package com.unimib.oases.ui.screen.bluetooth.sending

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.usecase.SendPatientViaBluetoothUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendPatientViaBluetoothViewModel @Inject constructor(
    private val useCase: SendPatientViaBluetoothUseCase,
    bluetoothCustomManager: BluetoothCustomManager
) : ViewModel(){

    val pairedDevices = bluetoothCustomManager.pairedDevices

    private val _sendPatientResult = MutableStateFlow<Resource<Unit>>(Resource.None())
    val sendPatientResult: MutableStateFlow<Resource<Unit>> = _sendPatientResult

    init {
        bluetoothCustomManager.fetchPairedDevices()
    }

    fun onEvent(event: SendPatientViaBluetoothEvent) {
        when (event) {
            is SendPatientViaBluetoothEvent.SendPatient -> sendPatient(event.patient, event.device)
            is SendPatientViaBluetoothEvent.SendResultShown -> resetSendPatientResult()
        }
    }

    private fun sendPatient(patient: Patient, device: BluetoothDevice) {
        viewModelScope.launch {
            _sendPatientResult.value = Resource.Loading()
            _sendPatientResult.value = useCase(patient, device)
        }
    }

    private fun resetSendPatientResult(){
        _sendPatientResult.value = Resource.None()
    }
}