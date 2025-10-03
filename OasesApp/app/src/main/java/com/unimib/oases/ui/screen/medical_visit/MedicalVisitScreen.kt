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
import androidx.compose.material3.HorizontalDivider
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
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.navigation.Screen.MainComplaintScreen
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun MedicalVisitScreen(
    appViewModel: AppViewModel,
) {

    val viewModel: MedicalVisitViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Spacer(Modifier.height(64.dp))

        HorizontalDivider()

        TitleText("View or edit PMH")

        PastMedicalHistoryBox(state.patientId, appViewModel)

        HorizontalDivider()

        MainComplaintsGrid(state.patientId, appViewModel)
    }
}

@Composable
private fun PastMedicalHistoryBox(
    patientId: String,
    appViewModel: AppViewModel
){
    BoxButton(
        label = "Past Medical History",
        destination = Screen.PastMedicalHistoryScreen.route + "/patientId=$patientId",
        appViewModel
    )
}

@Composable
private fun MainComplaintsGrid(
    patientId: String,
    appViewModel: AppViewModel
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
            for (complaint in ComplaintId.entries)
                MainComplaintBoxButton(complaint, patientId, appViewModel)
        }
    }
}

@Composable
private fun MainComplaintBoxButton(
    complaintId: ComplaintId,
    patientId: String,
    appViewModel: AppViewModel
){
    BoxButton(
        complaintId.label,
        MainComplaintScreen.route + "/patientId=$patientId/complaintId=${complaintId.id}",
        appViewModel
    )
}

@Composable
private fun BoxButton(
    label: String,
    destination: String,
    appViewModel: AppViewModel
) {
    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)
            .clickable {
                appViewModel.onNavEvent(
                    NavigationEvent.Navigate(
                        destination
                    )
                )
            }
    ) {
        CenteredTextInBox(label, 20.sp, MaterialTheme.colorScheme.onPrimaryContainer)
    }
}