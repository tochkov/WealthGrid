package com.topdownedge.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topdownedge.domain.fmtPercent
import com.topdownedge.presentation.R
import com.topdownedge.presentation.ui.theme.ColorMaster


@Composable
fun PercentChip(
    modifier: Modifier = Modifier,
    percent: Double?,
    hasBackground: Boolean = false,
    fontSize: TextUnit = 16.sp
) {
    if (percent == null) return

    val priceColor = if (percent < 0)
        ColorMaster.priceRed
    else
        ColorMaster.priceGreen

    var rowMod = modifier

    if (hasBackground) {
        rowMod = rowMod
            .background(
                color = priceColor.applyAlpha(0.2f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(end = 5.dp)
    }

    Row(
        modifier = rowMod
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .offset(x = 2.dp),
            painter = painterResource(
                if (percent < 0)
                    R.drawable.ic_arrow_drop_down
                else R.drawable.ic_arrow_drop_up
            ),
            tint = priceColor,
            contentDescription = null
        )
        Text(modifier = Modifier.fillMaxHeight(),
            text = percent.fmtPercent().removePrefix("-"),
            color = priceColor,
            fontSize = fontSize
        )
    }
}