@file:JvmName("ChronicConditionCardKt")

package com.unimib.oases.ui.home_page.components.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChronicConditionCard(condition: ChronicConditionUi) {
    val cardBackgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 300),
        label = "cardBackgroundColor"
    )
    val textColor = MaterialTheme.colorScheme.surface

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Chronic condition: ${condition.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
            Text(
                text = "Diagnosis date: ${condition.diagnosisDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}