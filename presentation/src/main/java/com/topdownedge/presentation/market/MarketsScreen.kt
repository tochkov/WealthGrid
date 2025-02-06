package com.topdownedge.presentation.market

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.topdownedge.presentation.navigation.navigateToCompanyDetailsScreen

@Composable
internal fun CompaniesListScreen(
    masterNavController: NavHostController,
) {

    val viewModel: MarketsViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
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
                color = if(company.isFromCache) Color.Gray else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
            val percentGain = company.getPercentGain()
            val percentColor = if(company.isFromCache){
                Color.Gray
            } else {
                if(percentGain.startsWith("-")){
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