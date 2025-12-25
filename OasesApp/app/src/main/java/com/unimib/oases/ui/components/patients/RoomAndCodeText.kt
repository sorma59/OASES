package com.unimib.oases.ui.components.patients

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.Visit

@Composable
fun RoomAndCodeText(visit: Visit) {
    Row {

        Text(
            text = visit.roomName ?: "Room not yet selected",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(end = 5.dp)
        )

        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = "Triage color",
            modifier = Modifier.size(20.dp),
            tint = visit.triageCode.getColor()
        )
    }
}