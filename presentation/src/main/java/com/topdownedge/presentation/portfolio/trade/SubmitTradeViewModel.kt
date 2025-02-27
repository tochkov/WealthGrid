package com.topdownedge.presentation.portfolio.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.topdownedge.domain.entities.UserTrade
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.fmtPrice
import com.topdownedge.domain.repositories.PriceDataRepository
import com.topdownedge.domain.repositories.UserPortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

enum class InputError {
    DATE,
    PRICE,
    SHARES,
}

sealed interface UiEvent {
    data class ShowToast(val inputError: InputError) : UiEvent
    data class Navigate(val route: String) : UiEvent
}

data class SubmitTradeUiState(
    var ticker: String = "",
    var name: String = "",
    var displayPriceStr: String = "",
    var displayChangePct: Double? = null,
    var chartError: Boolean = false,
    var inputError: InputError? = null,

    var manualInputDetected: Boolean = false,
    var isBuyState: Boolean = true,
    var selectedDate: LocalDate = LocalDate.now(),
    var selectedPrice: String = "",
    var selectedShares: String = "",
    var totalPosition: String = "0.00",
)

@HiltViewModel
class SubmitTradeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    priceDataRepository: PriceDataRepository,
    val userPortfolioRepository: UserPortfolioRepository,
) : ViewModel() {

    val tickerCode = savedStateHandle.get<String>("tickerCode") ?: ""
    val tickerExchange = savedStateHandle.get<String>("tickerExchange") ?: ""
    val tickerName = savedStateHandle.get<String>("tickerName") ?: ""
    val symbol = "$tickerCode.$tickerExchange"

    val uiState: StateFlow<SubmitTradeUiState>
        field = MutableStateFlow(SubmitTradeUiState())

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    var priceBars: List<PriceBar> = emptyList()
    val chartModelProducer = CartesianChartModelProducer()

    init {

        val startDate = LocalDate.now().minusMonths(6)

        viewModelScope.launch {
            priceDataRepository.getHistoricalDailyPrices(symbol, fromDate = startDate)
                .distinctUntilChanged()
                .collectLatest { result ->
                    if (result.isFailure) {
                        uiState.update {
                            it.copy(
                                chartError = true
                            )
                        }
                    } else {
                        priceBars = result.getOrNull() ?: emptyList()
                        uiState.update {
                            it.copy(
                                chartError = false,
                                displayPriceStr = getLastAvailablePrice().fmtPrice(),
                                displayChangePct = getLastPriceChangePct(),
                                selectedDate = getLastAvailableDate() ?: LocalDate.now(),
                                selectedPrice = getLastAvailablePrice().fmtPrice()
                            )
                        }
                        chartModelProducer.runTransaction {
                            lineSeries { series(priceBars.map { it.close }) }
                        }
                    }
                }
        }
    }

    fun userSetBuyState(isBuyState: Boolean) {
        uiState.update {
            it.copy(
                manualInputDetected = true,
                isBuyState = isBuyState
            )
        }
    }

    fun userSetDate(date: LocalDate) {
        uiState.update {
            it.copy(
                manualInputDetected = true,
                selectedDate = date
            )
        }
    }

    fun userSetPrice(price: String) {
        uiState.update {
            it.copy(
                manualInputDetected = true,
                selectedPrice = price,
                totalPosition = calculateTotalPosition(price, it.selectedShares)
            )
        }
    }

    fun userSetShares(shares: String) {
        uiState.update {
            it.copy(
                manualInputDetected = true,
                selectedShares = shares,
                totalPosition = calculateTotalPosition(it.selectedPrice, shares)
            )
        }
    }

    private fun calculateTotalPosition(price: String, shares: String): String {
        val priceDouble = price.toDoubleOrNull() ?: return "0.00"
        val sharesDouble = shares.toDoubleOrNull() ?: return "0.00"
        return (priceDouble * sharesDouble).fmtPrice()
    }

    fun onSubmitClicked() {
        val isInputValid = validateInput()
        if (isInputValid) {
            viewModelScope.launch {
                val shares = uiState.value.selectedShares.toDouble()
                val sharesCorrected =
                    if (!uiState.value.isBuyState && shares > 0) -shares else shares
                userPortfolioRepository.submitUserTrade(
                    UserTrade(
                        tickerCode = tickerCode,
                        tickerExchange = tickerExchange,
                        tickerName = tickerName,
                        dateSubmitted = LocalDate.now(),
                        dateTraded = uiState.value.selectedDate,
                        price = uiState.value.selectedPrice.toDouble(),
                        shares = sharesCorrected,
                        isBuy = uiState.value.isBuyState,
                    )
                )
                _uiEvents.send(UiEvent.Navigate("success_back"))
            }
        }
    }

    private fun validateInput(): Boolean {
        val uiState = uiState.value
        if (uiState.selectedDate.isAfter(LocalDate.now())) {
            showErrorToast(InputError.DATE)
            return false
        }
        if (uiState.selectedPrice.toDoubleOrNull() == null) {
            showErrorToast(InputError.PRICE)
            return false
        }
        if (uiState.selectedShares.toDoubleOrNull() == null) {
            showErrorToast(InputError.SHARES)
            return false
        }
        return true
    }

    private fun showErrorToast(inputError: InputError) {
        viewModelScope.launch {
            _uiEvents.send(UiEvent.ShowToast(inputError))
        }
    }

    fun onUserClickOrDragChart(position: Int) {
        uiState.update {
            val lastPrice = getLastAvailablePrice(position).fmtPrice()
            it.copy(
                manualInputDetected = true,
                displayPriceStr = lastPrice,
                displayChangePct = getLastPriceChangePct(position),
                selectedDate = getLastAvailableDate(position) ?: LocalDate.now(),
                selectedPrice = lastPrice,
                totalPosition = calculateTotalPosition(lastPrice, it.selectedShares)
            )
        }

    }

    fun onUserChartInteractionOver() {
        uiState.update {
            it.copy(
                displayPriceStr = getLastAvailablePrice().fmtPrice(),
                displayChangePct = getLastPriceChangePct(),
            )
        }
    }

    val DEFAULT_POSITION = -1
    private fun getLastAvailableDate(index: Int = DEFAULT_POSITION): LocalDate? {
        return if (index <= DEFAULT_POSITION) {
            priceBars.lastOrNull()?.date
        } else {
            priceBars.getOrNull(index)?.date
        }
    }

    private fun getLastAvailablePrice(index: Int = DEFAULT_POSITION): Double? {
        return if (index <= DEFAULT_POSITION) {
            priceBars.lastOrNull()?.close
        } else {
            priceBars.getOrNull(index)?.close
        }
    }

    private fun getLastPriceChangePct(index: Int = DEFAULT_POSITION): Double? {

        // if index not specified and there are at least 2 price bars, use last index
        val targetIndex = if (index <= DEFAULT_POSITION && priceBars.size >= 2) {
            priceBars.size - 1 // last index
            // if index and previous index are in bounds -> use index
        } else if (index >= 1 && index <= priceBars.size - 1) {
            index
        } else {
            return null
        }

        val lastClose = priceBars[targetIndex].close
        val prevClose = priceBars[targetIndex - 1].close
        return 100 * ((lastClose - prevClose) / prevClose)
    }


}