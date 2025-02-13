package com.topdownedge.presentation.portfolio


import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.topdownedge.domain.fmtPercent
import com.topdownedge.presentation.R
import com.topdownedge.presentation.common.ListItemInsideCard
import com.topdownedge.presentation.common.applyAlpha
import com.topdownedge.presentation.common.getLogoUrl
import com.topdownedge.presentation.navigation.ScreenVisibilityObserver
import com.topdownedge.presentation.navigation.navigateToUserPositionScreen
import com.topdownedge.presentation.ui.theme.ColorMaster
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

private const val PAGE_PIE_CHART = 0
private const val PAGE_LINE_CHART = 1
private const val PAGE_STATS = 2


val cardElevation = 6.dp
val cardRounding = 12.dp
val zzzInnerPadding = 16.dp
val zzzHorizontalPadding = 11.dp
val zzzVerticalPadding = 8.dp

val zzzBigTextSize = 22.sp
val zzzLessBigTextSize = 18.sp
val zzzPercentTextSize = 13.sp

@Composable
internal fun PortfolioScreen(
    masterNavController: NavHostController,
) {

    val viewModel: PortfolioViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if (uiState.positions.isEmpty() && !uiState.isLoading) {
        EmptyPortfolioScreen()
        return
    }

    ScreenVisibilityObserver(
        onScreenEnter = {
            viewModel.startCollectingLivePrices()
        },
        onScreenExit = {
            viewModel.stopCollectingLivePrices()
        }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            PortfolioViewPager(viewModel, uiState)
        }

        itemsIndexed(uiState.positions) { index, position ->
            ListItemInsideCard(
                modifier = Modifier
                    .padding(horizontal = zzzHorizontalPadding),
                outerCardVerticalPadding = 12.dp,
                outerCardCornerElevation = cardElevation,
                outerCardCornerRounding = cardRounding,
                index = index,
                totalSize = uiState.positions.size,
            ) {
                Column {
                    if (index == 0) {
                        PositionItemHeader()
                    }
                    PositionCardItem(
                        modifier = Modifier
                            .padding(
                                start = zzzHorizontalPadding,
                                end = zzzHorizontalPadding,
                                top = 20.dp,
                                bottom = 4.dp
                            )
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                masterNavController.navigateToUserPositionScreen(position)
                            },
                        position = position
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyPortfolioScreen() {
    Column(
//        modifier = Modifier.alpha(0.7f),
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.curve_arrow_up_8_svgrepo_com),
            contentDescription = null,
            tint = ColorMaster.primary.applyAlpha(0.4f),
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.End)
                .rotate(10f)
//                .alpha(0.4f)

        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 30.dp,
                    bottom = 0.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
            text = "You don't have any positions.",
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp
                ),
            text = "To add a new one click the right corner",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier
                .alpha(0.6f)
                .padding(horizontal = 42.dp, vertical = 54.dp),
            painter = painterResource(R.drawable.undraw_personal_goals_f9bb),
            contentDescription = null,
        )
    }
}

@Composable
private fun PortfolioViewPager(
    viewModel: PortfolioViewModel,
    uiState: PortfolioUiState,
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val centralItemVerticalPadding = 16.dp
    val centralItemHorizontalPadding = 30.dp
    HorizontalPager(pagerState) { page ->
        when (page) {
            PAGE_PIE_CHART -> {
                PortfolioDonut(
                    modifier = Modifier.padding(
                        vertical = centralItemVerticalPadding,
                        horizontal = centralItemHorizontalPadding
                    ),
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
                        .padding(
                            horizontal = centralItemHorizontalPadding,
                            vertical = centralItemVerticalPadding
                        ),
                    text = "PAGE_LINE_CHART"
                )
            }

            PAGE_STATS -> {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(
                            horizontal = centralItemHorizontalPadding,
                            vertical = centralItemVerticalPadding
                        ),
                    text = "PAGE_STATS"
                )
            }
        }
    }

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 6.dp),
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
                    .size(9.dp)
            )
        }

    }
}


@Composable
fun PortfolioDonut(
    modifier: Modifier = Modifier,
    uiState: PortfolioUiState,
    onPieSliceClick: (Pie) -> Unit,
) {

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val position = uiState.selectedPosition?.let { uiState.positions[it] }

            val topText: AnnotatedString = if (position == null) {
                buildAnnotatedString { append("My Portfolio") }
            } else {
                buildAnnotatedString {
                    append(position.tickerCode)
                    withStyle(
                        style = SpanStyle(
                            color = position.positionColor.applyAlpha(0.8f),
                        )
                    ) {
                        append(" (${position.percentageOfPortfolio()})")
                    }
                }
            }

            val positionValue = if (position == null) {
                uiState.portfolioValue
            } else {
                position.currentValue()
            }

            val gainPct = if (position == null) {
                uiState.portfolioGain.removeSuffix("%").toDoubleOrNull()
            } else {
                position.totalPNLPercentDouble()
            }

            Text(
                text = topText,
                fontSize = 17.sp
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp, top = 4.dp),
                text = positionValue,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold

            )
            PercentChip(
                percent = gainPct
            )

        }

        PieChart(
            modifier = Modifier
                .aspectRatio(1f),
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
            style = Pie.Style.Stroke(width = 65.dp)
        )
    }
}

@Composable
private fun PositionItemHeader(

) {
    Row(
        Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Positions",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Icon(
            modifier = Modifier
                .background(
                    color = ColorMaster.primary.applyAlpha(0.2f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(5.dp),
            painter = painterResource(R.drawable.ic_sort_swap_vert),
            contentDescription = "Sort",
//                                    tint =
        )
    }
}

@Composable
fun PositionCardItem(
    modifier: Modifier = Modifier,
    position: PositionItemUiModel
) {
    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(end = zzzHorizontalPadding)
                    .fillMaxHeight()
                    .aspectRatio(1f)
//                    .clip(CircleShape),
                    .clip(RoundedCornerShape(8.dp)),
                contentDescription = null,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getLogoUrl(position.tickerCode))
                    .diskCacheKey(position.tickerCode)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build()

            )
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = position.tickerName,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = position.currentValue(),
                    fontSize = zzzBigTextSize,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Column(
                modifier = Modifier.padding(start = 6.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = position.totalPNL(),
                    fontSize = zzzLessBigTextSize,
                    color = if (position.totalPNL().startsWith("-"))
                        ColorMaster.priceRed
                    else ColorMaster.priceGreen
                )

                PercentChip(
                    percent = position.totalPNLPercentDouble()
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(5f)
                    .alpha(0.4f)
                    .height(2.dp),
                gapSize = 2.dp,
                progress = { (position.percentageOfPortfolio!! / 100.00).toFloat() },
                color = position.positionColor,
                drawStopIndicator = {}
            )
            Text(
                text = position.percentageOfPortfolio(),
                modifier = Modifier
                    .weight(1f)
                    .alpha(0.4f),
                textAlign = TextAlign.End,
                fontSize = zzzPercentTextSize,
            )
        }
    }

}

@Composable
fun PercentChip(
    modifier: Modifier = Modifier,
    percent: Double?
) {
    if (percent == null) return

    val priceColor = if (percent < 0)
        ColorMaster.priceRed
    else
        ColorMaster.priceGreen

    Row(
        modifier = modifier
            .background(
                color = priceColor.applyAlpha(0.2f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(end = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                if (percent < 0)
                    R.drawable.ic_arrow_drop_down
                else R.drawable.ic_arrow_drop_up
            ),
            tint = priceColor,
            contentDescription = null
        )
        Text(
            text = percent.fmtPercent(),
            color = priceColor
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ASDF() {
//    val position = PositionItemUiModel(
//        tickerCode = "AAPL",
//        tickerExchange = "US",
//        tickerName = "Apple Inc.",
//        averagePrice = 123.45,
//        sharesQuantity = 1000.0,
//        currentPrice = 131.17,
//        previousDayClose = 117.8,
//        percentageOfPortfolio = 27.43,
//        isCurrentPriceFromCache = false,
//        positionColor = ColorMaster.primary
//
//    )
//    PositionCardItem(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth(),
//        position = position
//    )
    EmptyPortfolioScreen()


}