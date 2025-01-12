package com.topdownedge.presentation.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.topdownedge.domain.entities.NewsArticle

@Composable
internal fun NewsDetailsScreen(
    newsTitle: String,
    newsContent: String,
) {
    Scaffold { innerPadding ->
        Column {
            Text(
                text = newsTitle,
                modifier = Modifier.padding(innerPadding),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = newsContent,
                modifier = Modifier.padding(innerPadding),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }


}