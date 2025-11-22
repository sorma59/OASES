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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.button.RetryButton

@Composable
fun YellowCodeContent(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
) {

    val scrollState = rememberScrollState()

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

        state.error?.let {
            RetryButton(
                error = it,
                onClick = { onEvent(TriageEvent.Retry) }
            )
        } ?: state.editingState?.let {
            Box {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {

                    it.triageConfig.yellowOptions.forEach { (symptom, label) ->
                        val id = symptom.symptom.id
                        LabeledCheckbox(
                            label = label,
                            checked = it.triageData.selectedYellows.contains(id),
                            onCheckedChange = { onEvent(TriageEvent.FieldToggled(id)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                }

                FadeOverlay(Modifier.align(Alignment.BottomCenter))
            }
        } ?: RetryButton(
            error = "Failed to enter editing mode",
            onClick = { onEvent(TriageEvent.EditButtonPressed) }
        )
    }
}