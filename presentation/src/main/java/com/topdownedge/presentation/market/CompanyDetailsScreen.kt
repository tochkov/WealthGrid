package com.topdownedge.presentation.market

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.topdownedge.presentation.common.SimpleAppBar
import com.topdownedge.presentation.common.chart.SimpleCandlestickChart


@Composable
fun CompanyDetailsScreen(
    tickerCode: String,
    tickerExchange: String,
    tickerName: String
) {
    val viewModel: CompanyDetailsViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            SimpleAppBar(
                title = tickerName,
                onBackPress = {

                },
                actionImage = Icons.Default.MoreVert,
                actionDescription = "Submit",
                onActionClick = {

                }
            )
        }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = tickerCode)
            Text(text = tickerExchange)
            Text(text = tickerName)

                SimpleCandlestickChart(
                    modifier = Modifier.height(224.dp),
                    chartModelProducer = viewModel.chartProducer,
                    priceBars = uiState.priceBars
                )

        }
    }

}