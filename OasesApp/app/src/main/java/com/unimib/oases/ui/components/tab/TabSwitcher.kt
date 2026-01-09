package com.unimib.oases.ui.components.tab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.ui.components.card.OasesCard
import com.unimib.oases.ui.screen.nurse_assessment.history.HistoryScreenTab

/**
 * A generic, reusable tab switcher built with Material3's SegmentedButton.
 *
 * This composable is ideal for switching between a small, fixed number of views or modes.
 *
 * @param T The type of the data representing each tab (e.g., an enum).
 * @param tabs The list of all available tabs of type T.
 * @param selectedTab The currently selected tab of type T.
 * @param onTabSelected A callback invoked when a new tab is selected by the user.
 * @param getTabTitle A lambda function to resolve the display text for a given tab.
 * @param modifier An optional Modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> TabSwitcher(
    tabs: List<T>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
    getTabTitle: (T) -> String,
    modifier: Modifier = Modifier
) {
    // The MultiChoiceSegmentedButtonRow is perfect for creating a group of connected buttons.
    // We use it in a "single choice" mode by only ever having one item in the `checked` list.
    OasesCard(
        modifier = modifier,
        shape = SegmentedButtonDefaults.baseShape,
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        MultiChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 4.dp)
        ) {
            tabs.forEach { tab ->
                val checked = (tab == selectedTab)
                val fontSize = if (checked) 22.sp else 20.sp
                SegmentedButton(
                    // Determine if the current tab in the loop is the selected one.
                    checked = checked,
                    // The onCheckedChange lambda provides a boolean, but we want to know *which*
                    // tab was clicked, so we call onTabSelected with the 'tab' from the loop.
                    onCheckedChange = { onTabSelected(tab) },
                    icon = { },
                    colors = SegmentedButtonDefaults.colors(
                        inactiveContainerColor = Color.Transparent,
                        activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    border = BorderStroke(0.dp, Color.Transparent),
                    // Set the shape for each button in the row.
                    shape = SegmentedButtonDefaults.baseShape
                ) {
                    // Use the provided lambda to get the title for the current tab.
                    Text(text = getTabTitle(tab), fontSize = fontSize)
                }
            }
        }
    }
}

// =====================================================================================
// Example Usage and Preview
// =====================================================================================

@Preview(showBackground = true)
@Composable
private fun TabSwitcherPreview() {
    val tabs = HistoryScreenTab.entries
    var selectedTab by remember { mutableStateOf(HistoryScreenTab.PAST_MEDICAL_HISTORY) }

    TabSwitcher(
        tabs = tabs,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        getTabTitle = { tab -> tab.title }
    )
}