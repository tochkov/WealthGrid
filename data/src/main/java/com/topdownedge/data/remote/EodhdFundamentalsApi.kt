package com.topdownedge.data.remote

import com.topdownedge.data.remote.dto.IndexResponse
import com.topdownedge.data.remote.dto.SymbolItemDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

/**
 * Interface for accessing EODHD Fundamentals API endpoints.
 * Provides access to exchange symbols and index constituents data.
 *
 * with the help of Claude - https://claude.ai/chat/0ff0b92e-0054-457b-a70e-17470203dc41
 */
interface EodhdFundamentalsApi {

    /**
     * Retrieves a list of symbols for a specific exchange.
     * @see [EODHD API Documentation](https://eodhd.com/financial-apis/exchanges-api-list-of-tickers-and-trading-hours#Get_List_of_Tickers_Exchange_Symbols)
     *
     * @param exchangeCode The exchange code (e.g., "US" for all US exchanges, "NYSE", "NASDAQ", etc.)
     * @param format Response format (default: "json")
     * @param type Optional filter for ticker type. Possible values:
     *             - "common_stock" for common stocks only
     *             - "preferred_stock" for preferred stocks only
     *             - "stock" for both common and preferred stocks
     *             - "etf" for ETFs only
     *             - "fund" for funds only
     * @return Response containing list of [SymbolItemDto] with symbol details including Code, Name, Country, Exchange
     */
    @GET("exchange-symbol-list/{exchange}")
    suspend fun getExchangeSymbolsList(
        @Path("exchange") exchangeCode: String = EXCHANGE_US_ALL,
        @Query("fmt") format: String = "json",
        @Query("type") type: String? = null
    ): Response<List<SymbolItemDto>>

    /**
     * Retrieves constituents (components) data for a specific index.
     * @see [EODHD API Documentation](https://eodhd.com/financial-apis/stock-etfs-fundamental-data-feeds#Current_and_Historical_Index_Constituents_API)
     *
     * The response includes detailed information for each component:
     * - Code: The ticker symbol
     * - Exchange: The exchange where the component is listed
     * - Name: Full company name
     * - Sector: Company's sector classification
     * - Industry: Specific industry within the sector
     *
     * @param indexCode The index code (e.g., "GSPC.INDX" for S&P 500)
     * @param format Response format (default: "json")
     * @return Response containing [IndexResponse] with index details and its components
     */
    @GET("fundamentals/{index}")
    suspend fun getIndexConstituents(
        @Path("index") indexCode: String = INDEX_SP500,
        @Query("fmt") format: String = "json",
    ): Response<IndexResponse>

    companion object {
        const val INDEX_SP500 = "GSPC.INDX"
        const val EXCHANGE_US_ALL = "US"

        // Common US exchanges
        const val EXCHANGE_NYSE = "NYSE"
        const val EXCHANGE_NASDAQ = "NASDAQ"
        const val EXCHANGE_BATS = "BATS"
        const val EXCHANGE_OTCQB = "OTCQB"
        const val EXCHANGE_PINK = "PINK"

        // Common ticker types
        const val TYPE_COMMON_STOCK = "common_stock"
        const val TYPE_PREFERRED_STOCK = "preferred_stock"
        const val TYPE_STOCK = "stock"
        const val TYPE_ETF = "etf"
        const val TYPE_FUND = "fund"
    }
}