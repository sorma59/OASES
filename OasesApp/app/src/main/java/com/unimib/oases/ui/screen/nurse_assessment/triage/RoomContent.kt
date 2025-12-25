package com.unimib.oases.ui.screen.nurse_assessment.triage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.TriageCode

@Composable
fun RoomContent(
    state: EditingState,
    onEvent: (TriageEvent) -> Unit,
) {
    // Initial Retry moved to TriageViewModel's init

    Box{
        Column(modifier = Modifier.padding(30.dp)){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Patient triage code: ")

                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = "Triage code",
                    modifier = Modifier.size(20.dp),
                    tint = state.triageData.triageCode.getColor()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 2
            ) {
                state.roomsState.rooms.forEach { room ->
                    RoomCard(
                        room = room,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(150.dp)
                            .clickable {
                                onEvent(TriageEvent.RoomClicked(room))
                            },
                        isSelected = state.triageData.selectedRoom == room
                    )
                }
            }
        }
    }
}

@Composable
fun RoomCard(
    room: Room,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.2f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Selection indicator
            Icon(
                imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = if (isSelected) "Selected" else "Not selected",
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Gray,
                modifier = Modifier
                    .size(20.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Room name
            Text(
                text = room.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Gray,
            )
        }
    }
}


@Preview(apiLevel = 34)
@Composable
fun RoomScreenPreview() {
    RoomContent(
        EditingState(
            triageConfig = TriageConfig(
                redOptions = emptyList(),
                yellowOptions = emptyList()
            ),
            triageData = TriageData(
                selectedReds = setOf("aaaaaaaaaaaaaaa"),
                selectedYellows = setOf("aaaa", "vvvvv", "aaaa", "dadad"),
                triageCode = TriageCode.GREEN,
                selectedRoom = null
            ),
            roomsState = RoomsState(),
            tabStack = emptyList()
        ),
        onEvent = {},
    )
}




