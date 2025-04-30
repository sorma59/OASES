package com.unimib.oases.ui.home_page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.OasesTheme
import com.unimib.oases.ui.Greeting

@Composable
fun HomeScreen(){
    Box(modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier.fillMaxSize()){
            Surface (
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column (){
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){
                        Text("OASES")
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.MoreVert,
                                contentDescription = "More")
                        }
                    }

                    Row (modifier = Modifier.fillMaxWidth()){
                        SearchBar(
                            query = "",
                            onQueryChange = { },
                            onSearch = { },
                            active = false,
                            onActiveChange = { },
                            searchHistory = emptyList()
                        ) { }
                    }
                }
            }

        }

        FloatingActionButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ){
            Icon(Icons.Default.Add, "Add")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchHistory: List<String>,
    onHistoryItemClick: (String) -> Unit
) {
    DockedSearchBar(
        shadowElevation = 5.dp,
        colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainer),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            onSearch(it)
            onActiveChange(false)
        },
        active = active,
        shape = RoundedCornerShape(10.dp),
        onActiveChange = onActiveChange,
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 12.dp)
            .fillMaxWidth(),
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
        }
    ) {
        searchHistory.takeLast(3).forEach { item ->
            ListItem(
                modifier = Modifier.clickable { onHistoryItemClick(item) },
                headlineContent = { Text(text = item) },
                leadingContent = {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OasesTheme {
        HomeScreen()
    }
}


