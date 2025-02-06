package com.topdownedge.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.domain.fmtPercent
import com.topdownedge.domain.fmtPrice
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

data class CompanyItemUiModel(
    val tickerCode: String,
    val tickerName: String,
    val tickerExchange: String,
    val logoUrl: String,
    val currentPrice: Double?,
    val previousClose: Double?,
    val isFromCache: Boolean = true
){
    fun getCurrentPrice() = currentPrice.fmtPrice()
    fun getPercentGain(): String {
        if(currentPrice == null || previousClose == null) return ""
        val percentGain = (currentPrice - previousClose) / previousClose * 100
        return if(percentGain >= 0) {
            "+${percentGain.fmtPercent()}"
        } else {
            percentGain.fmtPercent()
        }
    }

}

data class MarketsUiState(
    val companyList: List<CompanyItemUiModel> = emptyList(),
)


@HiltViewModel
class MarketsViewModel @Inject constructor(
    private val priceDataRepository: PriceDataRepository,
    private val livePricesRepository: LivePricesRepository
) : ViewModel() {

    val uiState: StateFlow<MarketsUiState>
        field = MutableStateFlow(MarketsUiState())

    val spyTicker = Ticker("SPY", "S&P 500", "US")
    val qqqTicker = Ticker("QQQ", "Nasdaq 100", "US")
    val iwmTicker = Ticker("IWM", "Russell 2000", "US")

    val spyChartModelProducer = CartesianChartModelProducer()
    val qqqChartModelProducer = CartesianChartModelProducer()
    val iwmChartModelProducer = CartesianChartModelProducer()

    init {

        val fromDate = LocalDate.now().minusMonths(3)

        loadIndexChart(spyTicker, spyChartModelProducer, fromDate)
        loadIndexChart(qqqTicker, qqqChartModelProducer, fromDate)
        loadIndexChart(iwmTicker, iwmChartModelProducer, fromDate)

        getTopCompanyListWithPrices()
    }

    private fun loadIndexChart(
        ticker: Ticker,
        chartModelProducer: CartesianChartModelProducer,
        fromDate: LocalDate = LocalDate.now().minusMonths(3)
    ) {
        viewModelScope.launch {
            priceDataRepository.getHistoricalDailyPrices(ticker.code, fromDate = fromDate)
                .distinctUntilChanged()
                .collectLatest {
                    if(it.getOrNull()?.isNotEmpty() == true) {
                        chartModelProducer.runTransaction {
                            lineSeries { series(it.getOrNull()!!.map { it.close } )}
                        }
                    }
                }
        }
    }

    private fun getTopCompanyListWithPrices(){
        viewModelScope.launch{
            priceDataRepository.getMarketTickersWithPrice().collect{ result ->

                if(result.isSuccess) {
                    val uiList = result.getOrNull()!!.tickers.map { ticker ->
                        CompanyItemUiModel(
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
                                currentPrice = livePrices[company.tickerCode] ?: company.currentPrice,
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