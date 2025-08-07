package com.unimib.oases.ui.screen.nurse_assessment.visit_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.components.card.VisitCard
import com.unimib.oases.ui.components.util.CenteredTextInBox
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator

@Composable
fun VisitHistoryScreen(
    state: VisitHistoryState,
    onEvent: (VisitHistoryEvent) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        if (state.error != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = state.error)

                Button(
                    onClick = {
                        onEvent(VisitHistoryEvent.Retry)
                    }
                ) {
                    Text("Retry")
                }
            }
        } else if (state.isLoading)
            CustomCircularProgressIndicator()
        else
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