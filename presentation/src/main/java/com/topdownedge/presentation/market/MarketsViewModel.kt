package com.topdownedge.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.topdownedge.domain.repositories.LivePricesRepository
import com.topdownedge.domain.repositories.PriceDataRepository
import com.topdownedge.presentation.common.getLogoUrl
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
    val spyTicker: MarketItemUiModel = SPY,
    val qqqTicker: MarketItemUiModel = QQQ,
    val iwmTicker: MarketItemUiModel = IWM,
    val companyList: List<MarketItemUiModel> = emptyList(),
)

@HiltViewModel
class MarketsViewModel @Inject constructor(
    private val priceDataRepository: PriceDataRepository,
    private val livePricesRepository: LivePricesRepository
) : ViewModel() {

    val uiState: StateFlow<MarketsUiState>
        field = MutableStateFlow(MarketsUiState())

    val spyChartModelProducer = CartesianChartModelProducer()
    val qqqChartModelProducer = CartesianChartModelProducer()
    val iwmChartModelProducer = CartesianChartModelProducer()

    init {
        val fromDate = LocalDate.now().minusMonths(3)
        loadIndexChart(uiState.value.spyTicker.tickerCode, spyChartModelProducer, fromDate)
        loadIndexChart(uiState.value.qqqTicker.tickerCode, qqqChartModelProducer, fromDate)
        loadIndexChart(uiState.value.iwmTicker.tickerCode, iwmChartModelProducer, fromDate)

        getIndexTickersPrices()

        getTopCompanyListWithPrices()
    }

    private fun loadIndexChart(
        tickerCode: String,
        chartModelProducer: CartesianChartModelProducer,
        fromDate: LocalDate = LocalDate.now().minusMonths(3)
    ) {
        viewModelScope.launch {
            priceDataRepository.getHistoricalDailyPrices(tickerCode, fromDate = fromDate)
                .distinctUntilChanged()
                .collectLatest {
                    if (it.getOrNull()?.isNotEmpty() == true) {
                        chartModelProducer.runTransaction {
                            lineSeries { series(it.getOrNull()!!.map { it.close }) }
                        }
                    }
                }
        }
    }

    private fun getIndexTickersPrices() {
        viewModelScope.launch {
            val indexesList = listOf(
                uiState.value.spyTicker,
                uiState.value.qqqTicker,
                uiState.value.iwmTicker
            ).map { it.tickerCode }
            priceDataRepository.getLastKnownPriceData(indexesList).collect { pricesResult ->
                val pricesMap = pricesResult.getOrNull()
                if (pricesMap?.isEmpty() == false) {
                    uiState.update {
                        it.copy(
                            spyTicker = it.spyTicker.updatePrice(pricesMap[uiState.value.spyTicker.tickerCode]),
                            qqqTicker = it.qqqTicker.updatePrice(pricesMap[uiState.value.qqqTicker.tickerCode]),
                            iwmTicker = it.iwmTicker.updatePrice(pricesMap[uiState.value.iwmTicker.tickerCode]),
                        )
                    }
                }
            }
        }
    }


    private fun getTopCompanyListWithPrices() {
        viewModelScope.launch {
            priceDataRepository.getMarketTickersWithPrice().collect { result ->

                if (result.isSuccess) {
                    val uiList = result.getOrNull()!!.tickers.map { ticker ->
                        MarketItemUiModel(
                            tickerCode = ticker.code,
                            tickerName = ticker.name,
                            tickerExchange = ticker.exchange,
                            logoUrl = getLogoUrl(ticker.code),
                            currentPrice = ticker.lastPrice,
                            previousClose = ticker.previousClose,
                            isFromCache = result.getOrNull()!!.isFromCache
                        )
                    }


                    uiState.update {
                        it.copy(
                            companyList = uiList
                        )
                    }
                    subscribeToTickersPriceUpdates()
                }

            }
        }
    }

    fun startCollectingLivePrices() {
        viewModelScope.launch {
            livePricesRepository.observeLivePricesConnection().collect { livePrices ->
                uiState.update {
                    it.copy(
                        companyList = it.companyList.map { company ->
                            company.copy(
                                currentPrice = livePrices[company.tickerCode]
                                    ?: company.currentPrice,
                                isFromCache = false
                            )
                        }
                    )
                }
            }
        }
        subscribeToTickersPriceUpdates()
    }

    fun stopCollectingLivePrices() {
        livePricesRepository.closeLivePricesConnection()
    }

    private fun subscribeToTickersPriceUpdates() {
        livePricesRepository.subscribeToLivePrices(uiState.value.companyList.map { it.tickerCode })
    }


}