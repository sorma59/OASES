package com.unimib.oases.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchHistory: List<String>,
    onHistoryItemClick: (String) -> Unit
) {
    val colors1 = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainer)
    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch()
                    onActiveChange(false)
                },
                expanded = active,
                onExpandedChange = onActiveChange,
                enabled = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (active) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (query.isNotEmpty()) {
                                    onQueryChange("")
                                } else {
                                    onActiveChange(false)
                                }
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close search",
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        expanded = searchHistory.isNotEmpty() && active,
        onExpandedChange = onActiveChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = colors1,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = 5.dp,
        content = {
            searchHistory.takeLast(3).forEach { item ->
                ListItem(
                    modifier = Modifier.clickable { onHistoryItemClick(item) },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    headlineContent = { Text(text = item) },
                    leadingContent = {
                        Icon(
                            modifier = Modifier.padding(end = 10.dp),
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                        )
                    },
                    trailingContent = {
                        IconButton(
                            onClick = { onQueryChange(item) }
                        ){
                            Icon(
                                modifier = Modifier.padding(start = 10.dp),
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = "Insert history into search",
                            )
                        }
                    }
                )
            }
        },
    )
}
