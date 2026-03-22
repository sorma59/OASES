package com.unimib.oases.ui.screen.medical_visit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.ui.components.util.CenteredTextInBox
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun MedicalVisitScreen(
    appViewModel: AppViewModel,
) {

    val viewModel: MedicalVisitViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    MedicalVisitContent(state, viewModel::onEvent)
}

@Composable
private fun MedicalVisitContent(
    state: MedicalVisitState,
    onEvent: (MedicalVisitEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(Modifier.padding(bottom = 32.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(32.dp))

            MainComplaintsGrid(state, onEvent)
        }

        FloatingActionButton(
            modifier = Modifier
                .size(256.dp, 64.dp)
                .align(Alignment.BottomCenter),
            onClick = { },
            shape = ButtonDefaults.shape
        ) { TitleText("Disposition") }
    }
}

@Composable
private fun MainComplaintsGrid(
    state: MedicalVisitState,
    onEvent: (MedicalVisitEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleText("Choose a main complaint", fontSize = 28)

        Spacer(Modifier.height(32.dp))

        TitleText("Evaluation", fontSize = 24)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.horizontalScroll(scrollState),
        ) {
            ComplaintId.entries.forEach { complaint ->
                EvaluationBoxButton(state, complaint, onEvent)
            }
        }

        Spacer(Modifier.height(32.dp))

        TitleText("Reassessment", fontSize = 24)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.horizontalScroll(scrollState),
        ) {
            ComplaintId.entries.forEach { complaint ->
                ReassessmentBoxButton(state, complaint, onEvent)
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun EvaluationBoxButton(
    state: MedicalVisitState,
    complaintId: ComplaintId,
    onEvent: (MedicalVisitEvent) -> Unit,
){
    BoxButton(
        complaintId,
        isEnabled = { true },
        hasBorder = { state.complaintSummaries[complaintId.id] != null }
    ) {
        onEvent(
            MedicalVisitEvent.EvaluationClicked(complaintId.id)
        )
    }
}

@Composable
private fun ReassessmentBoxButton(
    state: MedicalVisitState,
    complaintId: ComplaintId,
    onEvent: (MedicalVisitEvent) -> Unit,
){
    BoxButton(
        complaintId,
        isEnabled = {
            state.complaintSummaries[complaintId.id] != null
        },
        hasBorder = {
            false //TODO
        }
    ) {
        onEvent(
            MedicalVisitEvent.ReassessmentClicked(complaintId.id)
        )
    }
}

@Composable
private fun BoxButton(
    complaintId: ComplaintId,
    isEnabled: () -> Boolean,
    hasBorder: () -> Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(shape)
            .background(
                if (isEnabled())
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.primaryContainer.copy(0.38f)
            )
            .run {
                if (hasBorder()) {
                    this.border(2.dp, MaterialTheme.colorScheme.tertiaryContainer, shape)
                } else this
            }
            .run {
                if (isEnabled()) {
                    this.clickable { onClick() }
                } else this
            }
            .padding(4.dp)
    ) {
        CenteredTextInBox(
            complaintId.label,
            20.sp,
            if (isEnabled())
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onPrimaryContainer.copy(0.3f)
        )
    }
}
