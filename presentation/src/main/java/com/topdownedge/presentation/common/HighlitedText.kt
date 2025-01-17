package com.topdownedge.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun HighlightedText(
    text: String,
    highlightSegment: String,
    modifier: Modifier = Modifier,
//    normalColor: Color = Color.Black,
    highlightColor: Color = MaterialTheme.colorScheme.primary
) {
    if (highlightSegment.isEmpty()) {
        Text(text = text, modifier = modifier)
        return
    }

    val annotatedString = buildAnnotatedString {
        var startIndex = 0
        var matchIndex: Int
        val searchQueryLower = highlightSegment.lowercase()
        val textLower = text.lowercase()

        while (startIndex < text.length) {
            matchIndex = textLower.indexOf(searchQueryLower, startIndex)
            if (matchIndex == -1) {
                // No more matches, append the rest of the text
                append(text.substring(startIndex))
                break
            }

            // Append text before the match
            if (matchIndex > startIndex) {
                append(text.substring(startIndex, matchIndex))
            }

            // Append the matched text with different style
            withStyle(
                style = SpanStyle(
                    color = highlightColor,
                    fontWeight = FontWeight.ExtraBold
                )
            ) {
                append(text.substring(matchIndex, matchIndex + highlightSegment.length))
            }

            startIndex = matchIndex + highlightSegment.length
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier
    )
}