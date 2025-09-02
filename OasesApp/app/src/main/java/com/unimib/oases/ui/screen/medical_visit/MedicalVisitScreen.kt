package com.unimib.oases.ui.screen.medical_visit


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.screen.login.AuthViewModel


@Composable
fun MedicalVisitScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        CenteredText("Beginning of the medical visit")
    }

}