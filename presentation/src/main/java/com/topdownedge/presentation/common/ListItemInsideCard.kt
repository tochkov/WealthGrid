package com.topdownedge.presentation.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ListItemInsideCard(
    modifier: Modifier = Modifier,
    outerCardVerticalPadding: Dp = 0.dp,
    outerCardCornerRounding: Dp = 0.dp,
    outerCardCornerElevation: Dp = 0.dp,
    index: Int = 0,
    totalSize: Int = 1,
    content: @Composable (ColumnScope.() -> Unit)
) {
    var cardMod = modifier.fillMaxWidth()

    cardMod = if (totalSize == 1) {
        cardMod.padding(vertical = outerCardVerticalPadding)
    } else when (index) {
        0 -> cardMod.padding(top = outerCardVerticalPadding)
        totalSize - 1 -> cardMod.padding(bottom = outerCardVerticalPadding)
        else -> cardMod.padding(vertical = 0.dp)
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = outerCardCornerElevation),
        modifier = cardMod,
        shape = when (totalSize) {
            1 -> RoundedCornerShape(outerCardCornerRounding)
            else -> when (index) {
                0 -> RoundedCornerShape(
                    topStart = outerCardCornerRounding,
                    topEnd = outerCardCornerRounding
                )

                totalSize - 1 -> RoundedCornerShape(
                    bottomStart = outerCardCornerRounding,
                    bottomEnd = outerCardCornerRounding
                )

                else -> RoundedCornerShape(0)
            }
        }
    ) {
        content()
    }
}