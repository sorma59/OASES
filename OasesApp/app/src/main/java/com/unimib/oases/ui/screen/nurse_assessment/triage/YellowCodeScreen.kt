package com.unimib.oases.ui.screen.nurse_assessment.triage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.util.ToastUtils

@Composable
fun YellowCodeScreen(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
) {

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    LaunchedEffect(state.toastMessage) {
        if (state.toastMessage != null) {
            ToastUtils.showToast(context, state.toastMessage)
            onEvent(TriageEvent.ToastShown)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "Yellow Code",
                tint = Color.Yellow,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "YELLOW Code",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (state.error != null){
            RetryButton(
                error = state.error,
                onClick = { onEvent(TriageEvent.Retry) }
            )
        }

        else {
            Box{
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {

                    state.triageConfig!!.yellowOptions.forEach {
                        val id = it.symptom.symptomId.value.string
                        LabeledCheckbox(
                            label = it.label,
                            checked = state.selectedYellows.contains(id),
                            onCheckedChange = { onEvent(TriageEvent.FieldToggled(id)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                }

                FadeOverlay(Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}