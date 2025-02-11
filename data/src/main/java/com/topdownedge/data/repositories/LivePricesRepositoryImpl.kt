package com.topdownedge.data.repositories

import android.util.Log
import com.topdownedge.data.remote.dto.AuthResponse
import com.topdownedge.data.remote.dto.LivePriceDto
import com.topdownedge.domain.repositories.LivePricesRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.wss
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


class LivePricesRepositoryImpl @Inject constructor(
    val wssClient: HttpClient
) : LivePricesRepository {

    private var session: DefaultWebSocketSession? = null
    private var isAuthorized = AtomicBoolean(false)
    private val messageQueue: Queue<String> = ConcurrentLinkedQueue()

    override fun observeLivePricesConnection(): Flow<Map<String, Double>> = callbackFlow {

        wssClient.wss(path = "us") {
            session = this

            val latestPricesMap = mutableMapOf<String, Double>()

            try {
                val authResponse = waitForAuth()
                if (authResponse?.status_code != 200) {
                    throw Exception("Authorization failed: ${authResponse?.message}")
                } else {
                    isAuthorized.set(true)
                    while (messageQueue.isNotEmpty()) {
                        send(Frame.Text(messageQueue.poll()))
                    }
                }

                incoming
                    .receiveAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .mapNotNull { frame ->
                        LivePriceDto.parse(frame.readText())
                    }
                    .collect { livePriceDto ->
                        latestPricesMap[livePriceDto.symbol] = livePriceDto.price
                        trySend(latestPricesMap.toMap())
//                        Log.d("XXX", "${livePriceDto.symbol} - ${livePriceDto.price}")
                    }

            } catch (e: Exception) {
                e.printStackTrace()
                clearSession()
                cancel("Flow cancelled due to error", e)
            }

        }

        awaitClose { clearSession() }
    }
        .sample(200)
        .flowOn(Dispatchers.IO)

    override fun closeLivePricesConnection() {
        clearSession()
    }

    override fun subscribeToLivePrices(tickers: List<String>) {
        subscribeUnsubscribe("subscribe", tickers)
    }

    override fun unsubscribeFromLivePrices(tickers: List<String>) {
        subscribeUnsubscribe("unsubscribe", tickers)
    }

    private fun subscribeUnsubscribe(action: String, tickers: List<String>) {
        if (tickers.isEmpty()) return

        val message = """{"action": "$action", "symbols": "${tickers.joinToString(",")}"}"""
        if (isAuthorized.get()) {
            CoroutineScope(Dispatchers.IO).launch {
                session?.send(Frame.Text(message))
            }
        } else {
            messageQueue.add(message)
        }
    }

    private fun clearSession() {
        isAuthorized.set(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                session?.close()
            } catch (e: Exception){
                e.printStackTrace()
            } finally {
                session = null
            }
        }
    }

    private suspend fun DefaultWebSocketSession.waitForAuth(): AuthResponse? {
        // Wait for the first message which should be the auth response
        val frame = incoming.receive() as? Frame.Text
            ?: throw Exception("Expected text frame for auth")
        return AuthResponse.parse(frame.readText())
    }

}