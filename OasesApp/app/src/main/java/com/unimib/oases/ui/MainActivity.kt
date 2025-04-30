package com.unimib.oases.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.ui.login.LoginScreen
import com.unimib.oases.ui.theme.OasesTheme
import com.unimib.oases.ui.util.AppNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OasesTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    AppNavigation(navController)
                }
            }
        }
    }
}