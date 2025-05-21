package com.unimib.oases.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.screen.admin_screen.AdminScreen
import com.unimib.oases.ui.screen.admin_screen.diseases_management.DiseaseManagementScreen
import com.unimib.oases.ui.screen.admin_screen.user_management.UserManagementScreen
import com.unimib.oases.ui.screen.admin_screen.vital_signs_management.VitalSignManagementScreen
import com.unimib.oases.ui.screen.bluetooth.pairing.PairNewDeviceScreen
import com.unimib.oases.ui.screen.bluetooth.sending.SendPatientViaBluetoothScreen
import com.unimib.oases.ui.screen.homepage.HomeScreen
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.login.LoginScreen
import com.unimib.oases.ui.screen.medical_visit.MedicalVisitScreen
import com.unimib.oases.ui.screen.patient_registration.RegistrationScreen
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    padding: PaddingValues,
    bluetoothCustomManager: BluetoothCustomManager
){

    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        //  nurse
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {

        composable(Screen.AdminScreen.route) {
            AdminScreen(navController, padding, authViewModel)
        }

        composable(Screen.VitalSignsManagementScreen.route) {
            VitalSignManagementScreen(navController, padding)
        }

        composable(Screen.UserManagementScreen.route) {
            UserManagementScreen (navController, padding)
        }

        composable(Screen.DiseaseManagementScreen.route) {
            DiseaseManagementScreen (navController, padding)
        }



        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, padding, authViewModel)
        }

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, padding, authViewModel, bluetoothCustomManager)
        }

        composable(Screen.RegistrationScreen.route) {
            RegistrationScreen(navController, padding)
        }

        composable(Screen.SendPatient.route){
            // Get the patient from SavedStateHandle
            val patient = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<Patient>("patient")

            if (patient != null) {
                SendPatientViaBluetoothScreen(
                    patient = patient,
                    navController = navController,
                    padding = padding
                )
            }
        }

        composable(Screen.PairDevice.route){
            PairNewDeviceScreen(navController, padding)
        }
        composable(Screen.MedicalVisitScreen.route) {
            MedicalVisitScreen(navController, authViewModel, padding)
        }
    }
}