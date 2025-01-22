package com.topdownedge.presentation.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topdownedge.domain.repositories.PriceDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    priceDataRepository: PriceDataRepository
) : ViewModel() {

    init {
        viewModelScope.launch() {

        }
    }
}