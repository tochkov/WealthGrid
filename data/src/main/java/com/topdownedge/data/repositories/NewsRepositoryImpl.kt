package com.topdownedge.data.repositories

import com.topdownedge.data.remote.EodhdNewsApi
import com.topdownedge.domain.repositories.NewsRepository
import de.jensklingenberg.ktorfit.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsRepositoryImpl
@Inject constructor(private val newsApi: EodhdNewsApi) : NewsRepository {


    init {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newsResponse = newsApi.getNewsForSymbol(symbol = "AAPL.US")
//                val newsResponse = newsApi.getNewsForTopic(topic = "class action")
                val ss = newsResponse.toApiResult()
                println("XXXX ${newsResponse.body()?.size ?: -1}")
                when (newsResponse.code) {
                    in 200..299 -> {
                        newsResponse.body()?.forEach {
//                            println("XXXX ${it.title}")
                        }
                    }

                    404 -> {
                        println("XXXX 404}")
                    }

                    in 400..499 -> {
                        println("XXXX 499")
                    }

                    in 500..599 -> {

                    }

                    else -> {
                        println("XXXX else")
                    }
                }
            } catch (e: Exception) {
                println("XXXX ${e}")
                e.printStackTrace()
            }
        }

    }


}

sealed class ApiResult<V>(val data: V) {
    class Success<V>(data: V) : ApiResult<V>(data)
    class Loading()
    class Error()
}

fun <T> Response<T>.toApiResult(): ApiResult<T> {
    return ApiResult.Success(this.body()!!)
}