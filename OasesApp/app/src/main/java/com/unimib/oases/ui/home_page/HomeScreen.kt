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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.core.presentation.components.SearchBar
import com.unimib.oases.ui.theme.OasesTheme

@Composable
fun HomeScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier.fillMaxSize()){
            Surface (
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column {
//                    Row (
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 12.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically){
//                        Text("OASES")
//                        IconButton(onClick = {}) {
//                            Icon(imageVector = Icons.Default.MoreVert,
//                                contentDescription = "More")
//                        }
//                    }

                    Row (modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 12.dp)){
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
            onClick = {navController.navigate("registration_screen")},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)

        ){
            Icon(Icons.Default.Add, "Add")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OasesTheme {
        HomeScreen(navController = rememberNavController())
    }
}


