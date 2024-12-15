package com.topdownedge.presentation.welcome

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.topdownedge.presentation.R

@Composable
internal fun WelcomeScreen(
    onSuccessfulClickNext: () -> Unit = {}
) {

    val viewModel: WelcomeViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var inputValue by remember { mutableStateOf("") }
        val toastNoToken =
            Toast.makeText(LocalContext.current, R.string.toast_no_token, Toast.LENGTH_SHORT)
        val subTitle = stringResource(R.string.welcome_subtitle)
        val hyperlinkText = stringResource(R.string.welcome_subtitle_hyperlink)

        val annotatedLinkString: AnnotatedString = remember {
            buildAnnotatedString {
                val style = SpanStyle(fontSize = 14.sp)

                val styleCenter = SpanStyle(
                    color = Color.Blue,
                    fontSize = 15.sp,
                    textDecoration = TextDecoration.Underline
                )
                withStyle(style) {
                    append(subTitle)
                }
                withLink(LinkAnnotation.Url(url = "https://eodhd.com/register")) {
                    withStyle(styleCenter) {
                        append(hyperlinkText)
                    }
                }
            }
        }

        Text(
            text = stringResource(R.string.welcome_title),
            fontSize = 36.sp,
        )
        Text(
            text = annotatedLinkString,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 16.dp
            )
        )
        TextField(
            value = inputValue,
            onValueChange = { inputValue = it },
        )
        Button(
            onClick = {
                if (inputValue.isBlank()) {
                    toastNoToken.show()
                } else {
                    viewModel.saveApiToken(inputValue)
                    onSuccessfulClickNext()
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.button_next),
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    WelcomeScreen()
}