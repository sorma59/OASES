package com.unimib.oases.ui.screen.admin_screen.vital_signs_management

import com.unimib.oases.domain.model.VitalSign

sealed class VitalSignManagementEvent {
    data class EnteredVitalSignName(val value: String) : VitalSignManagementEvent()
    data class EnteredVitalSignAcronym(val value: String) : VitalSignManagementEvent()
    data class EnteredVitalSignUnit(val value: String) : VitalSignManagementEvent()
    data class Delete(val value: VitalSign) : VitalSignManagementEvent()
    data class Click(val value: VitalSign) : VitalSignManagementEvent()
    data object SaveVitalSign : VitalSignManagementEvent()
    data object UndoDelete: VitalSignManagementEvent()
}