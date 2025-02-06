package com.topdownedge.data.remote

import com.topdownedge.data.remote.dto.LastKnownPriceDto
import com.topdownedge.data.remote.dto.PriceBarDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query


interface EodhdPriceDataApi {
    /**
     * Fetches historical stock price data from EODHD API.
     * https://eodhd.com/financial-apis/api-for-historical-data-and-volumes
     *
     * @param symbol Consists of two parts: {SYMBOL_NAME}.{EXCHANGE_ID}
     *              For example: MCD.US for NYSE or MCD.MX for Mexican Stock Exchange
     * @param fromDate Optional start date in 'YYYY-MM-DD' format
     *                 Example: '2017-01-05' for January 5, 2017
     * @param toDate Optional end date in 'YYYY-MM-DD' format
     *               Example: '2017-02-10' for February 10, 2017
     * @param period Data aggregation period:
     *              'd' for daily prices (default)
     *              'w' for weekly prices
     *              'm' for monthly prices
     *              Note: Weekly and monthly data includes corresponding volume data
     * @param format The output format. Possible values are 'csv' for CSV output and 'json' for JSON output
     *              Default value: 'csv'
     *
     * @return List of price bars containing open, high, low, close, adjusted close prices and volume data
     */
    @GET("eod/{symbol}")
    suspend fun getHistoricalPrices(
        @Path("symbol") symbol: String,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null,
        @Query("period") period: String = "d",
        @Query("fmt") format: String = "json",
    ): Response<List<PriceBarDto>>


    /**
     * Fetches real-time (delayed) stock price data from EODHD API.
     * https://eodhd.com/financial-apis/api-for-real-time-stock-api/
     *
     * @param symbol Consists of two parts: {SYMBOL_NAME}.{EXCHANGE_ID}
     *              For example: AAPL.US for NYSE or TSLA.US for NASDAQ
     * @param additionalSymbols Optional comma-separated list of additional symbols to fetch in a single request
     *                         Example: 'VTI.US,EUR.FOREX'
     *                         Note: It's recommended to limit the number of tickers to 15-20 for optimal performance
     * @param format The output format. Possible values are 'csv' for CSV output and 'json' for JSON output
     *              Default value: 'json'
     *
     * @return List of last known prices containing code, timestamp, open, high, low, close, volume, and change data
     *         Note: Data is provided with a 15-20 minute delay and 1-minute interval frequency
     */
    @GET("real-time/{symbol}")
    suspend fun getRealTimePrices(
        @Path("symbol") symbol: String,
        @Query("s") additionalSymbols: String? = null,
        @Query("fmt") format: String = "json",
    ): Response<List<LastKnownPriceDto>>


}
