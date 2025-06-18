package com.unimib.oases.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.util.AuthStrings

@Composable
fun LoginScreen(
    navController: NavController,
    padding: PaddingValues,
    authViewModel: AuthViewModel,
) {

    val authState = authViewModel.authState.observeAsState()

    val context = LocalContext.current

    if (authViewModel.currentUser() != null) {
        when (authViewModel.currentUser()?.role) {
            Role.Admin -> navController.navigate(Screen.AdminScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }

            else -> navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        }
        return
    }


    var username by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated ->
                when (authViewModel.currentUser()?.role) {
                    Role.Admin -> navController.navigate(Screen.AdminScreen.route)
                    else -> navController.navigate(Screen.HomeScreen.route)
                }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 20.dp)
            .consumeWindowInsets(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Row(modifier = Modifier.padding(padding)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(250.dp)
                )


                Text(text = "OASES", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))


        if (authState.value == AuthState.Loading) {
            CustomCircularProgressIndicator()
        } else {
                Row {
                    Column {
                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(text = AuthStrings.USERNAME)
                            },
                            shape = RoundedCornerShape(10.dp),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(text = AuthStrings.PASSWORD)
                            },
                            shape = RoundedCornerShape(10.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

                            trailingIcon = {
                                val image =
                                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = "PASSWORD")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                authViewModel.authenticate(username, password)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(top = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            //enabled = authState.value != AuthState.Loading
                        ) {
                            Text(text = "LOGIN", color = MaterialTheme.colorScheme.surface)
                        }
                    }
                }
            }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview(){
//    OasesTheme {
//        LoginScreen(navController = rememberNavController(), padding = PaddingValues(0.dp), authViewModel = )
//    }
//}
