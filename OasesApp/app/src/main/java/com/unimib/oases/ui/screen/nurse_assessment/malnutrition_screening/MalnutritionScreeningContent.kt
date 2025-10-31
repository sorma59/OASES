package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.MuacCategory
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.button.RetryButton
import java.util.Locale

@Composable
fun MalnutritionScreeningContent(
    state: MalnutritionScreeningState,
    onEvent: (MalnutritionScreeningEvent) -> Unit
){
    if (state.error != null){
        RetryButton(
            error = state.error,
            onClick = { onEvent(MalnutritionScreeningEvent.Retry) }
        )
    }

    else {
        Column {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                AnimatedLabelOutlinedTextField(
                    value = state.weight,
                    onValueChange = { onEvent(MalnutritionScreeningEvent.WeightChanged(it)) },
                    labelText = "Weight (kg)",
                    isDouble = true

                )

                AnimatedLabelOutlinedTextField(
                    value = state.height,
                    onValueChange = { onEvent(MalnutritionScreeningEvent.HeightChanged(it)) },
                    labelText = "Height (cm)",
                    isDouble = true
                )

                Text(
                    "BMI: " + (state.bmi?.let { String.format(Locale.getDefault(), "%.1f", it) }
                        ?: "")
                )

                AnimatedLabelOutlinedTextField(
                    value = state.muacState.value,
                    onValueChange = { onEvent(MalnutritionScreeningEvent.MuacChanged(it)) },
                    labelText = "MUAC (cm)",
                    isDouble = true,
                    trailingIcon = {
                        MuacColorIndicator(muacCategory = state.muacState.category)
                    }
                )
            }
        }
    }
}

@Composable
fun MuacColorIndicator(muacCategory: MuacCategory?){
    var color: Color = when (muacCategory) {
        MuacCategory.NORMAL -> {
            Color.Green
        }
        MuacCategory.MODERATE -> {
            Color.Yellow
        }
        MuacCategory.SEVERE -> {
            Color.Red
        }
        null -> {
            Color.Gray
        }
    }

    Spacer(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color)
    )
}