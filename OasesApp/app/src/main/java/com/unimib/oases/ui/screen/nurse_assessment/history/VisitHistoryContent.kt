package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.unimib.oases.ui.components.card.VisitCard
import com.unimib.oases.ui.components.util.CenteredTextInBox
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator

@Composable
fun VisitHistoryContent(
    state: PastVisitsState,
    onEvent: (HistoryEvent) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        if (state.error != null)
            RetryButton(
                error = state.error,
                onClick = { onEvent(HistoryEvent.ReloadPastVisits) }
            )
        else if (state.isLoading)
            CustomCircularProgressIndicator()
        else
            VisitHistoryList(visits = state.visits)
    }

}

@Composable
fun VisitHistoryList(visits: List<VisitState>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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