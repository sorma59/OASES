package com.unimib.oases.ui.screen.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val patientRepository: PatientRepository
): ViewModel() {

    // ----------------------Patients-------------------------------
    private val _patients = MutableStateFlow<Resource<List<Patient>>>(Resource.None())
    val patients: StateFlow<Resource<List<Patient>>> = _patients

    val receivedPatients = patientRepository.receivedPatients

    init {
        loadPatients()
    }

    private fun loadPatients() {
        // Use viewModelScope to launch the coroutine for collecting data
        viewModelScope.launch {
            patientRepository.getPatients()
                .collect { patientsList ->
                    _patients.value = patientsList
                }
        }
    }

}