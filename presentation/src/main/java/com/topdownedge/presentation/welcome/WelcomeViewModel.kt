package com.topdownedge.presentation.welcome

import androidx.lifecycle.ViewModel
import com.topdownedge.domain.repositories.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel
@Inject constructor(
    val tokenRepository: TokenRepository
) : ViewModel() {

    fun saveApiToken(token: String) {
        tokenRepository.setApiToken(token)
    }

}