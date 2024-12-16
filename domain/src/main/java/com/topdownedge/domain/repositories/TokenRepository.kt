package com.topdownedge.domain.repositories

interface TokenRepository {

    fun hasApiToken(): Boolean
    fun getApiToken(): String
    fun setApiToken(token: String)
    fun clearApiToken()

}