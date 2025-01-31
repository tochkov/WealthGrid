package com.topdownedge.data.di


import com.topdownedge.data.BuildConfig
import com.topdownedge.data.remote.EodhdFundamentalsApi
import com.topdownedge.data.remote.EodhdNewsApi
import com.topdownedge.data.remote.EodhdPriceDataApi
import com.topdownedge.data.remote.createEodhdFundamentalsApi
import com.topdownedge.data.remote.createEodhdNewsApi
import com.topdownedge.data.remote.createEodhdPriceDataApi
import com.topdownedge.domain.repositories.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Named
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("ktorfit_eodhd")
    fun provideKtorfit(tokenManager: TokenRepository): Ktorfit {
        return Ktorfit.Builder()
            //.baseUrl(baseUrl) // Cant add default parameter, when baseUrl set here
            .converterFactories(ResponseConverterFactory())
            .httpClient(
                HttpClient {
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        })
                    }
                    install(Logging) {
                        logger = Logger.SIMPLE
                        level = LogLevel.ALL
                    }
                    install(HttpTimeout) {
                        this.requestTimeoutMillis = 30_000
                    }
                    defaultRequest {
                        url {
                            takeFrom(BuildConfig.BASE_URL_EODHD)
                            parameters.append("api_token", tokenManager.getApiToken())
                        }
                    }
                }
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideEodhdNewsApi(@Named("ktorfit_eodhd") ktorfit: Ktorfit): EodhdNewsApi {
        return ktorfit.createEodhdNewsApi()
    }

    @Provides
    @Singleton
    fun provideEodhdFundamentalsApi(@Named("ktorfit_eodhd") ktorfit: Ktorfit): EodhdFundamentalsApi {
        return ktorfit.createEodhdFundamentalsApi()
    }

    @Provides
    @Singleton
    fun provideEodhdPriceDataApi(@Named("ktorfit_eodhd") ktorfit: Ktorfit): EodhdPriceDataApi {
        return ktorfit.createEodhdPriceDataApi()
    }

    @Provides
    @Singleton
//    @Named("ktor_websocket_eodhd")
    fun provideKtorWebsocketClient(tokenManager: TokenRepository) = HttpClient {
        install(WebSockets)

        defaultRequest {
            url {
                takeFrom(BuildConfig.BASE_URL_EODHD_WSS)
                parameters.append("api_token", tokenManager.getApiToken())
            }
        }
    }

}


