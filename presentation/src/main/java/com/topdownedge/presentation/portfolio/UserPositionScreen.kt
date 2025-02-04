package com.topdownedge.presentation.portfolio

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.topdownedge.domain.entities.UserTrade
import com.topdownedge.domain.fmt
import com.topdownedge.domain.fmtPrice
import com.topdownedge.presentation.common.SimpleAppBar

@Composable
fun UserPositionScreen(
    tickerCode: String,
    tickerExchange: String,
) {
    val viewModel: UserPositionViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            SimpleAppBar(
                title = "Trades for $tickerCode",
                onBackPress = {

                },
                actionImage = Icons.Default.MoreVert,
                actionDescription = "Submit",
                onActionClick = {

                }
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {

                items(uiState.trades.size) { index ->
                    val trade = uiState.trades[index]
                    Log.e("XXX", "Index: $index, Trade: $trade")
                    TradeCard(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        trade = trade,
                    )

                }

            }

        }
    }


}

@Composable
fun TradeCard(
    modifier: Modifier = Modifier,
    trade: UserTrade,

    ) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {


            Text(text = trade.tickerCode, modifier = Modifier.padding(8.dp))
            Text(text = trade.dateTraded.fmt(), modifier = Modifier.padding(8.dp))
            Text(text = trade.price.fmtPrice(), modifier = Modifier.padding(8.dp))
            Text(text = trade.shares.fmtPrice(), modifier = Modifier.padding(8.dp))


        }
    }


}