package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.util.reactToKeyboardAppearance

@Composable
fun VitalSignsScreen(
    state: VitalSignsState,
    onEvent: (VitalSignsEvent) -> Unit,
    getPrecisionFor: (String) -> NumericPrecision?
) {

    val scrollState = rememberScrollState()

    Box{
        if (state.error != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = state.error)

                Button(
                    onClick = {
                        onEvent(VitalSignsEvent.Retry)
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
                    .verticalScroll(scrollState)
                    .reactToKeyboardAppearance()
                    .padding(16.dp)
            ) {

                for (vitalSign in state.vitalSigns) {
                    AnimatedLabelOutlinedTextField(
                        value = vitalSign.value.toString(),
                        onValueChange = {
                            onEvent(
                                VitalSignsEvent.ValueChanged(
                                    vitalSign.name,
                                    it
                                )
                            )
                        },
                        labelText = vitalSign.name + " (" + vitalSign.acronym + ", " + vitalSign.unit + ")",
                        isError = vitalSign.error != null,
                        isInteger = getPrecisionFor(vitalSign.name) == NumericPrecision.INTEGER,
                        isDouble = getPrecisionFor(vitalSign.name) == NumericPrecision.FLOAT
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        FadeOverlay(Modifier.align(Alignment.BottomCenter))
    }
}