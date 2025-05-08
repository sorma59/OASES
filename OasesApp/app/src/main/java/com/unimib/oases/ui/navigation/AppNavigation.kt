package com.unimib.oases.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.screen.admin_screen.AdminScreen
import com.unimib.oases.ui.screen.bluetooth.pairing.PairNewDeviceScreen
import com.unimib.oases.ui.screen.bluetooth.sending.SendPatientViaBluetoothScreen
import com.unimib.oases.ui.screen.homepage.HomeScreen
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.login.LoginScreen
import com.unimib.oases.ui.screen.medical_visit.MedicalVisitScreen
import com.unimib.oases.ui.screen.patient_registration.RegistrationScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    padding: PaddingValues,
){

    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
//  nurse
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {

        composable(Screen.AdminScreen.route) {
            AdminScreen(navController, padding)
        }


        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, padding, authViewModel)
        }

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, padding, authViewModel)
        }

        composable(Screen.RegistrationScreen.route) {
            RegistrationScreen(navController, padding, authViewModel)
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
                    navController = navController
                )
            }
        }

        composable(Screen.PairDevice.route){
            PairNewDeviceScreen(navController)
        }
        composable(Screen.MedicalVisitScreen.route) {
            MedicalVisitScreen(navController, authViewModel)
        }
    }
}