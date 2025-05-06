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

@Composable
fun AppNavigation(
    navController: NavHostController,
    padding: PaddingValues
){
    NavHost(
        modifier = Modifier
            .consumeWindowInsets(padding)
            .padding(padding),
        navController = navController,
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