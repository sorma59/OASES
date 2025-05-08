package com.unimib.oases.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unimib.oases.ui.screen.home_page.HomeScreen
import com.unimib.oases.ui.screen.login.LoginScreen
import com.unimib.oases.ui.screen.patient_registration.RegistrationScreen
import com.unimib.oases.ui.screen.admin_screen.AdminScreen
import com.unimib.oases.ui.screen.medical_visit.MedicalVisitScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    padding: PaddingValues,
){


        NavHost(
           // modifier = Modifier
               // .consumeWindowInsets(padding)
              //  .padding(padding),
            navController = navController,
            startDestination = Screen.LoginScreen.route
        ) {

            composable(Screen.AdminScreen.route) {
                AdminScreen(navController, padding)
            }


            composable(Screen.LoginScreen.route) {
                LoginScreen(navController, padding)
            }

            composable(Screen.HomeScreen.route) {
                HomeScreen(navController, padding)
            }

            composable(Screen.RegistrationScreen.route) {
                RegistrationScreen(navController, padding)
            }

            composable(Screen.MedicalVisitScreen.route) {
                MedicalVisitScreen(navController)
            }
        }

}