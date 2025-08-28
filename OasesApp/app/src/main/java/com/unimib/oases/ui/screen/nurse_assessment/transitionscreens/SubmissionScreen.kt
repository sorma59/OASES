package com.unimib.oases.ui.screen.nurse_assessment.transitionscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.ui.components.util.CenteredText

@Composable
fun SubmissionScreen(){
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

    }
}