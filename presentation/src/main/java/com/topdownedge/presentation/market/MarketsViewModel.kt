package com.topdownedge.presentation.market

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.domain.repositories.MarketInfoRepository
import com.topdownedge.domain.repositories.PriceDataRepository
import com.topdownedge.presentation.portfolio.trade.AssetSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


data class MarketsUiState(
    val companyList: List<Ticker> = emptyList(),
)


@HiltViewModel
class MarketsViewModel @Inject constructor(
    private val priceDataRepository: PriceDataRepository,
    private val marketInfoRepository: MarketInfoRepository
) : ViewModel() {

    val uiState: StateFlow<MarketsUiState>
        field = MutableStateFlow(MarketsUiState())


    val spyChartModelProducer = CartesianChartModelProducer()
    val qqqChartModelProducer = CartesianChartModelProducer()
    val iwmChartModelProducer = CartesianChartModelProducer()

    init {

        val fromDate = LocalDate.now().minusMonths(3)

        Log.e("XXX", "INITTTTTT")
        viewModelScope.launch {
            priceDataRepository.getHistoricalDailyPrices("SPY", fromDate = fromDate)
                .distinctUntilChanged()
                .collectLatest {
                    Log.e("XXX", "SPY: $it")
                    spyChartModelProducer.runTransaction {
                        lineSeries { series(it.getOrNull()?.map { it.close } ?: emptyList()) }
                    }
                }
        }

        viewModelScope.launch {
            priceDataRepository.getHistoricalDailyPrices("QQQ", fromDate = fromDate)
                .distinctUntilChanged()
                .collectLatest {
                    Log.e("XXX", "QQQ: $it")
                    qqqChartModelProducer.runTransaction {
                        lineSeries { series(it.getOrNull()?.map { it.close } ?: emptyList()) }
                    }
                }
        }

        viewModelScope.launch {
            priceDataRepository.getHistoricalDailyPrices("IWM", fromDate = fromDate)
                .distinctUntilChanged()
                .collectLatest {
                    Log.e("XXX", "IWM: $it")
                    iwmChartModelProducer.runTransaction {
                        lineSeries { series(it.getOrNull()?.map { it.close } ?: emptyList()) }
                    }
                }
        }

        viewModelScope.launch {
            marketInfoRepository.getInitialSearchTickerList()
                .distinctUntilChanged()
                .collectLatest { result ->
                    if (result.isSuccess) {
                        uiState.update {
                            it.copy(
                                companyList = result.getOrNull() ?: emptyList()
                            )
                        }

                    } else {
                        Log.e("XXX", "tickers.isFailure: ${result.exceptionOrNull()}")

                    }
                }

        }

    }

    fun updateVisibleItems(firstVisibleIndex: Int, lastVisibleIndex: Int) {
        Log.e("XXX", "visible: $firstVisibleIndex, $lastVisibleIndex")
    }


}