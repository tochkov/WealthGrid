package com.topdownedge.presentation.portfolio.trade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.topdownedge.domain.entities.common.TickerWithPrice
import com.topdownedge.presentation.common.HighlightedText
import com.topdownedge.presentation.common.SearchBoxAppBar

import com.topdownedge.presentation.R
import com.topdownedge.presentation.navigation.navigateToCompanyDetailsScreen
import com.topdownedge.presentation.navigation.navigateToSubmitTradeScreen

@Composable
internal fun SearchAssetScreen(
    masterNavController: NavHostController,
    isForNewTrade: Boolean
) {
    val viewModel: SearchAssetViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SearchBoxAppBar(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    viewModel.onSearchQueryChange(searchQuery.trim())
                },
                onBackClick = { masterNavController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(uiState.assets.size) { position ->
                val ticker = uiState.assets[position]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            if (isForNewTrade) {
                                masterNavController.navigateToSubmitTradeScreen(ticker)
                            } else {
                                masterNavController.navigateToCompanyDetailsScreen(
                                    TickerWithPrice(
                                        ticker.code, ticker.name, ticker.exchange, null, null, null
                                    )
                                )
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HighlightedText(
                        text = ticker.code,
                        highlightSegment = searchQuery.trim(),
                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
                    )

                    HighlightedText(
                        text = ticker.name,
                        highlightSegment = searchQuery.trim(),
                        modifier = Modifier
                            .weight(1f)
                    )

//                    Text(
//                        text = ticker.indexWeight.toString(),
//                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
//                    )
                }
                HorizontalDivider(thickness = 1.dp)

            }

            if (uiState.noResultsState) {
                item {
                    Text(
                        text = stringResource(R.string.no_results_found),
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

    }
}
