package com.unimib.oases.ui.navigation

import com.unimib.oases.ui.components.scaffold.OasesTopAppBarType

sealed class Screen(val route: String, val title: String, val type: OasesTopAppBarType = OasesTopAppBarType.BACK) {
    // Doctor or Nurse
    object Home: Screen("home", "OASES", OasesTopAppBarType.MENU)
    object PatientRegistration: Screen("patient_registration", "Patient Registration")
    object SendPatient: Screen("send_patient", "Send Patient")
    object MedicalVisit: Screen("medical_visit", "Medical Visit")
    object PatientDashboard: Screen("patient_dashboard", "Patient Dashboard")
    object ViewPatientDetails: Screen("view_patient_details", "Patient Details")
    object PastMedicalHistoryScreen: Screen("past_medical_history_screen", "Past Medical History")
    object MainComplaintScreen: Screen("main_complaint_screen", "Main Complaint")

    // Admin
    object AdminDashboard: Screen("admin_dashboard", "Admin Panel", OasesTopAppBarType.MENU)
    object DiseaseManagement: Screen("disease_management", "Diseases Management")
    object UserManagement: Screen("user_management", "Users Management")
    object VitalSignsManagement: Screen("vital_signs_management", "Vital Signs Management")

    // Common
    object PairDevice: Screen("pair_device", "Pair Device")

    companion object {
        val screens: Map<String, Screen> by lazy {
            mapOf(
                Home.route to Home,
                PatientRegistration.route to PatientRegistration,
                SendPatient.route to SendPatient,
                MedicalVisit.route to MedicalVisit,
                PatientDashboard.route to PatientDashboard,
                ViewPatientDetails.route to ViewPatientDetails,
                PastMedicalHistoryScreen.route to PastMedicalHistoryScreen,
                MainComplaintScreen.route to MainComplaintScreen,

                AdminDashboard.route to AdminDashboard,
                DiseaseManagement.route to DiseaseManagement,
                UserManagement.route to UserManagement,
                VitalSignsManagement.route to VitalSignsManagement,

                PairDevice.route to PairDevice,
            )
        }

        fun screenOf(route: String?): Screen =
            screens[route?.substringBefore("/")?.substringBefore("?")]!!
    }
}