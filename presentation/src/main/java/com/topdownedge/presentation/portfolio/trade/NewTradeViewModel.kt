package com.topdownedge.presentation.portfolio.trade

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject


data class NewTradeUiState(
    var ticker: String = "",
    var date: LocalDate = LocalDate.now(),
    var price: Long = 0L,
    var shares: Int = 0
)

@HiltViewModel
class NewTradeViewModel @Inject constructor() : ViewModel() {

    val assetSearchState: StateFlow<NewTradeUiState>
        field = MutableStateFlow(NewTradeUiState())

}