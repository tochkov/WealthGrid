package com.topdownedge.presentation.portfolio.trade

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.presentation.ui.theme.WealthGridTheme

@Composable
internal fun AssetSearchScreen(
    onListItemClick: (NewsArticle) -> Unit = {}
) {
    val viewModel: AssetSearchViewModel = hiltViewModel()
    val uiState = viewModel.assetSearchState.collectAsStateWithLifecycle().value

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SearchBoxAppBar(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    Log.e("XXX", "::: ${searchQuery}")
                },
                onBackClick = { }
            )
        }
    ) { innerPadding ->
        innerPadding
        LazyColumn {

            items(uiState.assets.size) { position ->
                Row {
                    Text(
                        text = uiState.assets[position].name,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(
                        text = uiState.assets[position].indexWeight.toString(),
                        modifier = Modifier.padding(16.dp)
                    )
                }

            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBoxAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowInfo = LocalWindowInfo.current
    val focusRequester = remember { FocusRequester() }

    // https://stackoverflow.com/a/75985103/4621448
    LaunchedEffect(windowInfo) {
        snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
            if (isWindowFocused) {
                focusRequester.requestFocus()
            }
        }
    }


    TopAppBar(
        title = {
            Box {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .clip(RoundedCornerShape(percent = 50)),
                    placeholder = {
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchQueryChange("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        }
    )
}


// Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchTopBarPreview() {
    WealthGridTheme {
        AssetSearchScreen(

        )
    }
}