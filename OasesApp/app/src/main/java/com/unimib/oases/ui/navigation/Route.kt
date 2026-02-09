package com.unimib.oases.ui.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed class Route {
    // --- Routes with specific Top App Bar types ---

    /**
     * when adding a new Route make sure to add its title and type to the map in [com.unimib.oases.ui.components.scaffold.titlesAndTypesByClass]
     */
    @Serializable
    data object Home: Route()
    @Serializable
    data object InitialIntake: Route()
    @Serializable
    data object AdminDashboard: Route()
    @Serializable
    data object PatientRegistration: Route()

    @Serializable
    data class VitalSignsForm(
        val patientId: String,
        val visitId: String? = null,
        val isWizardMode: Boolean = false
    ) : Route()
    @Serializable
    data class Demographics(
        val patientId: String? = null
    ): Route()
    @Serializable
    data class Triage(
        val patientId: String,
        val visitId: String,
        val isWizardMode: Boolean = false
    ) : Route()
    @Serializable
    data class VitalSigns(
        val patientId: String,
        val visitId: String,
        val isWizardMode: Boolean = false
    ): Route()
    @Serializable
    data class MalnutritionScreening(
        val patientId: String,
        val visitId: String,
        val isWizardMode: Boolean = false
    ) : Route()
    @Serializable
    data class History(
        val patientId: String
    ): Route()
    @Serializable
    data class SendPatient(
        val patientId: String,
        val visitId: String
    ): Route()
    @Serializable
    data class MedicalVisit(val patientId: String): Route()
    @Serializable
    data class PatientDashboard(
        val patientId: String,
        val visitId: String
    ): Route()
    @Serializable
    data class ViewPatientDetails(val patientId: String): Route()
    @Serializable
    data class MainComplaint(
        val patientId: String,
        val mainComplaintId: String
    ): Route()
    @Serializable
    data object DiseaseManagement: Route()
    @Serializable
    data object UserManagement: Route()
    @Serializable
    data object VitalSignsManagement: Route()
    @Serializable
    data object RoomsManagement: Route()
    @Serializable
    data object PairDevice: Route()

    /**
     * when adding a new Route make sure to add its title and type to the map in [com.unimib.oases.ui.components.scaffold.titlesAndTypesByClass]
     */
}