package com.unimib.oases.ui.components.scaffold

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.login.LoginScreen

@Composable
fun LoginScaffold(authViewModel: AuthViewModel){
    Scaffold { padding ->
        LoginScreen(padding, authViewModel)
    }
}