package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.card.VisitCard
import com.unimib.oases.ui.components.timeline.TimelineNode
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
            VisitHistoryList(state.visits, Modifier.padding(16.dp))
    }

}

@Composable
fun VisitHistoryList(
    visits: List<VisitState>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (visits.isNotEmpty()) {
            if (visits.size == 1) {
                item { VisitCard(visits[0]) }
            }
            else
                itemsIndexed(visits) { index, visit ->
                    TimelineNode(
                        isFirstNode = index == 0,
                        isLastNode = index == visits.size - 1
                    ) { VisitCard(visit) }
                }
        }
        else
            item {
                CenteredTextInBox("No visits found")
            }
    }
}