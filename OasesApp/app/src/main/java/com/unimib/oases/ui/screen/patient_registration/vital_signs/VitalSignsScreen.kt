package com.unimib.oases.ui.screen.patient_registration.vital_signs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
                .weight(1f)
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
                    isInteger = vitalSign.name != "Temperature" && vitalSign.name != "Rapid Blood Sugar", // TODO: de-hardcode this
                    isDouble = vitalSign.name == "Temperature" || vitalSign.name == "Rapid Blood Sugar", // TODO: de-hardcode this
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedButton(onClick = { onBack() }) {
                Text("Back")
            }

            Button(
                onClick = {
                    onSubmitted(state)
                }
            ) {
                Text("Next")
            }
        }
    }
}

fun isInteger(string: String):Boolean{
    return string.all{ch -> ch.isDigit()} || string.isEmpty()
}

fun isDecimal(string: String):Boolean{
    return string.matches(Regex("^\\d*\\.?\\d*$")  ) || string.isEmpty()
}

//Regex("^\\d*\\.?\\d* $ " )
//Regex("^\\d+\\.?\\d+$|^\\d+$")
