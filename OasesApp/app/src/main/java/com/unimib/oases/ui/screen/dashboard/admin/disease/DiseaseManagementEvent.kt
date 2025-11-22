package com.unimib.oases.ui.screen.dashboard.admin.disease

import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.SexSpecificity

sealed class DiseaseManagementEvent {
    data class EnteredDiseaseName(val value: String) : DiseaseManagementEvent()
    data class EnteredSexSpecificity(val sexSpecificity: SexSpecificity) : DiseaseManagementEvent()
    data class EnteredAgeSpecificity(val ageSpecificity: AgeSpecificity) : DiseaseManagementEvent()
    data class Delete(val value: Disease) : DiseaseManagementEvent()
    data class Click(val value: Disease) : DiseaseManagementEvent()
    data object SaveDisease : DiseaseManagementEvent()
    data object UndoDelete: DiseaseManagementEvent()
}