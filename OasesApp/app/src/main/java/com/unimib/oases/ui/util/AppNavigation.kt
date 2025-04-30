package com.unimib.oases.ui.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unimib.oases.ui.home_page.HomeScreen
import com.unimib.oases.ui.login.LoginScreen
import com.unimib.oases.ui.patient_registration.RegistrationScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = Screen.LoginScreen.route) {

        composable(Screen.LoginScreen.route){
            LoginScreen(navController)
        }

        composable(Screen.HomeScreen.route){
            HomeScreen(navController)
        }

        composable(Screen.RegistrationScreen.route){
            RegistrationScreen(navController)
        }

    }
}