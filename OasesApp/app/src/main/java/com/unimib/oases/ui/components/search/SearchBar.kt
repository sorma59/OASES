package com.unimib.oases.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unimib.oases.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchHistory: List<String>,
    onHistoryItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    DockedSearchBar(
        modifier = modifier,
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
                    Box { // Box used to mimic an icon button without making it clickable
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            modifier = Modifier.padding(8.dp) // Padding of an IconButton
                        )
                    }
                },
                trailingIcon = {
                    if (active) {
                        IconButton(
                            onClick = {
                                if (query.isNotEmpty()) {
                                    onQueryChange("")
                                } else {
                                    onActiveChange(false)
                                }
                            }
                        ){
                            Icon(
                                modifier = Modifier.padding(),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close search",
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        expanded = searchHistory.isNotEmpty() && active,
        onExpandedChange = onActiveChange,
        shape = RoundedCornerShape(10.dp),
        colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainerHighest),
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = 5.dp
    ) {
        searchHistory.asReversed().takeLast(3).forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SearchBarDefaults.InputFieldHeight) // Same height as the input field
                    .clickable { onHistoryItemClick(item) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box { // Box used to mimic an icon button without making it clickable
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                modifier = Modifier.padding(8.dp) // Padding of an IconButton
                            )
                        }

                        Text(text = item)
                    }

                    IconButton(
                        onClick = { onQueryChange(item) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_insert_24px),
                            contentDescription = "Replace search query with history item",
                        )
                    }
                }
            }
        }
    }
}
