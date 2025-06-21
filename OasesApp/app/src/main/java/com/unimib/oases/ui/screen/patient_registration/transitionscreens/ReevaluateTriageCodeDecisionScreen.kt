package com.unimib.oases.ui.screen.patient_registration.transitionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.components.util.CenteredText

@Composable
fun ReevaluateTriageCodeDecisionScreen(
    visit: Visit,
    onConfirm: () -> Unit,
    onDenial: () -> Unit
) {
    var triageCode =
        when(visit.triageCode) {
            TriageCode.YELLOW.name -> Color.Yellow
            TriageCode.RED.name -> Color.Red
            TriageCode.GREEN.name -> Color.Green
            else -> Color.Gray
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        CenteredText(
            "Do you want to also reevaluate the triage code?",
            fontSize = 20.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Current: ")

            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "Current triage code",
                tint = triageCode,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            ReevaluateTriageCodeButton(onClick = onConfirm)
            DoNotReevaluateTriageCodeButton(onClick = onDenial)
        }

    }
}

@Composable
fun DoNotReevaluateTriageCodeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick
    ){
        Text("Save changes")
    }
}

@Composable
fun ReevaluateTriageCodeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick
    ){
        Text("Reevaluate triage")
    }
}