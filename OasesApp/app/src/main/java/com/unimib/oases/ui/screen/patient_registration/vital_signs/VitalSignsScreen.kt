package com.unimib.oases.ui.screen.patient_registration.vital_signs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.BottomButtons
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator

@Composable
fun VitalSignsScreen(
    onSubmitted: (VitalSignsState) -> Unit,
    onBack: () -> Unit
) {

    val vitalSignsViewModel: VitalSignsViewModel = hiltViewModel()

    val state by vitalSignsViewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Box(modifier = Modifier.weight(1f)){
            if (state.error != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(text = state.error!!)

                    Button(
                        onClick = {
                            vitalSignsViewModel.onEvent(VitalSignsEvent.Retry)
                        }
                    ) {
                        Text("Retry")
                    }
                }
            } else if (state.isLoading) {
                CustomCircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {

                    for (vitalSign in state.vitalSigns) {
                        AnimatedLabelOutlinedTextField(
                            value = vitalSign.value.toString(),
                            onValueChange = {
                                vitalSignsViewModel.onEvent(
                                    VitalSignsEvent.ValueChanged(
                                        vitalSign.name,
                                        it
                                    )
                                )
                            },
                            labelText = vitalSign.name + " (" + vitalSign.acronym + ", " + vitalSign.unit + ")",
                            isError = vitalSign.error != null,
                            isInteger = vitalSign.name != "Temperature" && vitalSign.name != "Rapid Blood Sugar", // Hardcoded, to fix later
                            isDouble = vitalSign.name == "Temperature" || vitalSign.name == "Rapid Blood Sugar", // Hardcoded, to fix later
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            FadeOverlay(Modifier.align(Alignment.BottomCenter))
        }

        BottomButtons(
            onCancel = { onBack() },
            onConfirm = { onSubmitted(state) },
            cancelButtonText = "Back",
            confirmButtonText = "Next",
        )
    }
}