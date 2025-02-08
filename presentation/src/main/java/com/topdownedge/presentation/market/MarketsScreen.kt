package com.topdownedge.presentation.market

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.presentation.common.chart.SimpleAssetLineChart
import com.topdownedge.presentation.navigation.ScreenVisibilityObserver
import com.topdownedge.presentation.navigation.navigateToCompanyDetailsScreen
import kotlin.math.roundToInt
import kotlin.math.sqrt

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

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
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
                    .padding(16.dp),
            )
        }

        val cardHeight = 140.dp
        item {
            ChartCard(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(cardHeight)
                    .fillMaxWidth(),
                modelProducer = viewModel.spyChartModelProducer,
            )
        }

        item {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                ChartCard(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(cardHeight)
                        .weight(1f),
                    modelProducer = viewModel.qqqChartModelProducer,
                )
                ChartCard(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(cardHeight)
                        .weight(1f),
                    modelProducer = viewModel.iwmChartModelProducer,
                )
            }
        }

        itemsIndexed(uiState.companyList) { index, company ->
            StockCard(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        masterNavController.navigateToCompanyDetailsScreen(
                            Ticker(
                                company.tickerCode,
                                company.tickerName,
                                company.tickerExchange
                            )
                        )
                    },
                company = company,
            )
        }


    }

}

@Composable
fun ChartCard(
    modifier: Modifier = Modifier,
    modelProducer: CartesianChartModelProducer,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SPY",
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = "435.34",
                    modifier = Modifier.padding(8.dp)
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
fun StockCard(
    modifier: Modifier = Modifier,
    company: CompanyItemUiModel,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier,
    ) {
        Row {

            Box {
                val imgUrl = company.logoUrl
                AsyncImage(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(50.dp)
                        .height(50.dp),
                    contentDescription = null,
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imgUrl)
                        .diskCacheKey(imgUrl)
//                        .networkCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build()

                )
            }


            Text(
                text = company.tickerCode,
                modifier = Modifier.padding(8.dp),
            )
            Text(
                text = company.getCurrentPrice(),
                color = if (company.isFromCache) Color.Gray else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
            val percentGain = company.getPercentGain()
            val percentColor = if (company.isFromCache) {
                Color.Gray
            } else {
                if (percentGain.startsWith("-")) {
                    Color.Red
                } else {
                    Color.Green
                }
            }
            Text(
                text = percentGain,
                color = percentColor,
                modifier = Modifier.padding(8.dp)
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





