package com.unimib.oases.ui.screen.nurse_assessment.transitionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.ui.components.util.CenteredText

@Composable
fun SubmissionScreen(
    onSubmit: () -> Unit,
    onBack: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        CenteredText(
            "End of nurse assessment",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        CenteredText(
            "Do you want to submit everything?",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(64.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            OutlinedButton(
                onClick = onBack
            ){
                Text("Back")
            }
            Button(
                onClick = onSubmit
            ){
                Text("Submit")
            }
        }

    }
}