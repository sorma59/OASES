package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.seanproctor.datatable.DataColumn
import com.seanproctor.datatable.material3.DataTable
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.theme.backgroundDark
import com.unimib.oases.util.DateAndTimeUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId



@Composable
fun VitalSignsSummary(
    appViewModel: AppViewModel
){

    val viewModel: VitalSignsViewModel = hiltViewModel()


    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    VitalSignsTable (state, viewModel::onEvent)
}


@Composable
fun VitalSignsTable(
    state: VitalSignsState,
    onEvent: (VitalSignsEvent) -> Unit
) {


    Box {
        val groupedTimelines = remember(state.visitVitalSigns) {
            state.visitVitalSigns
                .sortedByDescending { it.timestamp }
                .groupBy { it.timestamp }

        }

//        val vitalSignNames = remember(state.vitalSigns) {
//            state.vitalSigns
//                .map { it }
//                .distinct()
//        }




        val vitalLookup = remember(state.visitVitalSigns) {
            state.visitVitalSigns.associateBy {
                it.name to it.timestamp
            }
        }


        Column (horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth().padding(20.dp)) {

            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
                Text("Add a Vital Sign ", modifier = Modifier.padding(end = 5.dp))
                Button(onClick = { onEvent(VitalSignsEvent.AddButtonClicked) })
                {
                    Text("Add new")
                }
            }

            if(state.visitVitalSigns.isEmpty())
                Text("No vital signs yet")

            else  Row {



                DataTable(
                    modifier  = Modifier.border(1.dp, MaterialTheme.colorScheme.surfaceContainerLowest).width(100.dp),
                    headerBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    columns = listOf(
                        DataColumn(Alignment.Center) {
                            Text("Vital", color = MaterialTheme.colorScheme.surface)
                        }
                    )
                ) {
                    state.vitalSigns.forEach { vital ->
                        row {
                            onClick = {

                                // Handle row click
                            }
                            // Row label cell
                            cell {
                                Text(
                                    vital.acronym + "\n(${vital.unit})",
                                    textAlign = TextAlign.Center,
                                    fontSize = 13.sp,
                                    lineHeight = 15.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                DataTable(
                    headerBackgroundColor = MaterialTheme.colorScheme.primaryContainer,

                    columns =
                        groupedTimelines.map { time ->
                            DataColumn(Alignment.Center) {
                                Text(
                                    Instant.ofEpochMilli(time.key.toLong())
                                        .atZone(ZoneId.systemDefault())
                                        .format(DateAndTimeUtils.hoursAndMinutesFormatter),
                                    color = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                ) {

                    state.vitalSigns.forEach { vital ->
                        row {

                            // ONE CELL PER TIMESTAMP
                            groupedTimelines.forEach { (time, _) ->

                                val value = vitalLookup[vital.name to time]?.value ?: "-"
                                val color = vitalLookup[vital.name to time]?.color


                                cell {
                                    Text(
                                        value,
                                        modifier = Modifier.background(color ?: Color.Transparent),
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                    }


                }
            }


        }


    }


}
