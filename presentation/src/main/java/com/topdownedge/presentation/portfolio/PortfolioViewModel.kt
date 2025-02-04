package com.topdownedge.presentation.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.fmtPrice
import com.topdownedge.domain.repositories.LivePricesRepository
import com.topdownedge.domain.repositories.UserPortfolioRepository
import com.topdownedge.presentation.common.randomColor
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PositionUiModel(
    var ticker: String,
    var valueDollarz: Double,
    var avgPrice: Double,
    var selected: Boolean = false
)

data class PortfolioUiState(
    var positions: List<UserPosition> = listOf(),
    var pieList: List<Pie> = listOf(),
    var portfolioValue: String = "",
    var portfolioGain: String = "",
    var selectedPie: Pie? = null,
)

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val userPortfolioRepository: UserPortfolioRepository,
    private val livePricesRepository: LivePricesRepository
) : ViewModel() {

    val uiState: StateFlow<PortfolioUiState>
        field = MutableStateFlow(PortfolioUiState())

    init {
        viewModelScope.launch {
            userPortfolioRepository.getAllUserPositions().collect { positionList ->
                uiState.update {
                    it.copy(
                        positions = positionList,
                        pieList = positionList.map { position ->
                            val color = randomColor()
                            Pie(
                                label = position.tickerCode,
                                data = position.totalInvested,
                                color = color,
                                selectedColor = color
                            )
                        },
                        portfolioValue = calculatePortfolioValue(positionList),
                        portfolioGain = calculatePortfolioGain(positionList)
                    )
                }
                subscribeToTickersPriceUpdates()
            }
        }
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
                    selectedPie = null
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
                            else pie.selectedColor.copy(alpha = 0.7f)
                        )
                    },
                    selectedPie = clickedPie
                )

            }
        }
    }

    fun startCollectingLivePrices() {
        viewModelScope.launch {
            livePricesRepository.observeLivePricesConnection().collect { latestPrices ->
                uiState.update {
                    it.copy(
                        positions = it.positions.map { position ->
                            position.copy(
                                currentPrice = latestPrices[position.tickerCode]
                                    ?: position.currentPrice
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
        livePricesRepository.subscribeToLivePrices(uiState.value.positions.map { it.tickerCode })
    }

    private fun calculatePortfolioValue(positions: List<UserPosition>): String {
        var totalValue = 0.0
        for (position in positions) {
            totalValue += position.totalInvested
        }
        return totalValue.fmtPrice()
    }

    private fun calculatePortfolioGain(positions: List<UserPosition>): String {
        return "23.45%"
    }
}