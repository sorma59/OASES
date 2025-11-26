package com.unimib.oases.ui.screen.dashboard.admin.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.SexSpecificity
import com.unimib.oases.domain.usecase.DiseaseUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiseaseManagementViewModel @Inject constructor(
    private val useCases: DiseaseUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {


    private var getDiseasesJob: Job? = null

    private val _state = MutableStateFlow(DiseaseManagementState())
    val state: StateFlow<DiseaseManagementState> = _state

    private var undoDisease: Disease? = null
    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            it.copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    init {
        getDiseases()
    }

    fun onEvent(event: DiseaseManagementEvent) {
        when (event) {
            is DiseaseManagementEvent.Click -> {
                _state.update{
                    it.copy(
                        disease = event.value
                    )
                }
            }

            is DiseaseManagementEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.deleteDisease(event.value)
                    undoDisease = event.value
                }
            }

            is DiseaseManagementEvent.EnteredDiseaseName -> {
                _state.update{
                    it.copy(
                        disease = it.disease.copy(
                            name = event.value
                        )
                    )
                }
            }

            is DiseaseManagementEvent.EnteredSexSpecificity -> {
                _state.update{
                    it.copy(
                        disease = it.disease.copy(
                            sexSpecificity = event.sexSpecificity
                        )
                    )
                }
            }

            is DiseaseManagementEvent.EnteredAgeSpecificity -> {
                _state.update{
                    it.copy(
                        disease = it.disease.copy(
                            ageSpecificity = event.ageSpecificity
                        )
                    )
                }
            }

            DiseaseManagementEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.addDisease(undoDisease ?: return@launch)
                    undoDisease = null
                }
            }



            DiseaseManagementEvent.SaveDisease -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.update{
                            it.copy(isLoading = true)
                        }

                        useCases.addDisease(_state.value.disease)

                        _state.update{
                            it.copy(isLoading = false,
                                disease = Disease(
                                    name = "",
                                    sexSpecificity = SexSpecificity.ALL,
                                    ageSpecificity = AgeSpecificity.ALL
                                )
                            )
                        }

                    } catch (_: Exception) {
                        _state.update{
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun getDiseases() {
        getDiseasesJob?.cancel()

        getDiseasesJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = useCases.getDiseases()

            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                diseases = resource.data,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = resource.message,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.NotFound -> {
                        _state.update {
                            it.copy(
                                diseases = emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}




