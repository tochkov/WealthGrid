package com.topdownedge.presentation.welcome

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun WelcomeScreen(
    onClickNext: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var value by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        TextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.focusRequester(focusRequester)
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Button(
            onClick = { onClickNext(value) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Enter Token"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    WelcomeScreen()
}