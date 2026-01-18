package com.unimib.oases.ui.components.util.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.unimib.oases.ui.components.scaffold.UiEvent
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.screen.root.AppViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun HandleNavigationEvents(
    navigationEvents: Flow<NavigationEvent>,
    appViewModel: AppViewModel
){
    LaunchedEffect(Unit) {
        navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }
}

@Composable
fun HandleUiEvents(
    uiEvents: Flow<UiEvent>,
    appViewModel: AppViewModel
){
    LaunchedEffect(Unit) {
        uiEvents.collect {
            when (it) {
                is UiEvent.ShowSnackbar -> appViewModel.showSnackbar(it.snackbarData)
                is UiEvent.ShowToast -> appViewModel.showToast(it.message)
            }
        }
    }
}