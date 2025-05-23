package com.unimib.oases.ui.screen.patient_registration.continue_to_triage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.ui.components.util.CenteredText

@Composable
fun ContinueToTriageDecisionScreen(
    onContinueToTriage: () -> Unit,
    onSkipTriage: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        CenteredText(
            "The patient was saved, you will be able to edit it later on.\nDo you want to continue with their triage or go back to home?",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(64.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            ContinueToTriageButton(onClick = onContinueToTriage)
            SkipTriageButton(onClick = onSkipTriage)
        }

    }
}

@Composable
fun SkipTriageButton(onClick: () -> Unit) {
    Button(
        onClick = onClick
    ){
        Text("Go to Home")
    }
}

@Composable
fun ContinueToTriageButton(onClick: () -> Unit) {
    Button(
        onClick = onClick
    ){
        Text("Continue to triage")
    }
}