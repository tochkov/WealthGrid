package com.topdownedge.data.repositories

import android.app.Application
import android.content.Context
import com.topdownedge.domain.repositories.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val context: Application
) : TokenRepository {

    private val KEY_PREFERENCES = "wg_prefs"
    private val KEY_TOKEN = "key_api_token"

    private var token: String = ""

    override fun hasApiToken(): Boolean {
        return getApiToken().isNotEmpty()
    }

    override fun getApiToken(): String {
        if (token.isEmpty()) {
            token = loadToken()
        }
        return token
    }

    override fun setApiToken(token: String) {
        context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE).edit()
            .putString(KEY_TOKEN, token)
            .apply()
        this.token = token
    }

    override fun clearApiToken() {
        setApiToken("")
    }

    private fun loadToken(): String {
        return context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, "")!!
    }


}