package com.unimib.oases.ui.screen.patient_registration.visit_history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.unimib.oases.ui.home_page.components.card.VisitCard
import com.unimib.oases.ui.home_page.components.card.VisitUi

@Composable
fun VisitHistoryScreen() {
    val sampleVisits = remember {
        listOf(
            VisitUi(visitNumber = 1, visitDate = "05/05/2025", statusColor = "Verde"),
            VisitUi(visitNumber = 2, visitDate = "20/04/2025", statusColor = "Giallo"),
            VisitUi(visitNumber = 3, visitDate = "10/04/2025", statusColor = "Rosso"),
            VisitUi(visitNumber = 4, visitDate = "28/03/2025", statusColor = "Verde"),
            VisitUi(visitNumber = 5, visitDate = "15/03/2025", statusColor = "Giallo"),
            VisitUi(visitNumber = 6, visitDate = "01/03/2025", statusColor = "Verde"),
            VisitUi(visitNumber = 7, visitDate = "15/02/2025", statusColor = "Rosso"),
            VisitUi(visitNumber = 8, visitDate = "01/02/2025", statusColor = "Giallo"),
            VisitUi(visitNumber = 9, visitDate = "18/01/2025", statusColor = "Verde"),
            VisitUi(visitNumber = 10, visitDate = "05/01/2025", statusColor = "Rosso")
        )
    }

    Scaffold(
        content = { paddingValues ->
            VisitHistoryList(visits = sampleVisits, paddingValues = paddingValues)
        }
    )
}

@Composable
fun VisitHistoryList(visits: List<VisitUi>, paddingValues: PaddingValues) {
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