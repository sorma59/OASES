package com.unimib.oases.ui.screen.medical_visit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(64.dp))

        MainComplaintsGrid(state, onEvent)
    }
}

@Composable
private fun MainComplaintsGrid(
    state: MedicalVisitState,
    onEvent: (MedicalVisitEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleText("Choose a main complaint")

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            modifier = Modifier.height(340.dp), // Height for 2 rows of 160dp + spacing
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Interleaving Evaluation and Reassessment items for each complaint.
            // In a LazyHorizontalGrid with Fixed(2) rows, items are placed vertically first.
            ComplaintId.entries.forEach { complaint ->
                item {
                    EvaluationBoxButton(complaint, onEvent)
                }
                item {
                    ReassessmentBoxButton(state, complaint, onEvent)
                }
            }
        }
    }
}

@Composable
private fun EvaluationBoxButton(
    complaintId: ComplaintId,
    onEvent: (MedicalVisitEvent) -> Unit,
){
    BoxButton(complaintId, { true }) {
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
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isEnabled())
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.primaryContainer.copy(0.38f)
            )
            .padding(4.dp)
            .clickable { if (isEnabled()) onClick() }
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
