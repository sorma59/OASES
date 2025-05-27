package com.unimib.oases.ui.screen.patient_registration.visit_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.PatientUseCase
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
class VisitHistoryViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): ViewModel() {

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    private val _state = MutableStateFlow(VisitHistoryState())
    val state: StateFlow<VisitHistoryState> = _state.asStateFlow()

    internal fun loadVisits(patientId: String) {

        viewModelScope.launch(ioDispatcher + errorHandler) {
            _state.update { it.copy(isLoading = true) }

            patientUseCase.getPatientVisits(patientId).collect { visits ->
                _state.update { it.copy(visits = visits.data ?: emptyList()) }
            }

            _state.update { it.copy(isLoading = false) }
        }

    }
}