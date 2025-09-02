package com.unimib.oases.ui.components.scaffold

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.login.LoginScreen

@Composable
fun LoginScaffold(authViewModel: AuthViewModel){
    Scaffold { padding ->
        LoginScreen(
            authViewModel,
            Modifier
                .consumeWindowInsets(padding)
                .padding(padding)
        )
    }
}