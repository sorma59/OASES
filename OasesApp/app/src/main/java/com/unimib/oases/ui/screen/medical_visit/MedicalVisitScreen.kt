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
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.CenteredTextInBox
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Screen.MainComplaintScreen
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.util.datastructure.binarytree.TreeId


@Composable
fun MedicalVisitScreen(
    appViewModel: AppViewModel,
    viewModel: MedicalVisitViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Spacer(Modifier.height(64.dp))

        TitleText("Beginning of the medical visit")

        MainComplaintsGrid(appViewModel, state)
    }
}

@Composable
fun MainComplaintsGrid(appViewModel: AppViewModel, state: MedicalVisitState) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){

        CenteredText("Choose a main complaint")

        FlowRow(
            Modifier.padding(8.dp)
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
                                MainComplaintScreen.route +
                                "/patientId=${state.receivedId}" +
                                "/treeId=${TreeId.DIARRHEA.id}"
                            )
                        )
                    }
            ) {
                CenteredTextInBox("Diarrhea", 22.sp, MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}