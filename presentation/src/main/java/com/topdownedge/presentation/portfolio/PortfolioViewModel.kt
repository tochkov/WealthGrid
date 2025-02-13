package com.topdownedge.presentation.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.entities.common.LastKnownPrice
import com.topdownedge.domain.fmtMoney
import com.topdownedge.domain.fmtPercent
import com.topdownedge.domain.repositories.LivePricesRepository
import com.topdownedge.domain.repositories.PriceDataRepository
import com.topdownedge.domain.repositories.UserPortfolioRepository
import com.topdownedge.presentation.ui.theme.ColorMaster
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PortfolioUiState(
    var positions: List<PositionItemUiModel> = listOf(),
    var pieList: List<Pie> = listOf(),
    var portfolioValue: String = "",
    var portfolioGain: String = "",
    var selectedPosition: Int? = null,
    var isLoading: Boolean = false
)


@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val userPortfolioRepository: UserPortfolioRepository,
    private val priceDataRepository: PriceDataRepository,
    private val livePricesRepository: LivePricesRepository
) : ViewModel() {

    val uiState: StateFlow<PortfolioUiState>
        field = MutableStateFlow(PortfolioUiState())

    init {
        uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            userPortfolioRepository.getAllUserPositions().collect { positionList ->
                uiState.update {
                    val updatedPositions = updatePositionsFromDb(positionList)
                    it.copy(
                        positions = updatedPositions,
                        pieList = updatedPositions.toPieList(),
                        portfolioValue = calculatePortfolioValue(updatedPositions),
                        portfolioGain = calculatePortfolioGain(updatedPositions),
                        isLoading = false
                    )
                }
                getLastKnownPrices()
                subscribeToTickersPriceUpdates()
            }
        }
    }

    fun updatePositionsFromDb(positions: List<UserPosition>) =
        positions
            .sortedByDescending { it.currentValue()}
            .mapIndexed { i, userPosition ->
                userPosition.toPositionItemUiModel(true, ColorMaster.getColor(i))
            }
            .populatePctOfPortfolio()

    suspend fun getLastKnownPrices() {
        if (uiState.value.positions.isEmpty()) return

        priceDataRepository.getLastKnownPriceData(uiState.value.positions.map { it.tickerCode })
            .collect { result ->
                if (result.isSuccess) {
                    uiState.update {
                        val updatedPositions =
                            updatePositionsFromLastKnownPrice(it.positions, result.getOrNull()!!)
                        it.copy(
                            positions = updatedPositions,
                            pieList = updatedPositions.toPieList(),
                            portfolioValue = calculatePortfolioValue(updatedPositions),
                            portfolioGain = calculatePortfolioGain(updatedPositions),
                            isLoading = false
                        )
                    }
                }
            }
    }

    fun updatePositionsFromLastKnownPrice(
        currentPositions: List<PositionItemUiModel>,
        pricesMap: Map<String, LastKnownPrice>
    ) =
        currentPositions
            .map { position ->
                val priceData = pricesMap[position.tickerCode]
                position.copy(
                    currentPrice = priceData?.close ?: position.currentPrice,
                    previousDayClose = priceData?.previousClose ?: position.previousDayClose,
                    isCurrentPriceFromCache = priceData?.isFromCache ?: true
                )
            }
            .sortedByDescending { it.currentValueDouble() }
            .populatePctOfPortfolio()


    fun startCollectingLivePrices() {
        viewModelScope.launch {
            livePricesRepository.observeLivePricesConnection().collect { livePrices ->
                uiState.update {
                    val updatedPositions = updatePositionFromLivePrices(it.positions, livePrices)
                    it.copy(
                        positions = updatedPositions,
//                        pieList = updatedPositions.toPieList(),
                        portfolioValue = calculatePortfolioValue(updatedPositions),
                        portfolioGain = calculatePortfolioGain(updatedPositions),
                        isLoading = false
                    )
                }
            }
        }
        subscribeToTickersPriceUpdates()
    }

    fun updatePositionFromLivePrices(
        currentPositions: List<PositionItemUiModel>,
        pricesMap: Map<String, Double>
    ) =
        currentPositions
            .map { position ->
                position.copy(
                    currentPrice = pricesMap[position.tickerCode] ?: position.currentPrice,
                )
            }
            .sortedByDescending { it.currentValueDouble() }
            .populatePctOfPortfolio()

    fun stopCollectingLivePrices() {
        livePricesRepository.closeLivePricesConnection()
    }

    private fun subscribeToTickersPriceUpdates() {
        livePricesRepository.subscribeToLivePrices(uiState.value.positions.map { it.tickerCode })
    }

    private fun calculatePortfolioValue(positions: List<PositionItemUiModel>): String {
        return positions.sumOf { it.currentValueDouble() }.fmtMoney()
    }

    private fun calculatePortfolioGain(positions: List<PositionItemUiModel>): String {
        val totalPortfolioValue = positions.sumOf { it.currentValueDouble() }
        val totalInvestment = positions.sumOf { it.totalInvested }
        return (((totalPortfolioValue - totalInvestment) / totalInvestment) * 100).fmtPercent()
    }

    fun onPieSliceClick(clickedPie: Pie) {

        if (clickedPie.selected) {
            uiState.update {
                it.copy(
                    pieList = it.pieList.map { pie ->
                        pie.copy(
                            selected = false,
                            color = pie.selectedColor
                        )
                    },
                    selectedPosition = null
                )
            }
        } else {
            val clickedIndex = uiState.value.pieList.indexOf(clickedPie)
            uiState.update {
                it.copy(
                    pieList = it.pieList.mapIndexed { index, pie ->
                        val isSelected = index == clickedIndex
                        pie.copy(
                            selected = isSelected,
                            color = if (isSelected) pie.selectedColor
                            else pie.selectedColor.copy(alpha = 0.8f)
                        )
                    },
                    selectedPosition = uiState.value.positions.indexOf(
                        uiState.value.positions.find { it.tickerCode == clickedPie.label }
                    )
                )

            }
        }
    }
}