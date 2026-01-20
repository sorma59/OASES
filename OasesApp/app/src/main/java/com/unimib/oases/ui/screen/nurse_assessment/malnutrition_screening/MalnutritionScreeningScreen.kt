package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.MuacCategory
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.button.StartButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.util.reactToKeyboardAppearance
import java.util.Locale

@Composable
fun MalnutritionScreeningScreen(
    appViewModel: AppViewModel
){
    val viewModel: MalnutritionScreeningViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    MalnutritionScreeningContent(state, viewModel::onEvent)
}

@Composable
fun MalnutritionScreeningContent(
    state: MalnutritionScreeningState,
    onEvent: (MalnutritionScreeningEvent) -> Unit
){
    state.error?.let {
        RetryButton(
            error = state.error,
            onClick = { onEvent(MalnutritionScreeningEvent.Retry) }
        )
    } ?: if (state.uiMode is PatientRegistrationScreensUiMode.Standalone && !state.uiMode.isEditing)
        MalnutritionSummary(state, onEvent, Modifier.padding(16.dp))
    else {
        MalnutritionEditing(state, onEvent)
    }
}

@Composable
fun MuacColorIndicator(muacCategory: MuacCategory?){
    val color: Color = when (muacCategory) {
        MuacCategory.SEVERE -> Color.Red
        MuacCategory.MODERATE -> Color(0xFFE6A200) // Amber
        MuacCategory.NORMAL -> Color(0xFF2E7D32) // Dark Green
        null -> Color.Gray
    }

    Spacer(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color)
    )
}

/**
 * A composable to display the results of a malnutrition screening in a view-only summary card.
 */
@Composable
fun MalnutritionSummary(
    state: MalnutritionScreeningState,
    onEvent: (MalnutritionScreeningEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    
    state.storedData?.let {
        MalnutritionScreeningSummary(it, onEvent, modifier)
    } ?: StartButton(
        "Malnutrition screening is yet to be performed"
    ) {
        onEvent(MalnutritionScreeningEvent.CreateButtonPressed)
    }

}



@Composable
fun MalnutritionEditing(
    state: MalnutritionScreeningState,
    onEvent: (MalnutritionScreeningEvent) -> Unit
) {

    val onDismiss = remember {
        { onEvent(MalnutritionScreeningEvent.DismissDialog) }
    }

    val onConfirm = remember {
        { onEvent(MalnutritionScreeningEvent.ConfirmDialog) }
    }

    state.editingData?.let {
        Column(
            Modifier.reactToKeyboardAppearance()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                AnimatedLabelOutlinedTextField(
                    value = it.weight,
                    onValueChange = {value -> onEvent(MalnutritionScreeningEvent.WeightChanged(value)) },
                    labelText = "Weight (kg)",
                    isDouble = true,
                    isError = state.formErrors.weightError != null
                )

                state.formErrors.weightError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                AnimatedLabelOutlinedTextField(
                    value = it.height,
                    onValueChange = {value ->  onEvent(MalnutritionScreeningEvent.HeightChanged(value)) },
                    labelText = "Height (cm)",
                    isDouble = true,
                    isError = state.formErrors.heightError != null
                )

                state.formErrors.heightError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Text(
                    "BMI: " + (it.bmi?.let {bmi -> String.format(Locale.getDefault(), "%.1f", bmi) }
                        ?: "")
                )

                AnimatedLabelOutlinedTextField(
                    value = it.muacState.value,
                    onValueChange = { value -> onEvent(MalnutritionScreeningEvent.MuacChanged(value)) },
                    labelText = "MUAC (cm)",
                    isDouble = true,
                    isError = state.formErrors.muacValueError != null,
                    trailingIcon = {
                        MuacColorIndicator(muacCategory = it.muacState.category)
                    }
                )

                state.formErrors.muacValueError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            BottomButtons(
                onCancel = { onEvent(MalnutritionScreeningEvent.BackButtonPressed) },
                onConfirm = { onEvent(MalnutritionScreeningEvent.NextButtonPressed) },
            )
        }

        if (state.showAlertDialog) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = "Confirm malnutrition data saving")
                },
                text = {
                    Text(text = "Do you want to save the malnutrition data to the database?")
                },
                confirmButton = {
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text("Confirm" )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    } ?: RetryButton(
        error = "Failed to enter editing mode",
        onClick = { onEvent(MalnutritionScreeningEvent.EditButtonPressed) }
    )
}