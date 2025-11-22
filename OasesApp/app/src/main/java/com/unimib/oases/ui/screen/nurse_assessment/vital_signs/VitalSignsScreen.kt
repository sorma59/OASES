package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun VitalSignsScreen(
    appViewModel: AppViewModel
){

    val viewModel: VitalSignsViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    VitalSignsContent(state, viewModel::onEvent, viewModel::getPrecisionFor)
}

@Composable
fun VitalSignsContent(
    state: VitalSignsState,
    onEvent: (VitalSignsEvent) -> Unit,
    getPrecisionFor: (String) -> NumericPrecision?
) {

    val scrollState = rememberScrollState()

    Box{
        state.error?.let {
            RetryButton(
                it,
                onClick = {
                    onEvent(VitalSignsEvent.Retry)
                }
            )
        } ?: if (state.isLoading) {
            CustomCircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {

                for (vitalSign in state.vitalSigns) {
                    AnimatedLabelOutlinedTextField(
                        value = vitalSign.value,
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