package com.unimib.oases.ui.screen.admin_screen.diseases_management

import com.unimib.oases.domain.model.Disease

sealed class DiseaseManagementEvent {
    data class EnteredDiseaseName(val value: String) : DiseaseManagementEvent()
    data class EnteredSexSpecificity(val value: String) : DiseaseManagementEvent()
    data class EnteredAgeSpecificity(val value: String) : DiseaseManagementEvent()
    data class Delete(val value: Disease) : DiseaseManagementEvent()
    data class Click(val value: Disease) : DiseaseManagementEvent()
    data object SaveDisease : DiseaseManagementEvent()
    data object UndoDelete: DiseaseManagementEvent()
}