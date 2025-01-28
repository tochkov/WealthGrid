package com.topdownedge.domain.usecases

import com.topdownedge.domain.entities.UserPortfolio
import com.topdownedge.domain.repositories.PriceDataRepository
import com.topdownedge.domain.repositories.UserPortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetPortfolioUseCase(
    private val userPortfolioRepository: UserPortfolioRepository,
    private val priceDataRepository: PriceDataRepository
) {

    // construct portfolio from Positions and Trades
    // calculate portfolio returns
    // calculate portfolio value
    // calculate unrealizedProfit


    fun getPortfolio(): Flow<UserPortfolio> = flow {





    }


}