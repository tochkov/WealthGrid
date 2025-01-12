package com.topdownedge.presentation.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.entities.NewsCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewsListScreen(
    onListItemClick: (String) -> Unit = {}
) {
    val viewModel: NewsListViewModel = hiltViewModel()
    val uiState = viewModel.newsState.collectAsStateWithLifecycle().value
    val scrollState = rememberLazyListState()
    val PRELOAD_TRESHOLD = 5

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshCategory() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    val categories = mutableListOf(
                        NewsCategory.General,
                        NewsCategory.Ticker("AAPL"),
                        NewsCategory.Topic("zacks rank")
                    )
                    items(categories.size) { i ->
                        val isSelected = categories[i] == uiState.selectedCategory
                        Button(
                            onClick = {
                                viewModel.onCategoryChange(categories[i])
                            },
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = categories[i].title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            items(uiState.news.size) { position ->
                LaunchedEffect(scrollState) {
                    if (position >= uiState.news.size - 1 - PRELOAD_TRESHOLD
                        && uiState.hasMoreToLoad
                        && !uiState.isLoading
                    ) {
                        viewModel.loadNextPage()
                    }
                }
                NewsItemCard2(newsArticle = uiState.news[position])
            }

            item {
                if (uiState.paginationError) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.errorMassage)
                        Button(
                            onClick = {
                                viewModel.loadNextPage()
                            }
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
            item {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

    }
}

//@Preview(showBackground = true)
@Composable
fun NewsItemCard2(
    newsArticle: NewsArticle,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        ) {
            Text(
//                text = "NewsHeading Ashweaganda, Ashkolsun mashala Starwars The Gateman of the Galaxy",
                text = newsArticle.title,
                fontSize = 24.sp,
//                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge,

            )
            Text(
//                text = "News subtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asd",
                text = newsArticle.content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)

                )
                Text(
//                    text = "url.com",
                    text = newsArticle.source,
                    modifier = Modifier.weight(1f)
                )
                Text(
//                    text = "2024-12-21"
                    text = newsArticle.date
                )
            }
        }
    }
}
