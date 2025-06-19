package com.unimib.oases.ui.screen.patient_registration.visit_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.components.card.VisitCard
import com.unimib.oases.ui.components.util.CenteredTextInBox

@Composable
fun VisitHistoryScreen(
    patientId: String
) {

    val visitHistoryViewModel: VisitHistoryViewModel = hiltViewModel()

    LaunchedEffect(patientId) {
        visitHistoryViewModel.loadVisits(patientId)
    }

    val state by visitHistoryViewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        VisitHistoryList(visits = state.visits)
    }

}

@Composable
fun VisitHistoryList(visits: List<Visit>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (visits.isNotEmpty()) {
            items(visits) { visit ->
                VisitCard(visit = visit)
            }
        }
        else
            item {
                CenteredTextInBox("No visits found")
            }
    }
}