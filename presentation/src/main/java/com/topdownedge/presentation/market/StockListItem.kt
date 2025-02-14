package com.topdownedge.presentation.market

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.topdownedge.domain.fmtPrice
import com.topdownedge.presentation.common.PercentChip
import com.topdownedge.presentation.common.getLogoUrl


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun StockListItem(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    tickerCode: String,
    tickerName: String,
    percent: Double?,
    price: Double?,
    isFromCache: Boolean = false,
    fontSizeTopItems: TextUnit = 16.sp,
    fontSizeBottomItems: TextUnit = 13.sp,
    basePadding: Dp = 16.dp
) {
    with(sharedTransitionScope) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(basePadding),
            verticalAlignment = Alignment.CenterVertically

        ) {

            AsyncImage(
                modifier = Modifier
                    .padding(end = basePadding)
                    .fillMaxHeight()
                    .aspectRatio(1f)
//                .clip(CircleShape),
                    .sharedBounds(
                        sharedTransitionScope.rememberSharedContentState(
                            key = "image-${tickerCode}",
                        ),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .clip(RoundedCornerShape(8.dp)),
                contentDescription = null,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getLogoUrl(tickerCode))
                    .crossfade(true)
                    .diskCacheKey("image-${tickerCode}")
                    .memoryCacheKey("image-${tickerCode}")
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build()

            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = tickerCode,
                    fontSize = fontSizeTopItems,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .wrapContentWidth()
                        .sharedBounds(
                            sharedTransitionScope.rememberSharedContentState(
                                key = "code-${tickerCode}"
                            ),
                            animatedVisibilityScope = animatedContentScope,
                        enter = fadeIn(),
                        exit = fadeOut(),
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()

                        )
                )
                Text(
                    modifier = Modifier
                        .sharedBounds(
                            sharedTransitionScope.rememberSharedContentState(
                                key = "name-${tickerCode}"
                            ),
                            animatedVisibilityScope = animatedContentScope
                        )
                        .alpha(0.7f),
                    text = tickerName,
                    fontSize = fontSizeBottomItems,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier
                    .alpha(if (isFromCache) 0.4f else 1.0f),
                horizontalAlignment = Alignment.End
            ) {

                PercentChip(
                    percent = percent,
                    modifier = Modifier.sharedBounds(
                        sharedTransitionScope.rememberSharedContentState(
                            key = "percent-${tickerCode}"
                        ),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    fontSize = fontSizeTopItems
                )

                Text(
                    modifier = Modifier.sharedBounds(
                        sharedTransitionScope.rememberSharedContentState(
                            key = "price-${tickerCode}"
                        ),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    fontSize = fontSizeBottomItems,
                    textAlign = TextAlign.End,
                    text = price.fmtPrice(),
                )

            }
        }
    }
}