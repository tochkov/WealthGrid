package com.topdownedge.presentation.market

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.topdownedge.presentation.common.chart.SimpleCandlestickChart


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CompanyDetailsScreen(
    masterNavController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    tickerCode: String,
    tickerExchange: String,
    tickerName: String,
    percent: Double?,
    price: Double?
) {
    val viewModel: CompanyDetailsViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
//        topBar = {
//            SimpleAppBar(
//                title = tickerName,
//                onBackPress = {
//                    masterNavController.popBackStack()
//                },
//                actionImage = Icons.Default.MoreVert,
//                actionDescription = "Submit",
//                onActionClick = {
//
//                }
//            )
//        }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            val globalHorizontalPadding = 16.dp

            StockListItem(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                tickerCode = tickerCode,
                tickerName = tickerName,
                percent = percent,
                price = price,
                fontSizeTopItems = 24.sp,
                fontSizeBottomItems = 16.sp
            )


            SimpleCandlestickChart(
                modifier = Modifier
                    .height(224.dp)
                    .padding(horizontal = globalHorizontalPadding),
                chartModelProducer = viewModel.chartProducer,
                priceBars = uiState.priceBars
            )

        }
    }

}