package com.topdownedge.presentation.portfolio.trade


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.entities.common.Ticker
import com.topdownedge.domain.repositories.MarketInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssetSearchUiState(
    var assets: List<Ticker> = emptyList(),
    var isLoading: Boolean = false,
    var messageError: String = "",
    var messageNoResults: String = "",
)

@HiltViewModel
class AssetSearchViewModel @Inject constructor(
    private val marketInfoRepository: MarketInfoRepository
) : ViewModel() {

    val assetSearchState: StateFlow<AssetSearchUiState>
        field = MutableStateFlow(AssetSearchUiState())

    init {
        viewModelScope.launch {

            marketInfoRepository.getInitialSearchTickerList().collect { result ->
                if (result.isSuccess) {
                    assetSearchState.update {
                        it.copy(
                            assets = result.getOrNull()!!
                        )
                    }
                } else {
                    Log.e("XXX", "tickers.isFailure: ${result.exceptionOrNull()}")
                }
            }

//            marketInfoRepository.getAllTickerList("")
        }

    }
}