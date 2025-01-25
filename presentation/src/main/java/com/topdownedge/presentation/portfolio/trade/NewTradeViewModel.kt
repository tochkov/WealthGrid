package com.topdownedge.presentation.portfolio.trade

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.repositories.PriceDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


data class NewTradeUiState(
    var ticker: String = "",
    var date: LocalDate = LocalDate.now(),
    var price: Long = 0L,
    var shares: Int = 0,
    val modelProducer : CartesianChartModelProducer =  CartesianChartModelProducer(),
    var priceBars : List<PriceBar> = emptyList(),
)

@HiltViewModel
class NewTradeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    priceDataRepository: PriceDataRepository
) : ViewModel() {

    val tickerCode = savedStateHandle.get<String>("tickerCode") ?: ""
    val tickerExchange = savedStateHandle.get<String>("tickerExchange") ?: ""

    val uiState: StateFlow<NewTradeUiState>
        field = MutableStateFlow(NewTradeUiState())


    init {
        val timeStart = System.currentTimeMillis()

        Log.d("XXX", "Ticker: $tickerCode, Exchange: $tickerExchange")
        val symbol = "$tickerCode.$tickerExchange"
        viewModelScope.launch {
            priceDataRepository.getHistoricalDailyPrices(symbol)
                .distinctUntilChanged()
                .collect { result ->
                    Log.e(
                        "XXX",
                        "${symbol} | ${result.isSuccess} || timer : ${System.currentTimeMillis() - timeStart}"
                    )

                    val priceBars = result.getOrNull()!!
                    val date = priceBars.map { it.date.toEpochDay() }
                    val o = priceBars.map { it.open }
                    val h = priceBars.map { it.high }
                    val l = priceBars.map { it.low }
                    val c = priceBars.map { it.close }

                        Log.d("XXX", c.toString())

                    uiState.value.modelProducer.runTransaction {
                        lineSeries { series(c) }
                    }


                    uiState.update{
                        it.copy(
                            priceBars = priceBars
                        )
                    }




                }
        }
    }

}