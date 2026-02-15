package com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun VitalSignsForm(
    appViewModel: AppViewModel
){

    val viewModel: VitalSignsFormViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    InputFields (
        state,
        viewModel::onEvent,
        viewModel::getPrecisionFor,
        {
            VitalSignsBottomButtons(
                onCancel = { viewModel.onEvent(VitalSignsFormEvent.Cancel) },
                onSave = { viewModel.onEvent(VitalSignsFormEvent.Save) }
            )
        }
    )
}

@Composable
fun InputFields(
    state: VitalSignsFormState,
    onEvent: (VitalSignsFormEvent) -> Unit,
    getPrecisionFor: (String) -> NumericPrecision?,
    bottomButtons: @Composable (() -> Unit) = {}
) {

    val scrollState = rememberScrollState()

    Box{

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {

            state.vitalSigns.forEach { vitalSign ->
                AnimatedLabelOutlinedTextField(
                    value = vitalSign.value,
                    onValueChange = {
                        onEvent(
                            VitalSignsFormEvent.ValueChanged(
                                vitalSign.name,
                                it
                            )
                        )
                    },
                    labelText = "${vitalSign.name} (${vitalSign.acronym}, ${vitalSign.unit})",
                    isError = vitalSign.error != null,
                    isInteger = getPrecisionFor(vitalSign.name) == NumericPrecision.INTEGER,
                    isDouble = getPrecisionFor(vitalSign.name) == NumericPrecision.FLOAT
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            bottomButtons()
        }

        FadeOverlay(Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun VitalSignsBottomButtons(
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    BottomButtons(
        onCancel = onCancel,
        onConfirm = onSave,
        cancelButtonText = "Cancel",
        confirmButtonText = "Save"
    )
}