package com.topdownedge.presentation.portfolio

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.topdownedge.domain.fmtPrice
import com.topdownedge.presentation.navigation.ScreenVisibilityObserver
import com.topdownedge.presentation.navigation.navigateToUserPositionScreen
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

private const val PAGE_PIE_CHART = 0
private const val PAGE_LINE_CHART = 1
private const val PAGE_STATS = 2

@Composable
internal fun PortfolioScreen(
    masterNavController: NavHostController,
) {

    val viewModel: PortfolioViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(pageCount = { 3 })

    ScreenVisibilityObserver(
        onScreenEnter = {
            viewModel.startCollectingLivePrices()
        },
        onScreenExit = {
            viewModel.stopCollectingLivePrices()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .verticalScroll(rememberScrollState())
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                HorizontalPager(pagerState) { page ->
                    when (page) {
                        PAGE_PIE_CHART -> {
                            PortfolioDonut(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(30.dp)
                                    .align(Alignment.CenterHorizontally),
                                uiState = uiState,
                                onPieSliceClick = {
                                    viewModel.onPieSliceClick(it)
                                }
                            )
                        }

                        PAGE_LINE_CHART -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(30.dp)
                                    .align(Alignment.CenterHorizontally),
                                text = "PAGE_LINE_CHART"
                            )
                        }

                        PAGE_STATS -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(30.dp)
                                    .align(Alignment.CenterHorizontally),
                                text = "PAGE_STATS"
                            )
                        }
                    }
                }

                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
//                        .align(Alignment.)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(12.dp)
                        )
                    }

                }
            }


            item {
                Text(text = "Filter by: ")
            }


            items(uiState.positions) { position ->
                PositionCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clickable {
                            masterNavController.navigateToUserPositionScreen(position)
                        },
                    position = position,
                )
            }
        }

    }
}

@Composable
fun PortfolioDonut(
    modifier: Modifier = Modifier,
    uiState: PortfolioUiState,
    onPieSliceClick: (Pie) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            if (uiState.selectedPie == null) {
                Text(text = "Portfolio value:\n ${uiState.portfolioValue}")
                Text(text = "Portfolio gain: ${uiState.portfolioGain}")
            } else {
                Text(text = uiState.selectedPie!!.label!!)
                Text(text = uiState.selectedPie!!.data.fmtPrice())
            }


        }

        PieChart(
            modifier = modifier,
            data = uiState.pieList,
            onPieClick = onPieSliceClick,
            selectedScale = 1.15f,
            scaleAnimEnterSpec = spring<Float>(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            colorAnimEnterSpec = tween(300),
            colorAnimExitSpec = tween(300),
            scaleAnimExitSpec = tween(300),
            spaceDegreeAnimExitSpec = tween(300),
            //  spaceDegree = 7f,
            //  selectedPaddingDegree = 4f,
            style = Pie.Style.Stroke(width = 70.dp)
        )
    }
}

@Composable
fun PositionCard(
    modifier: Modifier = Modifier,
    position: PositionItemUiModel
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = position.tickerCode,
                    style = MaterialTheme.typography.titleMedium

                )
                val dailyGain = position.dailyPercentageGain()
                Text(
                    text = dailyGain,
                    color = if (dailyGain.startsWith("+")) Color.Green else Color.Red,
                )
            }
            Text(
                text = position.currentValue(),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Avg. price " + position.averagePrice(),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Current: " + position.currentPrice(),
                modifier = Modifier.weight(1f),
                color = if(position.isCurrentPriceFromCache) Color.Gray else MaterialTheme.colorScheme.primary
            )
            Text(
                text = "%portf: " + position.percentageOfPortfolio(),
                modifier = Modifier.weight(1f),
            )

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ASDF() {
    val position = PositionItemUiModel(
        tickerCode = "AAPL",
        tickerExchange = "US",
        averagePrice = 123.45,
        sharesQuantity = 10.0,
        currentPrice = 130.45,
        previousDayClose = 117.8,
        percentageOfPortfolio = 0.5,
        isCurrentPriceFromCache = false

    )
    PositionCard(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        position = position
    )


}