package com.unimib.oases.ui.components.tab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

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
    MultiChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        tabs.forEach { tab ->
            SegmentedButton(
                // Determine if the current tab in the loop is the selected one.
                checked = (tab == selectedTab),
                // The onCheckedChange lambda provides a boolean, but we want to know *which*
                // tab was clicked, so we call onTabSelected with the 'tab' from the loop.
                onCheckedChange = { onTabSelected(tab) },
                // Use custom colors to make the selected tab more prominent.
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                icon = { },
                // Set the shape for each button in the row.
                shape = SegmentedButtonDefaults.baseShape
            ) {
                // Use the provided lambda to get the title for the current tab.
                Text(text = getTabTitle(tab), fontSize = 20.sp)
            }
        }
    }
}

// =====================================================================================
// Example Usage and Preview
// =====================================================================================

// Let's define a sample enum to demonstrate how to use the TabSwitcher.
private enum class SampleTab(val title: String) {
    DETAILS("Details"),
    HISTORY("History"),
    SETTINGS("Settings")
}

@Preview(showBackground = true)
@Composable
private fun TabSwitcherPreview() {
    // In a real app, you would use `remember { mutableStateOf(...) }`
    // to manage the selected tab state.
    val tabs = SampleTab.entries
    val selectedTab = SampleTab.HISTORY

    TabSwitcher(
        tabs = tabs,
        selectedTab = selectedTab,
        onTabSelected = { /* In a real app, you'd update your state here */ },
        getTabTitle = { tab -> tab.title }
    )
}