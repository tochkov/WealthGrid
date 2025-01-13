package com.topdownedge.presentation.portfolio.trade


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.repositories.MarketInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstrumentPickerViewModel
@Inject constructor(private val marketInfoRepository: MarketInfoRepository) : ViewModel() {

    init {

        viewModelScope.launch() {

            val tickers = marketInfoRepository.getIndexTickersList("")
            if (tickers.isFailure) {
                Log.e("XXX", "(tickers.isFailure: ${tickers.exceptionOrNull()}")
            } else {
                for (i in 0..10) {
                    Log.e("XXX", "----vm------- ${tickers.getOrNull()?.get(i)}")
                }
            }

//            marketInfoRepository.getAllTickerList("")
        }

    }
}