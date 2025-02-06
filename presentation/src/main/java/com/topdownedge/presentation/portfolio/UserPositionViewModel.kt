package com.topdownedge.presentation.portfolio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.entities.UserTrade
import com.topdownedge.domain.repositories.UserPortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UserPositionUiState(
    val trades: List<UserTrade> = emptyList()
)

@HiltViewModel
class UserPositionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val portfolioRepository: UserPortfolioRepository
) : ViewModel() {

    val uiState: StateFlow<UserPositionUiState>
        field = MutableStateFlow(UserPositionUiState())

    private val tickerCode: String = checkNotNull(savedStateHandle["tickerCode"])
    private val tickerExchange: String = checkNotNull(savedStateHandle["tickerExchange"])
    private val symbol: String = "$tickerCode.$tickerExchange"


    init {
        viewModelScope.launch {
            portfolioRepository.getUserTradesForSymbol(symbol)
                .distinctUntilChanged()
                .collectLatest { trades ->
                    uiState.update {
                        it.copy(
                            trades = trades
                        )
                    }
                }
        }

    }

}