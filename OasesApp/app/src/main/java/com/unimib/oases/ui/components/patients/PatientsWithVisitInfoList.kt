package com.unimib.oases.ui.components.patients

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.seanproctor.datatable.DataColumn
import com.seanproctor.datatable.DataTableScope
import com.seanproctor.datatable.material3.DataTable
import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.SmallGrayText
import com.unimib.oases.util.DateAndTimeUtils
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

@Composable
fun PatientsWithVisitInfoList(
    modifier: Modifier = Modifier,
    patientsWithVisitInfo: List<PatientWithVisitInfo> = emptyList(),
    onItemClick: (PatientAndVisitIds) -> Unit = {},
    title: String = "Patient List",
    noPatientsMessage: String = "No patients found."
) {

    val patientList = remember { mutableStateListOf<PatientWithVisitInfo>() }
    val configuration = LocalConfiguration.current
    var sortColumnIndex by remember { mutableStateOf<Int?>(null) }
    var sortAscending by remember { mutableStateOf(true) }


    var roomFilter by remember { mutableStateOf<String?>(null) }



    val availableRooms = patientsWithVisitInfo.mapNotNull { it.visit.roomName }.distinct()

    val filteredData = patientList
        .filter { patient ->
            roomFilter == null || patient.visit.roomName == roomFilter
        }


    val sortedData = when (sortColumnIndex) {
        null -> filteredData.sortedBy { it.visit.arrivalTime}
        4 -> if (sortAscending) filteredData.sortedBy { it.visit.arrivalTime} else filteredData.sortedByDescending { it.visit.arrivalTime }
        else -> filteredData.sortedBy { it.visit.arrivalTime}
    }



    val itemSize = when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> 35.dp
        Configuration.ORIENTATION_LANDSCAPE -> 15.dp
        else -> 35.dp
    }



    val colorEven = MaterialTheme.colorScheme.surfaceBright
    val colorOdd = MaterialTheme.colorScheme.surfaceDim.copy(0.5f)

    LaunchedEffect(patientsWithVisitInfo) {
        patientList.clear()
        patientList.addAll(patientsWithVisitInfo)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp),
    ) {


        SmallGrayText(
            text = title,
            modifier = modifier.align(Alignment.Start).padding(bottom = 5.dp)
        )


        Row(horizontalArrangement = Arrangement.End, modifier =  Modifier.fillMaxWidth()){
            roomFilter?.let {

                Card(
                    modifier = Modifier.padding(5.dp),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onBackground),
                    shape = RoundedCornerShape(0),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(5.dp)) {
                        Text(text = roomFilter.toString(), fontSize = 12.sp)
                        Icon(contentDescription = "Clear Icon", imageVector = Icons.Default.Clear, tint = MaterialTheme.colorScheme.onBackground.copy(0.7f), modifier = Modifier.padding(start = 3.dp).size(15.dp).clickable(onClick = {roomFilter = null}))

                    }

                }


            }
        }



        if (patientList.isNotEmpty()) {
            var selectedRow by remember { mutableStateOf<Int?>(null) }
            DataTable(
                headerBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth(),
                columns = listOf(
                    DataColumn(Alignment.Center) {

                        Text(color = MaterialTheme.colorScheme.surface, text = "Name")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Age")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Gender")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Triage")
                    },

                    DataColumn(Alignment.Center,  onSort = { index, ascending ->
                        sortColumnIndex = index
                        sortAscending = !sortAscending
                    }) {
                        Row (verticalAlignment = Alignment.CenterVertically) {
                            Text(color = MaterialTheme.colorScheme.surface, text = "Arrival Time", textAlign = TextAlign.Center)
                            Icon(contentDescription = "Sort Icon", imageVector = if (sortAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, tint = MaterialTheme.colorScheme.surface, modifier = Modifier.size(itemSize))
                        }

                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Status")
                    },
                    DataColumn(Alignment.Center) {
                        Row {
                            Text(color = MaterialTheme.colorScheme.surface, text = "Room")
                            FilterableHeader(
                                options = availableRooms,
                                selected = roomFilter,
                                labelMapper = { it },
                                onSelected = { roomFilter = it }
                            )
                        }

                    },
                )
            ) {

                // Iterate over the list of patients
                sortedData.forEachIndexed {index, patient ->
                    row {
                        backgroundColor = if (index % 2 == 0) colorEven else colorOdd
                        // Handle row click
                        onClick = {
                            onItemClick(PatientAndVisitIds(patient.patient.id, patient.visit.id))
                        }

                        // Column 1 Data
                        cell {
                            Text(patient.patient.name, textAlign = TextAlign.Center)
                        }

                        // Column 2 Data
                        cell {
                            // TODO: Replace with actual property
                            Text(patient.patient.birthDate)
                        }

                        // Column 3 Data
                        cell {
                            // TODO: Replace with actual property
                            Text(patient.patient.sex.displayName)
                        }

                        cell {
                            // TODO: Replace with actual property
                            Icon(
                                imageVector = Icons.Default.Circle,
                                contentDescription = "Triage color",
                                modifier = Modifier.size(20.dp),
                                tint = patient.visit.triageCode.getColor()
                            )
                        }

                        cell {
                            Text(patient.visit.arrivalTime.format(DateAndTimeUtils.hoursAndMinutesFormatter))
                        }

                        cell {
                            // TODO: Replace with actual property
                            Text(patient.visit.patientStatus.displayValue, textAlign = TextAlign.Center)
                        }

                        cell {
                            // TODO: Replace with actual property
                            Text(patient.visit.roomName ?: "-", textAlign = TextAlign.Center)
                        }
                    }


                }
            }




//            LazyVerticalGrid (
//                modifier = modifier.fillMaxWidth(),
//                contentPadding = PaddingValues( vertical = 10.dp),
//                horizontalArrangement = Arrangement.spacedBy(20.dp),
//                verticalArrangement = Arrangement.spacedBy(20.dp),
//                columns = GridCells.Fixed(2),
//                content = {
//
//                    items(patientList) { patient ->
//                        PatientCard(
//                            patientWithVisitInfo = patient,
//                            isRevealed = false,
//                            onExpanded = {},
//                            actions = {},
//                            onCardClick = { ids ->
//                                onItemClick(ids)
//                            },
//                        )
//                    }
//                }
//            )
        } else
            CenteredText(noPatientsMessage, Modifier.padding(top = 16.dp))
    }
}
