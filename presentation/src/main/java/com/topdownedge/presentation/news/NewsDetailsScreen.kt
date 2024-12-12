package com.topdownedge.presentation.news

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
internal fun NewsDetailsScreen(
    newsId: String
){
    Text(
        text = "This is News #$newsId",
        fontSize = 36.sp
    )


}