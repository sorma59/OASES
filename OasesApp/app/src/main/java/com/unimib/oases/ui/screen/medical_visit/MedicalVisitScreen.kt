package com.unimib.oases.ui.screen.medical_visit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

        MainComplaintsGrid(onEvent)
    }
}

@Composable
private fun MainComplaintsGrid(
    onEvent: (MedicalVisitEvent) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){

        TitleText("Choose a main complaint")

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ComplaintId.entries.forEach {
                MainComplaintBoxButton(it, onEvent)
            }
        }
    }
}

@Composable
private fun MainComplaintBoxButton(
    complaintId: ComplaintId,
    onEvent: (MedicalVisitEvent) -> Unit

){
    BoxButton(complaintId, onEvent)
}

@Composable
private fun BoxButton(
    complaintId: ComplaintId,
    onEvent: (MedicalVisitEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)
            .clickable {
                onEvent(
                    MedicalVisitEvent.ComplaintClicked(complaintId.id)
                )
            }
    ) {
        CenteredTextInBox(complaintId.label, 20.sp, MaterialTheme.colorScheme.onPrimaryContainer)
    }
}