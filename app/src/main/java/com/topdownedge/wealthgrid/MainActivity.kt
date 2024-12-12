package com.topdownedge.wealthgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.topdownedge.presentation.navigation.WealthGridApp
import com.topdownedge.wealthgrid.ui.theme.WealthGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WealthGridTheme {
                WealthGridApp()
            }
        }
    }
}
