package com.topdownedge.presentation.market

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.presentation.common.ListItemInsideCard
import com.topdownedge.presentation.common.applyAlpha
import com.topdownedge.presentation.common.chart.SimpleAssetLineChart
import com.topdownedge.presentation.common.isFromCache
import com.topdownedge.presentation.navigation.ScreenVisibilityObserver
import com.topdownedge.presentation.navigation.navigateToCompanyDetailsScreen
import com.topdownedge.presentation.ui.theme.customColorsPalette
import kotlin.math.roundToInt
import kotlin.math.sqrt

val cardElevation = 6.dp
val innerPadding = 8.dp
val globalHorizontalPadding = 16.dp
val globalVerticalPadding = 8.dp

@Composable
internal fun CompaniesListScreen(
    masterNavController: NavHostController,
) {

    val viewModel: MarketsViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value




    ScreenVisibilityObserver(
        onScreenEnter = {
            viewModel.startCollectingLivePrices()
        },
        onScreenExit = {
            viewModel.stopCollectingLivePrices()
        }
    )

    LazyColumn {
        item {
            HorizontalScrollableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("BREAKING: ")
                    }
                    append("Developer leaves feature for later implementation - the quick brown fox...")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = globalVerticalPadding),
            )
        }

        val chartCardHeight = 140.dp
        item {
            ChartCard(
                modifier = Modifier
                    .padding(horizontal = globalHorizontalPadding, vertical = globalVerticalPadding)
                    .height(chartCardHeight)
                    .fillMaxWidth(),
                modelProducer = viewModel.spyChartModelProducer,
                indexTicker = uiState.spyTicker
            )
        }
        item {
            Row(
                modifier = Modifier
                    .padding(
                        start = globalHorizontalPadding,
                        end = globalHorizontalPadding,
                        bottom = globalVerticalPadding
                    )
            ) {
                ChartCard(
                    modifier = Modifier
                        .padding(end = globalVerticalPadding / 2)
                        .height(chartCardHeight)
                        .weight(1f),
                    modelProducer = viewModel.qqqChartModelProducer,
                    indexTicker = uiState.qqqTicker
                )
                ChartCard(
                    modifier = Modifier
                        .padding(start = globalVerticalPadding / 2)
                        .height(chartCardHeight)
                        .weight(1f),
                    modelProducer = viewModel.iwmChartModelProducer,
                    indexTicker = uiState.iwmTicker
                )
            }
        }

        item {
            StockListHeader()
        }

        itemsIndexed(uiState.companyList) { index, company ->


//            ListItemInsideCard(
//                modifier = Modifier
//                    .padding(horizontal = globalHorizontalPadding)
//                    .clickable {
//                        masterNavController.navigateToCompanyDetailsScreen(
//                            Ticker(
//                                company.tickerCode,
//                                company.tickerName,
//                                company.tickerExchange
//                            )
//                        )
//                    },
//                outerCardVerticalPadding = globalVerticalPadding,
//                outerCardCornerElevation = cardElevation,
//                outerCardCornerRounding = 12.dp,
//                index = index,
//                totalSize = uiState.companyList.size,
//            ) {
            Column(
                modifier = Modifier
                    .clickable {
                        masterNavController.navigateToCompanyDetailsScreen(
                            Ticker(
                                company.tickerCode,
                                company.tickerName,
                                company.tickerExchange
                            )
                        )
                    }
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = globalHorizontalPadding),
                    color = MaterialTheme.colorScheme.primary.applyAlpha(0.1f)
                )
                StockListItem(company = company)
            }
//            }
        }
    }
}

@Composable
fun ChartCard(
    modifier: Modifier = Modifier,
    modelProducer: CartesianChartModelProducer,
    indexTicker: MarketItemUiModel
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        modifier = modifier,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = indexTicker.tickerName,
                    fontWeight = FontWeight.Bold
                )

                var percentGain = indexTicker.getPercentGain()
                val percentColor = if (percentGain.startsWith("-")) {
                    MaterialTheme.customColorsPalette.priceRed.isFromCache(indexTicker.isFromCache)
                } else {
                    MaterialTheme.customColorsPalette.priceGreen.isFromCache(indexTicker.isFromCache)
                }
                Text(
                    text = percentGain,
                    color = percentColor
                )
            }
            SimpleAssetLineChart(
                modelProducer = modelProducer,
                animateIn = false,
            )
        }
    }

}


@Composable
fun StockListHeader(

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.4f)
            .padding(
                start = globalHorizontalPadding,
                end = globalHorizontalPadding,
                top = globalVerticalPadding * 2,
                bottom = globalVerticalPadding
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Top Companies",
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            textAlign = TextAlign.End,
            text = "Price",
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun StockListItem(
    modifier: Modifier = Modifier,
    company: MarketItemUiModel,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(globalHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically

    ) {


        AsyncImage(
            modifier = Modifier
                .padding(end = globalHorizontalPadding)
                .fillMaxHeight()
                .aspectRatio(1f)
//                .clip(CircleShape),
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = null,
            model = ImageRequest.Builder(LocalContext.current)
                .data(company.logoUrl)
                .diskCacheKey(company.logoUrl)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()

        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = company.tickerCode,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.alpha(0.7f),
                text = company.tickerName,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {

            val percentGain = company.getPercentGain()
            val percentColor = if (percentGain.startsWith("-")) {
                MaterialTheme.customColorsPalette.priceRed.isFromCache(company.isFromCache)
            } else {
                MaterialTheme.customColorsPalette.priceGreen.isFromCache(company.isFromCache)
            }

            Text(
                textAlign = TextAlign.End,
                text = percentGain,
                color = percentColor,
            )

            Text(
                modifier = Modifier
                    .alpha(if (company.isFromCache) 0.4f else 1.0f),
                fontSize = 13.sp,
                textAlign = TextAlign.End,
                text = company.getCurrentPrice(),
            )

        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HorizontalScrollableText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    minAnimDuration: Int = 10000,

    ) {
    val scrollState = rememberScrollState()

    // Very rudimentary calculation, to avoid measuring the screen and complicating the logic
    val duration = minAnimDuration + (sqrt(text.length.toDouble()) * 150).roundToInt()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    LaunchedEffect(Unit) {
        while (true) {
            scrollState.scrollTo(0)
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(duration, easing = LinearEasing)
            )
        }
    }
    Row(
        modifier = modifier
            .horizontalScroll(scrollState, enabled = false)
    ) {
        Spacer(modifier = Modifier.width(screenWidth))
        Text(
            text = text,
            maxLines = 1,
            softWrap = false
        )
        Spacer(modifier = Modifier.width(screenWidth))
    }
}





