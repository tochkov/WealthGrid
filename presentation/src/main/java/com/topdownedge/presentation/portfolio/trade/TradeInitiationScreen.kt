package com.topdownedge.presentation.portfolio.trade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.topdownedge.domain.entities.NewsArticle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TradeInitiationScreen(
    navigateToInstrumentPicker: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Initiate Trade") }
            )
        }
    ) { innerPadding ->
        var textField1Value by remember { mutableStateOf("") }
        var textField2Value by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            // First TextField
            TextField(
                value = textField1Value,
                onValueChange = { /* Disabled input */ },

                label = {
                    Text("Label")
                },
//                placeholder = {
//                    Text("Placeholder")
//                },

                enabled = false,

                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = navigateToInstrumentPicker)
            )

            // Second TextField
            TextField(
                value = textField2Value,
                onValueChange = { /* Disabled input */ },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { textField2Value = "You clicked the second field!" }
            )

            // First Button
            Button(
                onClick = { textField1Value = "Button 1 was pressed!" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Button 1")
            }

            // Second Button
            Button(
                onClick = { textField2Value = "Button 2 was pressed!" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Button 2")
            }
        }
    }
}