package com.topdownedge.presentation.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun NewsListScreen(
    onListItemClick: (String) -> Unit = {}
) {

    val viewModel: NewsListViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        repeat(25) {
            Button(
                onClick = { onListItemClick(it.toString()) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "NEWS $it"
                )
            }
        }
    }
}