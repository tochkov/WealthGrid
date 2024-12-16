package com.topdownedge.data.di


import com.topdownedge.data.remote.EodhdNewsApi
import com.topdownedge.data.remote.createEodhdNewsApi
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
    @Named("base_eodhd_url")
    fun provideBaseUrl(): String {
        //TODO move this in build config
        return "https://eodhd.com/api/"
    }

    @Provides
    @Singleton
    fun provideKtorfit(@Named("base_eodhd_url") baseUrl: String, tokenManager: TokenRepository): Ktorfit {
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
                            takeFrom(baseUrl)
                            parameters.append("api_token", tokenManager.getApiToken())
                        }
                    }
                }
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideEodhdNewsApi(ktorfit: Ktorfit): EodhdNewsApi {
        return ktorfit.createEodhdNewsApi()
    }
}