package com.topdownedge.domain.entities

data class UserPortfolio(
    val positions: List<UserPosition>,

    val totalInvested: Double,
    val currentValue: Double,

    val totalPNL: Double,
    val totalPNLPercent: Double,


) {


}