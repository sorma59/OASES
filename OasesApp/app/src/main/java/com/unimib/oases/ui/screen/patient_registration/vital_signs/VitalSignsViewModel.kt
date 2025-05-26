package com.unimib.oases.ui.screen.patient_registration.vital_signs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.VitalSignUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalSignsViewModel @Inject constructor(
    private val useCases: VitalSignUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    private val _state = MutableStateFlow(VitalSignsState())
    val state: StateFlow<VitalSignsState> = _state.asStateFlow()

    init {
        loadVitalSigns()
    }

    private fun loadVitalSigns() {
        viewModelScope.launch(ioDispatcher + errorHandler) {

            _state.update { it.copy(isLoading = true) }

            useCases.getVitalSigns().collect { vitalSigns ->

                if (vitalSigns is Resource.Success){

                    for (vitalSign in vitalSigns.data!!){
                        _state.update {
                            it.copy(
                                vitalSigns = it.vitalSigns + PatientVitalSignState(vitalSign.name)
                            )
                        }
                    }

                } else if (vitalSigns is Resource.Error){
                    _state.update { it.copy(error = vitalSigns.message) }
                }

                _state.update { it.copy(isLoading = false) }
            }

        }
    }

    fun onEvent(event: VitalSignsEvent) {
        when (event) {
            is VitalSignsEvent.ValueChanged -> {
                _state.update {
                    it.copy(
                        vitalSigns = it.vitalSigns.map {
                            if (it.vitalSign == event.vitalSign) {
                                it.copy(value = event.value)
                            } else {
                                it
                            }
                        }
                    )
                }
            }

            is VitalSignsEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {

        if (validate()){
            TODO("Validate Value: from string to double")
        }

    }

    private fun validate(): Boolean {
        return false
    }
}
