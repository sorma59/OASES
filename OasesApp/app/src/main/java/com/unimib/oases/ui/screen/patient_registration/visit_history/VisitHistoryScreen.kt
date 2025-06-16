package com.unimib.oases.ui.screen.patient_registration.visit_history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.components.card.VisitCard

@Composable
fun VisitHistoryScreen(
    patientId: String
) {

    val visitHistoryViewModel: VisitHistoryViewModel = hiltViewModel()

    LaunchedEffect(patientId) {
        visitHistoryViewModel.loadVisits(patientId)
    }

    val state by visitHistoryViewModel.state.collectAsState()

    Scaffold(
        content = { paddingValues ->
            VisitHistoryList(visits = state.visits, paddingValues = paddingValues)
        }
    )
}

@Composable
fun VisitHistoryList(visits: List<Visit>, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(visits) { visit ->
            VisitCard(visit = visit)
        }
    }
}