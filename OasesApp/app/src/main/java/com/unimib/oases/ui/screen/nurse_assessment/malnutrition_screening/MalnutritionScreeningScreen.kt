package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.MuacCategory
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.button.StartButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
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

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

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
        MalnutritionSummary(state, onEvent)
    else {
        MalnutritionEditing(state, onEvent)
    }
}

@Composable
fun MuacColorIndicator(muacCategory: MuacCategory?){
    val color: Color = when (muacCategory) {
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
        MalnutritionScreeningCard(it, onEvent, modifier)
    } ?: StartButton(
        "Malnutrition screening is yet to be performed",
        onClick = { onEvent(MalnutritionScreeningEvent.CreateButtonPressed) }
    )        
}

@Composable
private fun MalnutritionScreeningCard(
    data: MalnutritionScreeningData,
    onEvent: (MalnutritionScreeningEvent) -> Unit,
    modifier: Modifier
) {
    val decimalFormat = remember { DecimalFormat("#.#") }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            // --- Title ---
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Malnutrition Screening Summary",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                IconButton(
                    onClick = { onEvent(MalnutritionScreeningEvent.EditButtonPressed) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit malnutrition screening"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            // --- Weight, Height, BMI ---
            ListItem(
                headlineContent = { Text("Weight") },
                leadingContent = {
                    Icon(
                        Icons.Default.MonitorWeight,
                        contentDescription = "Weight"
                    )
                },
                trailingContent = {
                    Text(
                        text = data.weight.takeIf { it.isNotBlank() }?.let { "$it kg" }
                            ?: "N/A",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            ListItem(
                headlineContent = { Text("Height") },
                leadingContent = {
                    Icon(
                        Icons.Default.Height,
                        contentDescription = "Height"
                    )
                },
                trailingContent = {
                    Text(
                        text = data.height.takeIf { it.isNotBlank() }?.let { "$it cm" }
                            ?: "N/A",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            ListItem(
                headlineContent = { Text("BMI (Body Mass Index)") },
                leadingContent = {
                    Icon(
                        Icons.Default.SquareFoot,
                        contentDescription = "BMI"
                    )
                },
                trailingContent = {
                    Text(
                        text = data.bmi?.let { decimalFormat.format(it) } ?: "N/A",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // --- MUAC Section ---
            MuacResult(
                value = data.muacState.value,
                category = data.muacState.category
            )
        }
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
                    state.savingState.error?.let { error ->
                        Text("Error: $error")
                    } ?: if (state.savingState.isLoading)
                        CustomCircularProgressIndicator()
                    else
                        Text(text = "Do you want to save the malnutrition data to the database?")
                },
                confirmButton = {
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text(if (state.savingState.error == null) "Confirm" else "Retry")
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

/**
 * A sub-composable to display the MUAC result with its corresponding color code.
 */
@Composable
private fun MuacResult(
    value: String,
    category: MuacCategory?,
    modifier: Modifier = Modifier
) {
    val muacColor = remember(category) {
        when (category) {
            MuacCategory.SEVERE -> Color.Red
            MuacCategory.MODERATE -> Color(0xFFE6A200) // Amber
            MuacCategory.NORMAL -> Color(0xFF2E7D32) // Dark Green
            null -> Color.Gray
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Default.Straighten, contentDescription = "MUAC")
            Text("MUAC (Mid-Upper Arm Circumference)")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value.takeIf { it.isNotBlank() }?.let { "$it cm" } ?: "N/A",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "MUAC Category: ${category?.name}",
                tint = muacColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MalnutritionSummaryPreview() {
    MalnutritionSummary(
        state = MalnutritionScreeningState(
            patientId = "",
            visitId = "",
            uiMode = PatientRegistrationScreensUiMode.Standalone(),
            storedData = MalnutritionScreeningData(
                weight = "68.5",
                height = "175",
                bmi = 22.3,
                muacState = MuacState(
                    value = "28.5",
                    category = MuacCategory.NORMAL
                )
            )
        ),
        {}
    )
}

@Preview(showBackground = true)
@Composable
fun MalnutritionSummaryIncompletePreview() {
    MalnutritionSummary(
        state = MalnutritionScreeningState(
            patientId = "",
            visitId = "",
            uiMode = PatientRegistrationScreensUiMode.Standalone(),
            storedData = MalnutritionScreeningData(
                weight = "",
                height = "160",
                bmi = null,
                muacState = MuacState(
                    value = "21.2",
                    category = MuacCategory.MODERATE
                )
            )
        ),
        {}
    )
}