package com.unimib.oases.ui.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

/**
 * Oases themed [Card] composable.
 *
 * This is a wrapper around the Material 3 [Card] that simplifies its usage by introducing a [CardType]
 * parameter. This allows for easily switching between `elevated`, `filled`, and `outlined` card
 * styles while automatically applying the appropriate default shape and elevation.
 *
 * This composable can be used to display content and actions on a single topic.
 *
 * @param modifier The [Modifier] to be applied to this card.
 * @param type The style of the card. See [CardType] for available options (`ELEVATED`, `FILLED`, `OUTLINED`).
 * @param shape Defines the card's shape. Defaults to the shape corresponding to the [type].
 * @param elevation [CardElevation] used to resolve the elevation for this card in different states.
 * Defaults to the elevation corresponding to the [type].
 * @param colors [CardColors] that will be used to resolve the color of the card in different states.
 * See [CardDefaults.cardColors].
 * @param border The border to draw around the container of this card.
 * @param content The content of the card.
 */
@Composable
fun OasesCard(
    modifier: Modifier = Modifier,
    type: CardType = CardType.ELEVATED,
    shape: Shape = type.getShape(),
    elevation: CardElevation = type.getElevation(),
    colors: CardColors = CardDefaults.cardColors(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        colors = colors,
        border = border,
        content = content
    )
}

enum class CardType {
    FILLED,
    ELEVATED,
    OUTLINED;

    @Composable
    fun getShape(): Shape {
        return when (this) {
            FILLED -> CardDefaults.shape
            ELEVATED -> CardDefaults.elevatedShape
            OUTLINED -> CardDefaults.outlinedShape
        }
    }

    @Composable
    fun getElevation(): CardElevation {
        return when (this) {
            FILLED -> CardDefaults.cardElevation()
            ELEVATED -> CardDefaults.elevatedCardElevation()
            OUTLINED -> CardDefaults.outlinedCardElevation()
        }
    }
}