package com.topdownedge.wealthgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.topdownedge.domain.repositories.TokenRepository
import com.topdownedge.presentation.WealthGridApp
import com.topdownedge.wealthgrid.ui.theme.WealthGridTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WealthGridTheme {
                WealthGridApp(tokenManager.hasApiToken())
            }
        }
    }
}
