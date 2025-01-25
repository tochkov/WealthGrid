package com.topdownedge.presentation.portfolio.trade


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.domain.repositories.MarketInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssetSearchUiState(
    var assets: List<Ticker> = emptyList(),
    var noResultsState: Boolean = false
)

@HiltViewModel
class AssetSearchViewModel @Inject constructor(
    private val marketInfoRepository: MarketInfoRepository
) : ViewModel() {

    val uiState: StateFlow<AssetSearchUiState>
        field = MutableStateFlow(AssetSearchUiState())

    init {
        viewModelScope.launch {
            getInitialTickerList(false)
        }
    }

    suspend fun getInitialTickerList(fromCacheOnly: Boolean) {
        marketInfoRepository.getInitialSearchTickerList(fromCacheOnly)
            .distinctUntilChanged()
            .collect { result ->
                if (result.isSuccess) {
                    uiState.update {
                        it.copy(
                            assets = result.getOrNull()!!
                        )
                    }
                } else {
                    Log.e("XXX", "tickers.isFailure: ${result.exceptionOrNull()}")

                }
            }
    }

    fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                getInitialTickerList(true)
                uiState.update { it.copy(noResultsState = false) }
            } else {
                marketInfoRepository.getTickersForSearch(query)
                    .distinctUntilChanged()
                    .collect { result ->
                        if (result.isSuccess) {
                            uiState.update {
                                it.copy(
                                    noResultsState = result.getOrNull()!!.isEmpty(),
                                    assets = result.getOrNull()!!
                                )
                            }
                        } else {
                            Log.e("XXX", "ERRRORRRRR---")
                        }
                    }
            }
        }

    }
}