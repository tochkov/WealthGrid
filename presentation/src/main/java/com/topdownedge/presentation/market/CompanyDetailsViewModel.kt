package com.topdownedge.presentation.market

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.repositories.PriceDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


data class CompanyDetailsUiState(
    val priceBars: List<PriceBar> = emptyList()
)

@HiltViewModel
class CompanyDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val priceDataRepository: PriceDataRepository
) : ViewModel() {

    val uiState: StateFlow<CompanyDetailsUiState>
        field = MutableStateFlow(CompanyDetailsUiState())


    val tickerCode = savedStateHandle.get<String>("tickerCode") ?: ""
    val tickerExchange = savedStateHandle.get<String>("tickerExchange") ?: ""
    val symbol = "$tickerCode.$tickerExchange"

    val chartProducer = CartesianChartModelProducer()

    init {

        viewModelScope.launch {
            val startDate = LocalDate.now().minusMonths(2)
            priceDataRepository.getHistoricalDailyPrices(symbol, fromDate = startDate)
                .distinctUntilChanged()
                .collectLatest { result ->
                    if (result.isSuccess) {

                        val priceBars = result.getOrNull()!!
                        uiState.update {
                            it.copy(
                                priceBars = priceBars
                            )
                        }

                        chartProducer.runTransaction {
                            candlestickSeries(
                                opening = priceBars.map { it.open },
                                closing = priceBars.map { it.close },
                                high = priceBars.map { it.high },
                                low = priceBars.map { it.low },
                            )
                        }
                    }

                }

        }
    }

}