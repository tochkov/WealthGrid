package com.topdownedge.presentation.portfolio.trade

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.topdownedge.domain.entities.NewsArticle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstrumentPickerScreen(
    onListItemClick: (NewsArticle) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pick Instrument Screen") }
            )
        }
    ) { innerPadding ->
        Text(
            text = "PickInstrumentScreen",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(innerPadding)
        )
    }
}