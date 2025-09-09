package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import com.unimib.oases.util.datastructure.binarytree.ComplaintId
import com.unimib.oases.util.datastructure.binarytree.Diarrhea
import com.unimib.oases.util.datastructure.binarytree.LeafNode
import com.unimib.oases.util.datastructure.binarytree.ManualNode
import com.unimib.oases.util.datastructure.binarytree.evaluate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltViewModel
class MainComplaintViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val patientRepository: PatientRepository
): ViewModel() {

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message
            )
        }
    }

    private val _state = MutableStateFlow(
        MainComplaintState(
            receivedId = savedStateHandle["patientId"]!!,
            complaintId = savedStateHandle["complaintId"]!!
        )
    )
    val state: StateFlow<MainComplaintState> = _state.asStateFlow()

    init {

        getPatientData()

        when(_state.value.complaintId){
            ComplaintId.DIARRHEA.id -> {
                viewModelScope.launch(dispatcher) {

                    delay(500) // let the patient be found

                    val patient = _state.value.patient
                    patient?.let{
                        val age = it.ageInMonths / 12

                        Diarrhea(age).evaluate(
                            onManual = { node ->
                                val answer = awaitUserInput { callback ->
                                    openNode(node, callback)
                                }

                                return@evaluate answer
                            },
                            onLeafReached = {
                                showTherapy(it)
                            }
                        )
                    } ?: run {
                        _state.update{
                            it.copy(
                                error =  "Patient not found"
                            )
                        }
                    }
                }
            }
            else -> {
                _state.update{
                    it.copy(
                        error = "Complaint data not found"
                    )
                }
            }
        }
    }

    private fun getPatientData() {

        viewModelScope.launch(dispatcher + errorHandler) {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val resource = patientRepository.getPatientById(_state.value.receivedId).first {
                    it is Resource.Success || it is Resource.Error
                }

                when (resource) {
                    is Resource.Success -> {
                        _state.update { it.copy(patient = resource.data) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(error = resource.message) }
                    }
                    else -> Unit
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun openNode(
        node: ManualNode,
        callback: (Boolean) -> Unit
    ){
        _state.update {
            it.copy(
                questions = it.questions + QuestionState(
                    node = node,
                    answer = null,
                    callback = callback
                )
            )
        }
    }

    private fun showTherapy(node: LeafNode){
        node.value?.let { treatmentPlan ->
            _state.update {
                it.copy(
                    treatmentPlan = treatmentPlan
                )
            }
        }
    }

    private suspend fun awaitUserInput(onRequestInput: (continuation: (Boolean) -> Unit) -> Unit): Boolean {
        return suspendCancellableCoroutine { continuation ->
            // Tell the UI: "I'm waiting for input"
            onRequestInput { input ->
                continuation.resume(input) { cause, _, _ -> {
                    Log.e("MainComplaintViewModel", "awaitUserInput: $cause")
                } } // resume when input is provided
            }
        }
    }

    fun onEvent(event: MainComplaintEvent){
        when(event){
            is MainComplaintEvent.NodeAnswered -> {
                _state.update {
                    it.copy(
                        questions = it.questions.map { question ->
                            if (question.node == event.node) {
                                question.copy(answer = event.answer)
                            } else {
                                question
                            }
                        }
                    )
                }
            }

            MainComplaintEvent.NodeAnsweredAgain -> {
                _state.update {
                    it.copy(
                        toastMessage = "You cannot change your answer once it has been provided."
                    )
                }
            }

            MainComplaintEvent.ToastShown -> {
                _state.update {
                    it.copy(
                        toastMessage = null
                    )
                }
            }
        }
    }
}

