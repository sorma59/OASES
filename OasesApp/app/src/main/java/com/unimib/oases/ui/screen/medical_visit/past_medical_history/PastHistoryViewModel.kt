package com.unimib.oases.ui.screen.medical_visit.past_medical_history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PastHistoryViewModel @Inject constructor() : ViewModel() {

    private val _chronicConditions = MutableStateFlow(ChronicConditionsCheckboxesState())
    val chronicConditions: StateFlow<ChronicConditionsCheckboxesState> = _chronicConditions

    private val _chronicConditionsDates = MutableStateFlow(ChronicConditionsDates())
    val chronicConditionsDates: StateFlow<ChronicConditionsDates> = _chronicConditionsDates

    private val _chronicConditionAdditionalInfo = MutableStateFlow(ChronicConditionAdditionalInfo())
    val chronicConditionAdditionalInfo: StateFlow<ChronicConditionAdditionalInfo> = _chronicConditionAdditionalInfo

    fun updateDiabetes(isChecked: Boolean) {
        _chronicConditions.value = _chronicConditions.value.copy(diabetes = isChecked)
    }

    fun updateAsthma(isChecked: Boolean) {
        _chronicConditions.value = _chronicConditions.value.copy(asthma = isChecked)
    }

    fun updateHyperTension(isChecked: Boolean) {
        _chronicConditions.value = _chronicConditions.value.copy(hypertension = isChecked)
    }

    fun updateArthritis(isChecked: Boolean) {
        _chronicConditions.value = _chronicConditions.value.copy(arthritis = isChecked)
    }

    fun updateDepression(isChecked: Boolean) {
        _chronicConditions.value = _chronicConditions.value.copy(depression = isChecked)
    }

    fun updateDiabetesDate(date: String) {
        _chronicConditionsDates.value = _chronicConditionsDates.value.copy(diabetes = date)
    }

    fun updateAsthmaDate(date: String) {
        _chronicConditionsDates.value = _chronicConditionsDates.value.copy(asthma = date)
    }

    fun updateHyperTensionDate(date: String) {
        _chronicConditionsDates.value = _chronicConditionsDates.value.copy(hypertension = date)
    }

    fun updateArthritisDate(date: String) {
        _chronicConditionsDates.value = _chronicConditionsDates.value.copy(arthritis = date)
    }

    fun updateDepressionDate(date: String) {
        _chronicConditionsDates.value = _chronicConditionsDates.value.copy(depression = date)
    }

    fun updateDiabetesInfo(info: String) {
        _chronicConditionAdditionalInfo.value = _chronicConditionAdditionalInfo.value.copy(diabetes = info)
    }

    fun updateAsthmaInfo(info: String) {
        _chronicConditionAdditionalInfo.value = _chronicConditionAdditionalInfo.value.copy(asthma = info)
    }

    fun updateHyperTensionInfo(info: String) {
        _chronicConditionAdditionalInfo.value = _chronicConditionAdditionalInfo.value.copy(hypertension = info)
    }

    fun updateArthritisInfo(info: String) {
        _chronicConditionAdditionalInfo.value = _chronicConditionAdditionalInfo.value.copy(arthritis = info)
    }

    fun updateDepressionInfo(info: String) {
        _chronicConditionAdditionalInfo.value = _chronicConditionAdditionalInfo.value.copy(depression = info)
    }
}